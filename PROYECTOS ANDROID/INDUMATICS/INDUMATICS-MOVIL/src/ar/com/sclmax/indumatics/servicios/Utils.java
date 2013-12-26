package ar.com.sclmax.indumatics.servicios;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utils {

	final static String LOCATE = "LOCATE";
	final static String RESET = "L9992";

	/**
	 * Retorna la Fecha Actual en formato YYYY/MM/DD hh:mm:ss
	 * @return String
	 */
	static public String getFecha() {
		Date t = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
		return sdf.format(t).toString();
	}

	/**
	 * Redondea a un numero de dígitos determinado un valor
	 * 
	 * @param number
	 *            <- Valor
	 * @param places
	 *            <- Numero de dígitos
	 * @return -> Nuevo valor
	 */

	static public float round(float val, int places) {
		long factor = (long) Math.pow(10, places);
		val = val * factor;
		long tmp = Math.round(val);
		return (float) tmp / factor;
	}

	public void showMsg(Context context, String title, String msg,
			String txtButton) {
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(msg)
				.setNeutralButton(txtButton,
						new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Acción a realizar
							}
						}).show();
	}

	/**
	 * Calcula la distancia en Metros entre dos coordenadas GPS
	 * 
	 * @param lon1
	 * @param lat1
	 * @param lon2
	 * @param lat2
	 * @return int (Distancia en Metros)
	 */
	static public int calcularDistancia(double lon1, double lat1, double lon2,
			double lat2) {

		double earthRadius = 6371; // km
		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);
		double dlon = (lon2 - lon1);
		double dlat = (lat2 - lat1);
		double sinlat = Math.sin(dlat / 2);
		double sinlon = Math.sin(dlon / 2);
		double a = (sinlat * sinlat) + Math.cos(lat1) * Math.cos(lat2)
				* (sinlon * sinlon);
		double c = 2 * Math.asin(Math.min(1.0, Math.sqrt(a)));
		double distanceInMeters = earthRadius * c * 1000;
		return (int) distanceInMeters;
	}
}
