package ar.com.sclmax.gpstrackerapi2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GPSTrackerMain extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gpstracker_main, menu);
		return true;
	}

}
