package ar.com.sclmax.indumatics.servicios;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import ar.com.sclmax.indumatics.data.GpsData;

public class GPSService extends Service {

    private static int readDist = 0;
    private static int readTime = 30000;
    private static GpsData vdb = null;
    private static String SENDNRO = "3435177953";
    private Location loc = null;
    private LocationListener loclistener;
    private LocationManager locmanager;
    private int SendCount = 0;
    private boolean SendApagado = false;

    public void onCreate() {
        super.onCreate();

        vdb = new GpsData(this);

        setLog("GPSService creado");
        locmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setLog("Location Manager Proveedor habilitado");
            loclistener = new LocationListener() {
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    setLog("GPS " + provider + Integer.toString(status));
                }

                public void onProviderEnabled(String provider) {
                    setLog("GPS ENABLED");
                    if (SendApagado) {
                        SendSMS(SENDNRO, getFecha() + " - GPS PRENDIDO");
                        SendApagado = false;
                    }
                }

                public void onProviderDisabled(String provider) {
                    setLog("GPS DISABLED");
                    if (!SendApagado) {
                        SendSMS(SENDNRO, getFecha() + " - GPS APAGADO");
                        SendApagado = true;
                    }
                }

                public void onLocationChanged(Location location) {
//					vdb.GuardarLog(getFecha(), "Location Changed");
                    if (location != null) {
                        try {
                            setLoc(location);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            locmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, readTime, readDist, loclistener);
            if (locmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null)
                setLoc(locmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
    }

    @Override
    public void onDestroy() {
        setLog("GPSService cerrado");
        super.onDestroy();
    }

    private String getFecha() {
        Date t = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        return sdf.format(t).toString();
    }

    public void setLoc(Location newloc) {
        if (newloc.getAccuracy() < 50) {
            this.loc = newloc;
            String fecha;
            String lat;
            String lon;
            float speed = 0;
            fecha = getFecha();

            lat = Float.toString(Utils.round((float) loc.getLatitude(), 5));
            lon = Float.toString(Utils.round((float) loc.getLongitude(), 5));
            String precision = Double.toString(Utils.round(loc.getAccuracy(), 1));
            speed = (loc.getSpeed() / 1000) * 3600;
            speed = Utils.round(speed, 1);
            String velocidad = Float.toString(Utils.round(speed, 1));

            vdb.GuardarDatos(fecha, lat, lon, precision, velocidad);
        }
    }

    private void setLog(String log) {
        vdb.GuardarLog(getFecha(), log);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void SendSMS(String nro, String mensaje) {
        SmsManager sms = SmsManager.getDefault();
        if ((mensaje != null) && (nro != null)) {
            sms.sendTextMessage(nro, null, mensaje, null, null);
        }
    }
}
