package ar.com.sclmax.pruebas.wsprueba;

import java.util.StringTokenizer;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

@SuppressLint("NewApi")
public class MainWS extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_ws);
		Button btnEnvia = (Button)findViewById(R.id.btnEnviar);
		btnEnvia.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {
				    public void run() {
				    	final String SOAP_ACTION = "urn:webserv";
						final String METHOD = "add";
						final String NAMESPACE = "urn:webserv";
						final String ENDPOINTWS = "http://www.indumatics.com.ar/WebServiceGPS/WSGPSdata.php";
						String respuesta = null;
						try {
							SoapObject ur = new SoapObject(NAMESPACE, METHOD);
							StringTokenizer st = new StringTokenizer(SendToServer(),
									"|");
							int i = 0;
							while (st.hasMoreTokens()) {
								String token = st.nextToken();
								switch (i) {
								case 0:
									ur.addProperty("idequipo", token);
									break;
								case 1:
									ur.addProperty("fecha", token);
									break;
								case 2:
									ur.addProperty("latitud", token);
									break;
								case 3:
									ur.addProperty("longitud", token);
									break;
								case 4:
									ur.addProperty("precision", token);
									break;
								case 5:
									ur.addProperty("velocidad", token);
									break;
								}
								i++;
							}
							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
									SoapEnvelope.VER11);
							envelope.setOutputSoapObject(ur);
							HttpTransportSE androidHttpTransport = new HttpTransportSE(
									ENDPOINTWS);
							// androidHttpTransport.debug = true;
							androidHttpTransport.call(SOAP_ACTION, envelope);

							respuesta = envelope.getResponse().toString();
							if (respuesta.equals("OK")) {
								
							} else {
								
							}

						} catch (Exception e) {
						
						}
						
				    }
				}).start();
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_w, menu);
		return true;
	}

	public String SendToServer() {
		String r = "";
		r += "+543434702490|";
		r += "fecha" + "|";
		r += "latitud" + "|";
		r += "longitud" + "|";
		r += "precision" + "|";
		r += "velocidad" + "|";
		return r;
	}

	public class addWS extends AsyncTask<String, String, Boolean> {
		final String SOAP_ACTION = "urn:webserv";
		final String METHOD = "add";
		final String NAMESPACE = "urn:webserv";
		final String ENDPOINTWS = "http://www.indumatics.com.ar/WebServiceGPS/WSGPSdata.php";
		String respuesta = null;

		protected Boolean doInBackground(String... params) {
			try {
				SoapObject ur = new SoapObject(NAMESPACE, METHOD);
				StringTokenizer st = new StringTokenizer(params[0].toString(),
						"|");
				int i = 0;
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					switch (i) {
					case 0:
						ur.addProperty("idequipo", token);
						break;
					case 1:
						ur.addProperty("fecha", token);
						break;
					case 2:
						ur.addProperty("latitud", token);
						break;
					case 3:
						ur.addProperty("longitud", token);
						break;
					case 4:
						ur.addProperty("precision", token);
						break;
					case 5:
						ur.addProperty("velocidad", token);
						break;
					}
					i++;
				}
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.setOutputSoapObject(ur);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						ENDPOINTWS);
				// androidHttpTransport.debug = true;
				androidHttpTransport.call(SOAP_ACTION, envelope);

				respuesta = envelope.getResponse().toString();
				if (respuesta.equals("OK")) {
					return true;
				} else {
					return false;
				}

			} catch (Exception e) {
				return false;
			}
		}

	}

}
