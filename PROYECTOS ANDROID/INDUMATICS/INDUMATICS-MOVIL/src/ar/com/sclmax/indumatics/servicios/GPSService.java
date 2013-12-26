package ar.com.sclmax.indumatics.servicios;

import android.app.Service;
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
	private static int readTime = 100000;
	private static String SENDNRO = "+543435177953";
	private Location loc = null;
	private LocationListener loclistener;
	private LocationManager locmanager;
	private boolean SendApagado = false;

	public void onCreate() {
		super.onCreate();

		new GpsData(this);

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
						SendSMS(SENDNRO, Utils.getFecha() + " - GPS PRENDIDO");
						SendApagado = false;
					}
				}

				public void onProviderDisabled(String provider) {
					setLog("GPS DISABLED");
					if (!SendApagado) {
						SendSMS(SENDNRO, Utils.getFecha() + " - GPS APAGADO");
						SendApagado = true;
					}
				}

				public void onLocationChanged(Location location) {
					// vdb.SendToServer();
					if (location != null) {
						try {
							setLoc(location);
						} catch (Exception e) {
							setLog("ERROR" + e.getMessage());
						}
					}
				}
			};
			locmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					readTime, readDist, loclistener);
			if (locmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
				setLoc(locmanager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER));
			}
		}
	}

	@Override
	public void onDestroy() {
		setLog("GPSService cerrado");
		super.onDestroy();
	}

	public void setLoc(Location newloc) {
		if (newloc.getAccuracy() < 70) {
			this.loc = newloc;
			String fecha;
			double lat;
			double lon;
			double precision;
			double velocidad = 0;
			fecha = Utils.getFecha();
			lat = loc.getLatitude();
			lon = loc.getLongitude();
			precision = loc.getAccuracy();
			velocidad = (loc.getSpeed() / 1000) * 3600;
			GpsData.GuardarDatos(fecha, lat, lon, precision, velocidad);
		}
	}

	private void setLog(String log) {
		GpsData.GuardarLog(Utils.getFecha(), log);
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
