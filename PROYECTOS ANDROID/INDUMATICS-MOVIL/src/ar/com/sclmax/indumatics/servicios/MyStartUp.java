package ar.com.sclmax.indumatics.servicios;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyStartUp extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
        	try {
				Thread.sleep(3000);
				Intent serviceIntent = new Intent(context, GPSService.class);
	            context.startService(serviceIntent);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }

}
