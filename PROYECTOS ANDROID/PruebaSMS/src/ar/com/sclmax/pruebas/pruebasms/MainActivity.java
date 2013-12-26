package ar.com.sclmax.pruebas.pruebasms;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView view = new TextView(this);
		view.setScrollBarStyle(BIND_AUTO_CREATE);
		Uri uriSMSURI = Uri.parse("content://sms/inbox");
		Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,
				null);
		String sms = "";
		
		while (cur.moveToNext()) {
			//sms += cur.getColumnName(1) +" : "+ cur.getString(1)+ "From :" + cur.getString(2) + " : " + cur.getString(11)+ "\n";
			for(int i=0;i < cur.getColumnCount();i++){
				sms += cur.getColumnName(i) + " : "+ cur.getString(i) +"\n";
			}
		}
		view.setText(sms);
		setContentView(view);

	}
}
