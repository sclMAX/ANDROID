package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	public DataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String vSQL = "";
		vSQL = "CREATE TABLE clientes (" +
				"id INTEGER PRIMARY KEY, " +
				"telefono VARCHAR(20) NULL, " +
				"nombre VARCHAR(50) NULL, " +
				"fuc VARCHAR(20) NULL; ";
		db.execSQL(vSQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String vSQL = "DROP TABLE IF EXCISTS clientes;";
		db.execSQL(vSQL);
	}

}
