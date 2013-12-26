package ar.com.sclmax.pruebamysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainPruebaMySQL extends Activity {
	private static String Host = "200.45.48.233";
    private static String User = "root";
    private static String Password = "root";
    private static String DataBase = "indumatics";
    private static String port = "3306";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_prueba_my_sql);
		
		final TextView txtLog = (TextView)findViewById(R.id.log);
		Button btnSend = (Button)findViewById(R.id.btnSend);
		
		btnSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				txtLog.append("Conectando...");
				try {
					String urlConexionMySQL = "";
			    	urlConexionMySQL = "jdbc:mysql://" + Host + ":" + port+ "/"+DataBase;
			    	txtLog.append("URL: " + urlConexionMySQL);
			    	Class.forName("com.mysql.jdbc.Driver");
					Connection conn = DriverManager.getConnection(urlConexionMySQL,User, Password);
					txtLog.append("\n Creando Statement");
					Statement st = (Statement) conn.createStatement();
					txtLog.append("\n Ejecutando INSERT");
					int r = st.executeUpdate("INSERT INTO gpsdata (idequipo, fecha, latitud, longitud ) VALUES('P1','P1', '0', '0')");
					txtLog.append("\n Se ingresaron " + r + "Registros");
				} catch (SQLException e) {
					txtLog.append("\n Error al conectar ERROR:" + e.getMessage());
				} catch (ClassNotFoundException e) {
					txtLog.append("\n No se encontro la Clase ERROR:" + e.getMessage());
				}				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_prueba_my_sql, menu);
		return true;
	}

}
