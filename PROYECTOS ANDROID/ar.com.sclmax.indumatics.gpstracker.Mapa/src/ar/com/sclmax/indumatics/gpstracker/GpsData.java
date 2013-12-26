package ar.com.sclmax.indumatics.gpstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GpsData {
	private static IndumaticsDB db = null;

	public GpsData(Context context) {
		db = dbConnect(context, "indumatics.db");
	}
	public GpsData(Context context, String dbname){
		db = dbConnect(context, dbname);
	}

	public static IndumaticsDB dbConnect(Context contex, String dbname) {
		return new IndumaticsDB(contex, dbname, null, 1);
	}

	public static IndumaticsDB getDb() {
		return db;
	}

	public static String last3Loc() {
		String r = "";
		SQLiteDatabase tGPS = db.getReadableDatabase();
		if (db != null) {
			Cursor res = tGPS.rawQuery("SELECT * FROM gpsdata;", null);
			int registros = res.getCount();
			if (registros >= 1) {
				res.moveToPosition(registros - 1);
				String vtxt = "LOCATE|";
				vtxt = vtxt + res.getString(1) + "|";
				vtxt = vtxt + "LAT" + res.getString(2) + "|";
				vtxt = vtxt + "LON" + res.getString(3) + "|";
				vtxt = vtxt + "PRE" + res.getString(4) + "|";
				vtxt = vtxt + "VEL" + res.getString(5);
				r = r + vtxt + "\n";
			}
		}
		tGPS.close();
		return r;
	}
	
	public static void GuardarHistorial(String fecha, String telefono, String lat, String lon, String titulo, String info) {
		try {
			SQLiteDatabase tGPS = db.getWritableDatabase();
			if (tGPS != null) {
				ContentValues data = new ContentValues();
				data.put("fecha", fecha);
				data.put("telefono", telefono);
				data.put("latitud", lat);
				data.put("longitud", lon);
				data.put("titulo", titulo);
				data.put("info", info);
				tGPS.insert("sms", "1", data);
				tGPS.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Cursor getAllReg(String tabla){
		SQLiteDatabase tGPS = db.getReadableDatabase();
		if(db != null){
			return tGPS.rawQuery("SELECT * FROM " + tabla, null);
		
		}else return null;
	}
	
	public static Cursor getHistorial(String tabla, String tel){
		SQLiteDatabase tGPS = db.getReadableDatabase();
		if(db != null){
			String[] args = new String[] {tel};
			return tGPS.rawQuery("SELECT * FROM " + tabla + " WHERE telefono = ?", args);		
		}else return null;
	}

}
