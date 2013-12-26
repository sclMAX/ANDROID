package ar.com.sclmax.indumatics;

import ar.com.sclmax.indumatics.data.GpsData;
import ar.com.sclmax.indumatics.servicios.GPSService;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;

public class IndumaticsMain extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indumatics_main);
		IniciaServicio();
		Button btnEnviar = (Button) findViewById(R.id.btnEnviar);
		btnEnviar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String dato = GpsData.SendToServer();
				try {
					addWS ws = new addWS();
					Boolean resultado = ws.execute(dato).get();
					if (resultado) {
						//setEnviado(rs.getString(0));
						Toast.makeText(getApplicationContext(), "Envio OK", Toast.LENGTH_LONG).show();
					}else
					{
						Toast.makeText(getApplicationContext(), "ERROR DE ENVIO", Toast.LENGTH_LONG).show();
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(), "ERROR: " + ex.getMessage(), Toast.LENGTH_LONG).show();
					ex.printStackTrace();
				}
			}
		});
	}

	public void IniciaServicio() {
		Intent it = new Intent(this, GPSService.class);
		startService(it);
	}

	public boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("ar.com.sclmax.indumatics.servicios.GPSService"
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
