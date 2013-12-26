package com.example.webserviceprueba;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PruebaWS extends Activity {

	private EditText txtSalida;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prueba_ws);
		 txtSalida = (EditText) findViewById(R.id.txtResultado);
		Button btnEnviar = (Button) findViewById(R.id.btnSend);

		btnEnviar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				String txtIdEquipo = "Equipo1";
				String txtFecha = "Fecha1";
				String txtLatitud = "LAtitud1";
				String txtLongitud = "Longitud1";
				String txtPrecision = "Precision1";
				String txtVelocidad = "Velocidad1";
				EditText txtDato = (EditText) findViewById(R.id.txtDato);
				txtIdEquipo = txtDato.getText().toString();
				GPSreg r = new GPSreg(txtIdEquipo, txtFecha, txtLatitud, txtLongitud, txtPrecision, txtVelocidad);
				 new consumirWS().execute(r);

				if (isOnline()) {
					

				} else {
					txtSalida.setText("SIN INTERNET");
				}
			}
		});

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = cm.getActiveNetworkInfo();

		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}

		return false;
	}

	private class consumirWS extends AsyncTask<GPSreg , Void, String>{
        final String SOAP_ACTION = "urn:webserv";
        final String METHOD = "add";
        final String NAMESPACE = "urn:webserv";
        final String ENDPOINTWS = "http://www.indumatics.com.ar/WebServiceGPS/WSGPSdata.php";
        String respuesta = null;
 
 
        protected String doInBackground(GPSreg... args)
        {
            SoapObject userRequest = new SoapObject(NAMESPACE, METHOD);
            userRequest.addProperty("idequipo", args[0].getIdequipo());
            userRequest.addProperty("fecha", args[0].getFecha());
            userRequest.addProperty("latitud", args[0].getLatitud());
            userRequest.addProperty("longitud", args[0].getLongitud());
            userRequest.addProperty("precision", args[0].getPrecision());
            userRequest.addProperty("velocidad", args[0].getVelocidad());
 
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(userRequest);
 
            try{
                HttpTransportSE androidHttpTransport = new HttpTransportSE(ENDPOINTWS);
                androidHttpTransport.debug = true;
                androidHttpTransport.call(SOAP_ACTION, envelope);
 
                respuesta = envelope.getResponse().toString();
            }
            catch (Exception e){
                e.printStackTrace();
            }
 
            return respuesta;
        }
 
        protected void onPostExecute(String result)
        {
            txtSalida.setText(result);
 
            super.onPostExecute(String.valueOf(result));
        }
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prueba_w, menu);
		return true;
	}

}
