package ar.com.sclmax.gpstrackerapi2;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import ar.com.sclmax.gpstrackerapi2.data.CamionPos;
import ar.com.sclmax.gpstrackerapi2.data.Utils;
import ar.com.sclmax.gpstrackerapi2.data.WayPoint;
import ar.com.sclmax.gpstrackerapi2.services.GPSinteface;
import ar.com.sclmax.gpstrackerapi2.services.GpsData;
import ar.com.sclmax.gpstrackerapi2.services.SMSReceiver;

public class GPSTracker extends Activity implements GPSinteface {

	private GoogleMap mapa;
	final static String vTel1 = "+543436209715";
	final static String vTel2 = "+543436207805";
	final static String vTel3 = "+543434702490";
	private boolean medirEnabled = false;
	private LatLng medirPos1 = null;
	private MenuItem menu_medir;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa))
				.getMap();
		mapa.setMyLocationEnabled(true);
		SMSReceiver.setGpsInteface(this);

		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				if (medirEnabled) {
					if (medirPos1 == null) {
						medirPos1 = marker.getPosition();
					} else {
						MedirDistancia(marker.getPosition());
					}
				}
				return false;
			}
		});

		mapa.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng clickpos) {
				if (medirEnabled) {
					if (medirPos1 == null) {
						medirPos1 = clickpos;
					} else {
						MedirDistancia(clickpos);
					}
				}
			}
		});

		mapa.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng arg0) {
				setWayPoint(arg0);
			}
		});
	}

	public void MedirDistancia(LatLng pos2) {
		if (medirPos1 != null) {
			float distancia = 0;
			distancia = Utils.calcularDistancia(medirPos1.longitude,
					medirPos1.latitude, pos2.longitude, pos2.latitude);
			distancia = distancia / 1000;
			Utils.showMsg(this, "Distamcia Medida", distancia + " Km.", "OK");
			medirPos1 = null;
			medirEnabled = false;
			menu_medir.setChecked(medirEnabled);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gpstracker, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_mapa_normal:
			mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			return true;
		case R.id.menu_mapa_hybrid:
			mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			return true;
		case R.id.menu_mapa_sattelite:
			mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			return true;
		case R.id.menu_mapa_terrain:
			mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			return true;
		case R.id.menu_mapa_none:
			mapa.setMapType(GoogleMap.MAP_TYPE_NONE);
			return true;
		case R.id.cel1:
			sendSMS(vTel1, Utils.LOCATE);
			return true;
		case R.id.cel2:
			sendSMS(vTel2, Utils.LOCATE);
			return true;
		case R.id.cel3:
			sendSMS(vTel3, Utils.LOCATE);
			return true;
		case R.id.cel11:
			getFechaHistorial(vTel1);
			return true;
		case R.id.cel22:
			getFechaHistorial(vTel2);
			return true;
		case R.id.cel33:
			getFechaHistorial(vTel3);
			return true;
		case R.id.medir:
			menu_medir = item;
			medirEnabled = !item.isChecked();
			menu_medir.setChecked(medirEnabled);
			return true;
		case R.id.clear:
			mapa.clear();
			return true;
		case R.id.WayPoint:
			item.setChecked(!item.isChecked());
			if (item.isChecked()) {
				verWayPoint();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void sendSMS(String nro, String msg) {
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(nro, null, msg, null, null);
	}

	public void LoadHistorial(String tel, String fecha) {
		new GpsData(this);
		Cursor reg = GpsData.getHistorial("sms", tel, fecha);
		if (reg.getCount() > 0) {
			while (reg.moveToNext()) {
				CamionPos pos = new CamionPos();
				pos.setFecha(reg.getString(reg.getColumnIndexOrThrow("fecha")));
				pos.setId(reg.getString(reg.getColumnIndexOrThrow("telefono")));
				pos.setLatitud(Double.parseDouble(reg.getString(reg
						.getColumnIndexOrThrow("latitud"))));
				pos.setLongitud(Double.parseDouble(reg.getString(reg
						.getColumnIndexOrThrow("longitud"))));
				pos.setPresicion(Float.parseFloat(reg.getString(reg
						.getColumnIndexOrThrow("precision"))));
				pos.setVelocidad(Float.parseFloat(reg.getString(reg
						.getColumnIndexOrThrow("velocidad"))));
				updateLocation(pos);
			}
		} else {
			Toast.makeText(this,
					"No hay Registro para la fecha elegida!\n" + fecha,
					Toast.LENGTH_LONG).show();
		}
	}

	public void drawWayPoint(WayPoint w) {
		LatLng l = new LatLng(w.getLatitud(), w.getLongitud());
		MarkerOptions mk = new MarkerOptions();
		// Seteo las propiedades del Marker.
		mk.position(l);
		mk.title(w.getNombre());
		mk.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		// Seteo las propiedades de la camara.
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(l, 15);

		mapa.addMarker(mk); // Agrego el Marker al mapa.
		mapa.animateCamera(cu); // Muevo la camara al Marker.
	}

	public void verWayPoint() {
		new GpsData(this);
		Cursor cr = GpsData.getAllReg("waypoint");
		if (cr.getCount() > 0) {
			while (cr.moveToNext()) {
				WayPoint w = new WayPoint(cr.getString(cr
						.getColumnIndexOrThrow("nombre")), cr.getDouble(cr
						.getColumnIndexOrThrow("latitud")), cr.getDouble(cr
						.getColumnIndexOrThrow("longitud")));
				drawWayPoint(w);
			}
		} else {
			Toast.makeText(this, "No se encontro ninguna referencia guardada!", Toast.LENGTH_LONG).show();
		}
	}

	public void setMarker(CamionPos pos) {
		LatLng latlon = new LatLng(pos.getLatitud(), pos.getLongitud());
		MarkerOptions mk = new MarkerOptions();
		CircleOptions pre = new CircleOptions();
		int color = Color.GREEN;

		// Seteo las propiedades del Marker.
		mk.position(latlon);
		mk.title(pos.getId() + " - " + pos.getFecha());
		String snippet = "";
		snippet += "Lat: " + pos.getLatitud() + "\n";
		snippet += "Lon: " + pos.getLongitud() + "\n";
		snippet += "Pre: " + pos.getPresicion() + "\n";
		snippet += "Vel: " + pos.getVelocidad() + "\n";
		mk.snippet(snippet);
		mk.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		if (pos.getId().equals(vTel1)) {
			mk.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
		}
		if (pos.getId().equals(vTel2)) {
			mk.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		}
		if (pos.getId().equals(vTel3)) {
			mk.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		}
		// Seteo las propiedades del circulo que marca la precision.
		if (pos.getVelocidad() > 90) {
			color = Color.RED;
		}
		if (pos.getVelocidad() < 10) {
			color = Color.CYAN;
		}
		pre.center(latlon);
		pre.strokeColor(color);
		pre.radius(pos.getPresicion());
		pre.strokeWidth(5);

		// Seteo las propiedades de la camara.
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(latlon, 15);

		mapa.addMarker(mk); // Agrego el Marker al mapa.
		mapa.addCircle(pre); // Agrego el circulo de precision.
		mapa.animateCamera(cu); // Muevo la camara al Marker.
	}

	public void updateLocation(CamionPos pos) {
		setMarker(pos);
	}

	public void updateLocationSMS(CamionPos pos) {
		new GpsData(getBaseContext());
		GpsData.GuardarHistorial(pos);
		setMarker(pos);
	}

	private void setWayPoint(final LatLng pos) {
		new GpsData(this);
		AlertDialog.Builder dlg = new AlertDialog.Builder(this);
		dlg.setTitle("Guardar Referncia...");
		dlg.setMessage("Ingrese el nombre para la referencia");
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setText("WayPoint"
				+ Integer.toString(GpsData.getLastWayPointId() + 1));
		dlg.setView(input);
		dlg.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!input.equals("")) {
					WayPoint w = new WayPoint(input.getText().toString(),
							pos.latitude, pos.longitude);
					GpsData.setWayPoint(w);
				}
			}
		});
		dlg.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		dlg.show();
	}

	private void getFechaHistorial(final String tel) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Opciones de Busqueda");
		alert.setMessage("Ingrese la fecha (Ej. 2013/01/30)");
		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
		input.setText(Utils.getFechaCorta());

		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if (value.equals("")) {
					LoadHistorial(tel, "%");
				} else {
					LoadHistorial(tel, value);
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	@Override
	public void setNotification(CharSequence titulo, CharSequence msg) {
		Context context = getBaseContext();
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(titulo).setContentText(msg)
				.setAutoCancel(true);
		Intent resultIntent = new Intent(context, GPSTracker.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(GPSTracker.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());

	}

}
