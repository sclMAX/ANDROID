package ar.com.sclmax.indumatics.data;

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
				"fecha VARCHAR(20) NULL, " +
				"latitud VARCHAR(10) NULL, " +
				"longitud VARCHAR(10) NULL, " +
				"precision VARCHAR(10) NULL, " +
				"velocidad VARCHAR(10) NULL," +
				"enviado VARCHAR(1) NULL);";
		db.execSQL(vsql);
		vsql = "CREATE TABLE alertas (" +
				"id INTEGER PRIMARY KEY, " +
				"fecha VARCHAR(20) NULL, " +
				"latitud VARCHAR(10) NULL, " +
				"longitud VARCHAR(10) NULL, " +
				"precision VARCHAR(10) NULL, " +
				"velocidad VARCHAR(10) NULL," +
				"enviado VARCHAR(1) NULL);";
		db.execSQL(vsql);
		
		vsql = "CREATE TABLE [cliente] (" +
				"[id] INTEGER  PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				"[idcliente] INTEGER  UNIQUE NOT NULL, " +
				"[nombre] VARCHAR(50)  UNIQUE NOT NULL, " +
				"[saldo] FLOAT  NULL, " +
				"[entregado] BOOLEAN  NULL, " +
				"[zona] INTEGER  NULL, " +
				"[latitud] FLOAT  NULL, " +
				"[longitud] FLOAT  NULL)";
		db.execSQL(vsql);
		vsql = "CREATE TABLE log (" +
				"id INTEGER PRIMARY KEY, " +
				"fecha VARCHAR(20), " +
				"log TEXT)" ;
		db.execSQL(vsql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		vsql = "DROP TABLE IF EXCISTS gpsdata;";
		db.execSQL(vsql);
		vsql = "DROP TABLE IF EXCISTS alertas;";
		db.execSQL(vsql);
		vsql = "DROP TABLE IF EXCISTS cliente;";
		db.execSQL(vsql);
		vsql = "DROP TABLE IF EXCISTS log;";
		db.execSQL(vsql);
	}

}
