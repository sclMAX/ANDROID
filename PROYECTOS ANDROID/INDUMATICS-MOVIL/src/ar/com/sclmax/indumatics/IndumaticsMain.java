package ar.com.sclmax.indumatics;

import ar.com.sclmax.indumatics.servicios.GPSService;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;


public class IndumaticsMain extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indumatics_main);
		IniciaServicio();	
	}
	
	public void IniciaServicio(){
		Intent it = new Intent(this, GPSService.class);
		startService(it);	
	}
	



}
