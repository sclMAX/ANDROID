package ar.com.sclmax.indumatics.servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import ar.com.sclmax.indumatics.data.GpsData;


public class SMSReceiver extends BroadcastReceiver {
	
	static String LOCATE = "LOCATE";
	static String RESET = "L9992";
	/** Called when the activity is first created. */
	@Override
	public void onReceive(Context context, Intent intent) {
	    
		Bundle bundle =intent.getExtras();
		String Nroreenvio, body = "";
		SmsMessage [] msgs = null;
		if (bundle != null){
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
				Nroreenvio = msgs[i].getOriginatingAddress().toString();
				body = msgs[i].getMessageBody().toString();
				if(body.contains(LOCATE))
				{
					SendSMS(Nroreenvio, context);
				}
				else
				{
					if(body.contains(RESET))
					{
						GpsData.resetDB();
					}
				}
			}
		}
	}

	public void SendSMS (String nro, Context context){
		String mensaje = GpsData.getLastData(2);
		SmsManager sms = SmsManager.getDefault();
		if(mensaje != null){
			sms.sendTextMessage(nro, null, mensaje, null, null);
		}else {
			sms.sendTextMessage(nro, null, "SIN DATOS... Lanzando el Servicio...", null, null);
			Intent serviceIntent = new Intent(context, GPSService.class);
            context.startService(serviceIntent);
		}
	}	
}

