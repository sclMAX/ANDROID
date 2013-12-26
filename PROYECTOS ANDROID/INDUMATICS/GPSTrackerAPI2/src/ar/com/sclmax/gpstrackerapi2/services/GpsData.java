package ar.com.sclmax.gpstrackerapi2.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import ar.com.sclmax.gpstrackerapi2.data.CamionPos;
import ar.com.sclmax.gpstrackerapi2.data.Equipo;
import ar.com.sclmax.gpstrackerapi2.data.IndumaticsDB;
import ar.com.sclmax.gpstrackerapi2.data.WayPoint;

public class GpsData {
	private static IndumaticsDB db = null;

	public GpsData(Context context) {
		db = dbConnect(context, "indumatics.db");
	}

	public GpsData(Context context, String dbname) {
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

	public static void GuardarHistorial(CamionPos pos) {
		try {
			SQLiteDatabase tGPS = db.getWritableDatabase();
			if (db != null) {
				ContentValues data = new ContentValues();
				data.put("fecha", pos.getFecha());
				data.put("telefono", pos.getId());
				data.put("latitud", pos.getLatitud());
				data.put("longitud", pos.getLongitud());
				data.put("precision", pos.getPresicion());
				data.put("velocidad", pos.getVelocidad());
				tGPS.insert("sms", "1", data);
				tGPS.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Cursor getAllReg(String tabla) throws SQLException {
		SQLiteDatabase tGPS = db.getReadableDatabase();
		if (db != null) {
			return tGPS.rawQuery("SELECT * FROM " + tabla, null);

		} else
			return null;
	}

	public static Cursor getHistorial(String tabla, String tel, String fecha) {
		try {
			SQLiteDatabase tGPS = db.getReadableDatabase();
			if (db != null) {
				String dia = "%";
				if (!fecha.equals("%")) {
					dia = fecha + "%";
				}
				String[] args = new String[] { tel, dia };
				return tGPS.rawQuery("SELECT * FROM " + tabla
						+ " WHERE (telefono = ?) AND (fecha LIKE ?)ORDER BY fecha ;", args);
			} else
				return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// Manejo DB Equipos.
	public static void setEquipo(Equipo tel) throws SQLException {

		SQLiteDatabase tGPS = db.getWritableDatabase();
		try {
			if (db != null) {
				ContentValues data = new ContentValues();
				data.put("idequipo", tel.getIdequipo());
				data.put("nombre", tel.getNombre());
				data.put("color", tel.getColor());
				tGPS.insert("equipos", "1", data);
			}
		} finally {
			tGPS.close();
		}
	}

	public static Equipo getOneEquipo(String idEquipo) {
		Equipo tel = new Equipo();
		SQLiteDatabase tGPS = db.getReadableDatabase();
		if (db != null) {
			String[] args = new String[] { idEquipo };
			Cursor res = tGPS.rawQuery(
					"SELECT * FROM equipo WHERE idequipo = ?;", args);
			if (res.getCount() > 0) {
				tel.setIdequipo(res.getString(res
						.getColumnIndexOrThrow("idequipo")));
				tel.setNombre(res.getString(res.getColumnIndexOrThrow("nombre")));
				tel.setColor(res.getFloat(res.getColumnIndexOrThrow("color")));
				return tel;
			}
		}
		tGPS.close();
		return null;
	}
	
	public static void setWayPoint (WayPoint w) throws SQLException{
		SQLiteDatabase tWayPoint = db.getWritableDatabase();
		try{
		if(tWayPoint != null){
			ContentValues data = new ContentValues();
			data.put("nombre",w.getNombre());
			data.put("latitud", w.getLatitud());
			data.put("longitud", w.getLongitud());
			tWayPoint.insert("waypoint", "1", data);
		}
		}finally{
			tWayPoint.close();
		}
	}
	
	public static WayPoint getOneWayPoint(String nombre){
		SQLiteDatabase tWayPoint = db.getReadableDatabase();
		if(tWayPoint != null){
			if(!nombre.contains("%")){
				nombre = nombre + "%";
			}
			String[] args = new String[] {nombre};
			String sql = "SELECT * FROM waypoint WHERE nombre LIKE ?";
			Cursor cr = tWayPoint.rawQuery(sql, args);
			if(cr.getCount() > 0){
				WayPoint resultado = new WayPoint(cr.getString(cr.getColumnIndexOrThrow("nombre")),
						cr.getFloat(cr.getColumnIndexOrThrow("latitud")),
						cr.getFloat(cr.getColumnIndexOrThrow("longitud")));
				return resultado;
			}
		}
		return null;
	}
	
	public static int getLastWayPointId (){
		SQLiteDatabase tWayPoint = db.getReadableDatabase();
		if(tWayPoint != null){
			String sql = "SELECT id FROM waypoint;";
			Cursor cr = tWayPoint.rawQuery(sql, null);
			if(cr.getCount() > 0){
				if(cr.moveToLast()){
					return cr.getInt(0);
				}
			}
		}
		return 0;
	}
}
