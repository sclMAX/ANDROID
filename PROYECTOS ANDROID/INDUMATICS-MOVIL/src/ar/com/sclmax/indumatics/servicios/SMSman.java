package ar.com.sclmax.indumatics.servicios;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SMSman {
	
	public static void DeleteSMS(Context context){
	
	Uri uri = Uri.parse("content://sms/inbox"); // returns all the results from the given 
	Cursor c = context.getContentResolver().query(uri, null, null ,null,null);
	context.getContentResolver().delete(uri, "*", null);
	c.close (); 
}

}
//	do {
//	long threadId = c.getLong(1);
//	System.out.println("threadId:: "+threadId);
//	//   if (threadId == 4){
//	getContentResolver().delete(
//	Uri.parse("content://sms/conversations/" + threadId),
//	null, null);
//	//   }
//	} while (c.moveToNext());