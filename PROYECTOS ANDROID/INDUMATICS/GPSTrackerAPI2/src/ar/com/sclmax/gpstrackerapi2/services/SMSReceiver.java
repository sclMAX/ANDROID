package ar.com.sclmax.gpstrackerapi2.services;

import java.util.StringTokenizer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import ar.com.sclmax.gpstrackerapi2.data.CamionPos;
import ar.com.sclmax.gpstrackerapi2.data.Utils;

public class SMSReceiver extends BroadcastReceiver {

	private static GPSinteface gpsInteface;

	@Override
	public void onReceive(Context context, Intent intent) {
		String body = "";
		String tel = "";
		Bundle bundle = intent.getExtras();

		SmsMessage[] msgs = null;
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				tel = msgs[i].getOriginatingAddress().toString();
				body = msgs[i].getMessageBody().toString();
				if (body.contains(Utils.LOCATE)) {
					StringTokenizer st1 = new StringTokenizer(body.toString(),
							"@");
					while (st1.hasMoreTokens()) {
						CamionPos pos = new CamionPos();
						pos.setId(tel);
						String paquete = st1.nextToken();
						if (!paquete.contains(Utils.LOCATE)) {
							StringTokenizer st = new StringTokenizer(paquete,
									"|");
							int tokenindex = 0;
							while (st.hasMoreTokens()) {
								String token = st.nextToken();
								tokenindex = tokenindex + 1;
								switch (tokenindex) {
								case 1:
									pos.setFecha(token);
									break;
								case 2:
									pos.setLatitud(Double.parseDouble(token));
									break;
								case 3:
									pos.setLongitud(Double.parseDouble(token));
									break;
								case 4:
									pos.setPresicion(Float.parseFloat(token));
									break;
								case 5:
									pos.setVelocidad(Float.parseFloat(token));
									break;
								}
							}
							
							gpsInteface.setNotification(pos.getId() + " - " +pos.getFecha(),
									"Lat: " + Double.toString(pos.getLatitud())
									+ "Lon: " + Double.toString(pos.getLongitud())
									+ "Vel: " + Double.toString(pos.getVelocidad())
									+ "Pre: " + Double.toString(pos.getPresicion()));
							gpsInteface.updateLocationSMS(pos);
							abortBroadcast();							
						}
					}
				}
			}
		}
	}

	public static void setGpsInteface(GPSinteface gpsInteface) {
		SMSReceiver.gpsInteface = gpsInteface;
	}
}
