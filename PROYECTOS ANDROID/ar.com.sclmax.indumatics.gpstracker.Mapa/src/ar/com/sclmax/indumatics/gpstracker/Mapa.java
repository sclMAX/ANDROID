package ar.com.sclmax.indumatics.gpstracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Mapa extends MapActivity implements GPSinteface {

	MapView mapView = null;
	
	final static int icNORMAL = 1;
	final static int icPARADO = 2;
	final static int icRAPIDO = 3;
	final static int icDESPACIO = 4;
	final static String LOCATE = "L9991";
	final static String RESET = "L9992";
	
	private final int REQUEST_CODE_PICK_DIR = 1;
	private final int REQUEST_CODE_PICK_FILE = 2;
	private static String vTel = "+543436209715";
	private ProgressDialog pDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapa);

		final Activity activityForButton = this;

		SMSReceiver.setGpsInteface(this);
		Spinner sprTelefono =(Spinner)findViewById(R.id.sprTelefonos);
		
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.TELEFONOS, android.R.layout.simple_spinner_item);		
		sprTelefono.setAdapter(adapter);		
		sprTelefono.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parentview, View selectedItemView,
					int position, long id) {	
				vTel = parentview.getItemAtPosition(position).toString();
			}
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});		

		mapView = (MapView) findViewById(R.id.mapaview);
		mapView.setBuiltInZoomControls(true);


		Button btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
		Button btnAbrirDB = (Button) findViewById(R.id.btnAbrirFIle);
		Button btnHistorial = (Button)findViewById(R.id.btnHistorial);
		
		final CheckBox chkSatelite = (CheckBox) findViewById(R.id.chkSatelite);

		btnSendSMS.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SmsManager sms = SmsManager.getDefault();
				sms.sendTextMessage(vTel, null,LOCATE, null, null);
			}
		});
		
		btnHistorial.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				LoadHistorial(vTel);
			}
		});

		btnAbrirDB.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent fileExploreIntent = new Intent(
						ar.com.sclmax.indumatics.gpstracker.FileBrowserActivity.INTENT_ACTION_SELECT_FILE,
						null,
						activityForButton,
						ar.com.sclmax.indumatics.gpstracker.FileBrowserActivity.class);
				startActivityForResult(fileExploreIntent,
						REQUEST_CODE_PICK_FILE);
			}
		});

		chkSatelite.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (chkSatelite.isChecked()) {
					mapView.setSatellite(true);
				} else
					mapView.setSatellite(false);
				mapView.invalidate();
			}
		});

		if (chkSatelite.isChecked()) {
			mapView.setSatellite(true);
		} else
			mapView.setSatellite(false);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public void updateLocation(double lat, double lon, String titulo, String info, int icono) {

		List mapOverlays = mapView.getOverlays();
		switch (icono) {
		case icPARADO: icono = R.drawable.ic_parado;			
			break;
		case icRAPIDO: icono = R.drawable.ic_rapido;
			break;
		case icDESPACIO: icono = R.drawable.ic_despacio;

		default: icono = R.drawable.ic_normal;
			break;
		}
		Drawable drawable = this.getResources().getDrawable(icono);
		MyOverlay itemizedOverlay = new MyOverlay(drawable, this);

		GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, titulo, info);

		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);

		MapController mapController = mapView.getController();

		mapController.animateTo(point);
		mapController.setZoom(mapView.getZoomLevel());
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_PICK_DIR) {
			if (resultCode == this.RESULT_OK) {
				String newDir = data
						.getStringExtra(FileBrowserActivity.returnDirectoryParameter);
				Toast.makeText(
						this,
						"Received DIRECTORY path from file browser:\n" + newDir,
						Toast.LENGTH_LONG).show();

			} else {// if(resultCode == this.RESULT_OK) {
				Toast.makeText(this, "Received NO result from file browser",
						Toast.LENGTH_LONG).show();
			}// END } else {//if(resultCode == this.RESULT_OK) {
		}// if (requestCode == REQUEST_CODE_PICK_DIR) {

		if (requestCode == REQUEST_CODE_PICK_FILE) {
			if (resultCode == this.RESULT_OK) {
				String newFile = data
						.getStringExtra(FileBrowserActivity.returnFileParameter);
				Toast.makeText(this,
						"Received FILE path from file browser:\n" + newFile,
						Toast.LENGTH_LONG).show();
				LoadDB(newFile);

			} else {// if(resultCode == this.RESULT_OK) {
				Toast.makeText(this, "Received NO result from file browser",
						Toast.LENGTH_LONG).show();
			}// END } else {//if(resultCode == this.RESULT_OK) {
		}// if (requestCode == REQUEST_CODE_PICK_FILE) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void LoadDB(String file) {
		
		final int rdespacio = 10;
		final int rrapido = 20;
		final int rnormal =20;
		final int rparado = 5;
		
		new GpsData(this, file);
		Cursor reg = GpsData.getAllReg("gpsdata");
		boolean mostrar = true;
		int count =0;
		int b = 0;
		int fd = rdespacio;
		int fn = rnormal;
		int fr = rrapido ;
		int fp = rparado;
		
		while ((reg.moveToNext())) {
			String titulo = reg.getString(1);
			double vel = reg.getInt(5);
			int icono = icNORMAL;
			
			double lat = Double.parseDouble(reg.getString(2));
			double lon = Double.parseDouble(reg.getString(3));
			String info = "PRECISION: " + reg.getString(4) + "\n"
					+ "VELOCIDAD: " + reg.getString(5);
			if(vel <= 5) icono = icPARADO; 
			if(vel > 90) icono = icRAPIDO;
			if((vel < 50)&&(vel > 5)) icono = icDESPACIO;	
			
			if((vel < 10) ){
				if(fp>=rparado){
					mostrar = true;
					fp = 0;
				}fp++;
			}
			
			if((vel >= 10)&&(vel<50) ){
				if(fd>=rdespacio){
					mostrar = true;
					fd = 0;
				}fd++;
			}
			
			if((vel >= 50)&&(vel<80) ){
				if(fn>=rnormal){
					mostrar = true;
					fn = 0;
				}else fn++;
			}
			
			if((vel >= 80)){
				if(fr>=rrapido){
					mostrar = true;
					fr = 0;
				}else fr++;
			}
			
			
			
			if(mostrar){
					updateLocation(lat, lon, titulo, info, icono);
					count++;
					mostrar = false;
				}
			b++;
		}
		Toast.makeText(getBaseContext(), "Se Cargaron " + count + "registros", Toast.LENGTH_LONG).show();
		this.setTitle("Se cargaron " + count + " registros de " + reg.getCount());
	}
	
	public void LoadHistorial(String tel){
		new GpsData(this);
		Cursor reg = GpsData.getHistorial("sms", tel);
		while (reg.moveToNext()) {
			String titulo = reg.getString(5);
			double lat = Double.parseDouble(reg.getString(3));
			double lon = Double.parseDouble(reg.getString(4));
			String info = reg.getString(6);
			updateLocation(lat, lon, titulo, info, icNORMAL);
		}
	}

	public void updateLocationSMS(String tel, double lat, double lon, String titulo,
			String info) {
		new GpsData(this);
		GpsData.GuardarHistorial(getFecha(), tel, Double.toString(lat), Double.toString(lon), titulo, info);
		this.updateLocation(lat, lon, titulo, info, icNORMAL);		
	}
	
	private String getFecha(){
		Date t = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
		return sdf.format(t).toString();
	}

}