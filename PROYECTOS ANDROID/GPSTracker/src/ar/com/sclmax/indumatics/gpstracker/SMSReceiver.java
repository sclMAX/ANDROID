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

	/** Called when the activity is first created. */
	@Override
	public void onReceive(Context context, Intent intent) {
		double lat = 0;
		double lon = 0;

		Bundle bundle = intent.getExtras();
		String body = "";
		SmsMessage[] msgs = null;
		if (bundle != null) {
			Toast.makeText(context, "Mensaje recibido " + body,
					Toast.LENGTH_LONG).show();
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				body = msgs[i].getMessageBody().toString();
				if (body.contains("LOCATE")) {

					StringTokenizer st = new StringTokenizer(body.toString(), "|");
					while (st.hasMoreTokens()) {
						String token = st.nextToken();
						if (token.contains("LAT")) {
							lat = Double.parseDouble(token.replace(
									"LAT", "").toString());
						}
						if (token.contains("LON")) {
							lon = Double.parseDouble(token.replace(
									"LON", "").toString());
						}						
					}
					gpsInteface.updateLocation(lat, lon);
				}
			}

		}
	}

	public static void setGpsInteface(GPSinteface gpsInteface) {
		SMSReceiver.gpsInteface = gpsInteface;
	}

}
