package ar.com.sclmax.indumatics.data;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import ar.com.sclmax.indumatics.servicios.Utils;


public class GpsData {
    private static IndumaticsDB db = null;

    public GpsData(Context context) {
        db = dbConnect(context, "indumatics.db");
    }

    public static IndumaticsDB dbConnect(Context contex, String dbname) {
        return new IndumaticsDB(contex, dbname, null, 1);
    }

    public static IndumaticsDB getDb() {
        return db;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void resetDB() {
        SQLiteDatabase tGPS = db.getReadableDatabase();
        if (tGPS != null) {
            tGPS.rawQuery("DELETE FROM gpsdata;", null);
        }
        tGPS.close();
    }

    /**
     * Buscar los ultimos datos guardados en la BD
     *
     * @param cantidad determina la cantidad de  registros a recuperar
     * @return String conteniendo los registros(delimitados por @) concatenados y los campos delimitados por |
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static String getLastData(int cantidad) {
        String r = null;
        SQLiteDatabase tGPS = db.getReadableDatabase();
        if (tGPS != null) {
            Cursor res = tGPS.rawQuery("SELECT * FROM gpsdata;", null);
            int registros = res.getCount();
            if (registros >= cantidad) {
                for (int i = cantidad; i > 0; i--) {
                    res.moveToPosition(registros - i);
                    String vtxt = "@";
                    vtxt = vtxt + res.getString(1) + "|";
                    vtxt = vtxt + res.getString(2) + "|";
                    vtxt = vtxt + res.getString(3) + "|";
                    vtxt = vtxt + res.getString(4) + "|";
                    vtxt = vtxt + res.getString(5);
                    r = r + vtxt;
                }
            }
        }
        tGPS.close();
        return r;
    }

    public void GuardarDatos(String fecha, String lat, String lon, String precision, String velocidad) {
        try {
            //<Variables>
            double lat2 = 0;
            double lon2 = 0;
            double vel2 = 0;
            double lat1 = 0;
            double lon1 = 0;
            double vel1 = 0;

            lat2 = Double.parseDouble(lat);
            lon2 = Double.parseDouble(lon);
            vel2 = Double.parseDouble(velocidad);

            //</Variables>
            SQLiteDatabase tGPS = db.getWritableDatabase();
            if (tGPS != null) {
                Cursor rs = tGPS.rawQuery("SELECT * FROM gpsdata;", null);
                int registros = rs.getCount();
                if (registros > 2000) {
                    tGPS.delete("gpsdata", null, null);
                } else {
                    if (rs.moveToLast()) {
                        lat1 = rs.getDouble(2);
                        lon1 = rs.getDouble(3);
                        vel1 = rs.getDouble(5);
                    }

                    int distancia = Utils.calcularDistancia(lon1, lat1, lon2, lat2);

                    if ((distancia > (vel2 * 8) && (vel1 != vel2))) {
                        ContentValues data = new ContentValues();
                        data.put("fecha", fecha);
                        data.put("latitud", lat);
                        data.put("longitud", lon);
                        data.put("precision", precision);
                        data.put("velocidad", velocidad);
                        tGPS.insert("gpsdata", "1", data);
                        tGPS.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GuardarLog(String fecha, String log) {
        try {
            SQLiteDatabase tGPS = db.getWritableDatabase();
            if (tGPS != null) {
                Cursor rs = tGPS.rawQuery("SELECT * FROM log;", null);
                int registros = rs.getCount();
                if (registros > 2000) {
                    tGPS.delete("log", null, null);
                }
                ContentValues data = new ContentValues();
                data.put("fecha", fecha);
                data.put("log", log);
                tGPS.insert("log", "1", data);
                tGPS.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
