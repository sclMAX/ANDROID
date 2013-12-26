package ar.com.sclmax.gpstrackerapi2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class IndumaticsDB extends SQLiteOpenHelper {
	
	private String vsql;

	public IndumaticsDB(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		vsql = "CREATE TABLE gpsdata (" +
				"id INTEGER PRIMARY KEY, " +
				"fecha VARCHAR(20), " +
				"latitud VARCHAR(10), " +
				"longitud VARCHAR(10), " +
				"precision VARCHAR(10), " +
				"velocidad VARCHAR(10));";
		db.execSQL(vsql);
		vsql = "CREATE TABLE cliente (" +
				"[id] INTEGER  PRIMARY KEY, " +
				"[idcliente] INTEGER  UNIQUE NOT NULL, " +
				"[nombre] VARCHAR(50)  UNIQUE NOT NULL, " +
				"[saldo] FLOAT  NULL, " +
				"[entregado] BOOLEAN  NULL, " +
				"[latitud] FLOAT  NULL, " +
				"[longitud] FLOAT  NULL);";
		db.execSQL(vsql);
		
		vsql =  "CREATE TABLE equipos (" +
				"[id] INTEGER  PRIMARY KEY, " +
				"[idequipo] VARCHAR(20)  UNIQUE NOT NULL, " +
				"[nombre] VARCHAR(50)  UNIQUE NOT NULL, " +
				"[color] FLOAT NULL);";
		db.execSQL(vsql);
		
		vsql = "CREATE TABLE sms (" +
				"id INTEGER PRIMARY KEY, " +
				"fecha VARCHAR(20), " +
				"telefono VARCHAR(20), " +
				"latitud VARCHAR(10), " +
				"longitud VARCHAR(10), " +
				"precision VARCHAR(10), " +
				"velocidad VARCHAR(10));";
		db.execSQL(vsql);
		
		vsql = "CREATE TABLE waypoint ("
				+ "id INTEGER PRIMARY KEY, "
				+ "nombre VARCHAR(20), "
				+ "latitud FLOAT NULL, "
				+ "longitud FLOAT NULL);";
		db.execSQL(vsql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		vsql = "DROP TABLE IF EXCISTS gpsdata;";
		db.execSQL(vsql);
		vsql = "DROP TABLE IF EXCISTS cliente;";
		db.execSQL(vsql);
		vsql = "DROP TABLE IF EXCISTS equipos;";
		db.execSQL(vsql);
		vsql = "DROP TABLE IF EXCISTS sms;";
		db.execSQL(vsql);
		vsql = "DROP TABLE IF EXCISTS waypoint;";
		db.execSQL(vsql);
	}

}
