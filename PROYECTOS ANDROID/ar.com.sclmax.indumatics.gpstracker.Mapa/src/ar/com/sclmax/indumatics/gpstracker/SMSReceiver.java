package ar.com.sclmax.indumatics.gpstracker;

import java.util.StringTokenizer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver {

	private static GPSinteface gpsInteface;
	static String LOCATE = "L9991";
	static String RESET = "L9992";

	/** Called when the activity is first created. */
	@Override
	public void onReceive(Context context, Intent intent) {

		double lat = 0;
		double lon = 0;
		String body = "";
		String fecha = "";
		String pre = "";
		String vel = "";
		String tel = "";

		Bundle bundle = intent.getExtras();

		SmsMessage[] msgs = null;
		if (bundle != null) {
			Toast.makeText(context, "Mensaje recibido " + body,
					Toast.LENGTH_LONG).show();
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				tel = msgs[i].getOriginatingAddress().toString();
				body = msgs[i].getMessageBody().toString();
				if (body.contains(LOCATE)) {
					StringTokenizer st1 = new StringTokenizer(body.toString(),
							"@");
					while (st1.hasMoreTokens()) {
						String paquete = st1.nextToken();
						StringTokenizer st = new StringTokenizer(paquete, "|");
						while (st.hasMoreTokens()) {
							String token = st.nextToken();
							if (token.contains("LAT")) {
								lat = Double.parseDouble(token.replace("LAT",
										"").toString());
							} else if (token.contains("LON")) {
								lon = Double.parseDouble(token.replace("LON",
										"").toString());
							} else if (token.contains("PRE")) {
								pre = token.replace("PRE", "").toString();
							} else if (token.contains("VEL")) {
								vel = token.replace("VEL", "").toString();
							} else if (!token.contains("LOCATE")) {
								fecha = token;
							}
						}
						gpsInteface.updateLocationSMS(tel, lat, lon, tel
								+ " - " + fecha, "LAT: " + Double.toString(lat)
								+ "\n" + "LON: " + Double.toString(lon) + "\n"
								+ "PRE: " + pre + "\n" + "VEL: " + vel);
					}
				}
			}

		}
	}

	public static void setGpsInteface(GPSinteface gpsInteface) {
		SMSReceiver.gpsInteface = gpsInteface;
	}

}
