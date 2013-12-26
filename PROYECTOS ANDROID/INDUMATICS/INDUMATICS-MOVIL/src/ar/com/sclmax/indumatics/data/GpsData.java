package ar.com.sclmax.indumatics.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ar.com.sclmax.indumatics.servicios.Utils;

public class GpsData {
	private static IndumaticsDB db = null;
	private static Context smscontext;

	public GpsData(Context context) {
		smscontext = context;
		db = dbConnect(context, "indumatics.db");
	}

	public static IndumaticsDB dbConnect(Context context, String dbname) {
		smscontext = context;
		return new IndumaticsDB(context, dbname, null, 1);
	}

	public static IndumaticsDB getDb() {
		return db;
	}

	public static void resetDB() {
		SQLiteDatabase tGPS = db.getReadableDatabase();
		if (tGPS != null) {
			tGPS.rawQuery("DELETE FROM gpsdata;", null);
		}
		tGPS.close();
	}

	/**
	 * Buscar los �ltimos datos guardados en la BD
	 * 
	 * @param cantidad
	 *            determina la cantidad de registros a recuperar
	 * @return String conteniendo los registros(delimitados por @) concatenados
	 *         y los campos delimitados por |
	 */
	public static String getLastData(int cantidad) {
		String r = "";
		SQLiteDatabase tGPS = db.getReadableDatabase();
		if (tGPS != null) {
			String[] args = new String[] { "F" };
			Cursor res = tGPS.rawQuery(
					"SELECT * FROM gpsdata WHERE enviado=?;", args);
			int registros = res.getCount();
			if (registros >= cantidad) {
				for (int i = cantidad; i > 0; i--) {
					res.moveToPosition(registros - i);
					String vtxt = "@";
					vtxt += res.getString(res.getColumnIndexOrThrow("fecha"))
							+ "|";
					vtxt += res.getString(res.getColumnIndexOrThrow("latitud"))
							+ "|";
					vtxt += res
							.getString(res.getColumnIndexOrThrow("longitud"))
							+ "|";
					vtxt += res.getString(res
							.getColumnIndexOrThrow("precision")) + "|";
					vtxt += res.getString(res
							.getColumnIndexOrThrow("velocidad"));
					r += vtxt;
					setEnviado(res.getString(0));
				}
			} else if (registros == 1 && res.moveToFirst()) {
				String vtxt = "@";
				vtxt += res.getString(res.getColumnIndexOrThrow("fecha")) + "|";
				vtxt += res.getString(res.getColumnIndexOrThrow("latitud"))
						+ "|";
				vtxt += res.getString(res.getColumnIndexOrThrow("longitud"))
						+ "|";
				vtxt += res.getString(res.getColumnIndexOrThrow("precision"))
						+ "|";
				vtxt += res.getString(res.getColumnIndexOrThrow("velocidad"));
				r += vtxt;
				setEnviado(res.getString(0));
			}
		}
		tGPS.close();
		return r;
	}

	public static void setEnviado(String id) {
		try {
			SQLiteDatabase tGPS = db.getWritableDatabase();
			if (tGPS != null) {
				ContentValues data = new ContentValues();
				data.put("enviado", "T");
				tGPS.update("gpsdata", data, "id=" + id, null);
				tGPS.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void GuardarDatos(String fecha, double lat, double lon,
			double precision, double velocidad) {
		try {
			// <Variables>
			double lat1 = 0;
			double lon1 = 0;
			double vel1 = -1;
			double pre1 = 0;
			// </Variables>

			SQLiteDatabase tGPS = db.getWritableDatabase();

			if (tGPS != null) {
				Cursor rs = tGPS.rawQuery("SELECT * FROM gpsdata;", null);
				int registros = rs.getCount();
				if (rs.moveToLast()) {
					lat1 = rs.getDouble(rs.getColumnIndexOrThrow("latitud"));
					lon1 = rs.getDouble(rs.getColumnIndexOrThrow("longitud"));
					vel1 = rs.getDouble(rs.getColumnIndexOrThrow("velocidad"));
					pre1 = rs.getDouble(rs.getColumnIndexOrThrow("precision"));
					if (registros > 4000) {
						tGPS.delete("gpsdata", " id < (" + rs.getString(0)
								+ " - 1)", null);
						GuardarLog(
								Utils.getFecha(),
								"RESET DB 'gpsdata' WHERE [" + " id < ("
										+ rs.getString(0) + " - 1)]");
					}
				}

				int distancia = Utils.calcularDistancia(lon1, lat1, lon, lat);

				if (((velocidad == 0) && (velocidad != vel1))
						|| ((velocidad > 5) && (distancia > (pre1 + precision)) && (distancia > (velocidad * 8)))) {

					ContentValues data = new ContentValues();
					data.put("fecha", fecha);
					data.put("latitud",
							Float.toString(Utils.round((float) lat, 5)));
					data.put("longitud",
							Float.toString(Utils.round((float) lon, 5)));
					data.put("precision",
							Float.toString(Utils.round((float) precision, 1)));
					data.put("velocidad",
							Float.toString(Utils.round((float) velocidad, 1)));
					data.put("enviado", "F");
					tGPS.insert("gpsdata", "1", data);
				}
				tGPS.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String SendToServer() {
		if (isOnline()) {
			SQLiteDatabase tGPS = db.getReadableDatabase();
			if (tGPS != null) {
				String[] parametros = new String[] { "F" };
				Cursor rs = tGPS.rawQuery(
						"SELECT * FROM gpsdata WHERE enviado=?;", parametros);
				if (rs.moveToLast()) {
					String r = "";
					r += "+543434702490|";
					r += rs.getString(rs.getColumnIndexOrThrow("fecha")) + "|";
					r += rs.getString(rs.getColumnIndexOrThrow("latitud"))
							+ "|";
					r += rs.getString(rs.getColumnIndexOrThrow("longitud"))
							+ "|";
					r += rs.getString(rs.getColumnIndexOrThrow("precision"))
							+ "|";
					r += rs.getString(rs.getColumnIndexOrThrow("velocidad"))
							+ "|";
					return r;
				}
			}
		}
		return null;
	}

	public static void GuardarLog(String fecha, String log) {
		try {
			SQLiteDatabase tGPS = db.getWritableDatabase();
			if (tGPS != null) {
				Cursor rs = tGPS.rawQuery("SELECT * FROM log;", null);
				int registros = rs.getCount();
				if (registros > 5000) {
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

	public static boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) smscontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
