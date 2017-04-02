package in.rashtriyakaryadala.potholedetectionandlogging;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

/**
 * Created by Krishna on 26-02-2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    //Definition of database name, version table and column names
    public static final String TABLE_NAME = "location_info";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LONG = "longitude";
    public static final String COLUMN_NUM = "number";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "location.db";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+"("+
                COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COLUMN_LAT+ " REAL, "+
                COLUMN_LONG+" REAL, "+
                COLUMN_NUM+ " INTEGER "+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void addLocation(Location location){
        SQLiteDatabase db = getWritableDatabase();
        double longi = Math.round(location.getLongitude()*1000.0)/1000.0;
        double lati = Math.round(location.getLatitude()*1000.0)/1000.0;
        String q1 = "SELECT * FROM " +TABLE_NAME+ " WHERE " + COLUMN_LONG+ "=" + Double.toString(longi) + " AND "+ COLUMN_LAT+"=" + Double.toString(lati) + ";";
        Log.i("Query",q1);
        Cursor c = db.rawQuery(q1,null);
        String q2;
        if(c.moveToFirst()){
            q2 = "UPDATE "+TABLE_NAME+ " SET "+ COLUMN_NUM+"="+COLUMN_NUM+"+1" + " WHERE "  + COLUMN_LONG+ "=" + Double.toString(longi) + " AND "+ COLUMN_LAT+"=" + Double.toString(lati) + ";";
        }
        else{
            q2 = "INSERT INTO "+ TABLE_NAME +" VALUES(NULL,"+ Double.toString(lati)+ ","+ Double.toString(longi) + ",1);";
        }
        Log.i("Query",q2);
        db.execSQL(q2);
        db.close();
    }

    public void deleteTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public String printDataBase(){
        SQLiteDatabase db = getWritableDatabase();
        StringBuffer buffer = new StringBuffer();
        String q4 = "SELECT * FROM "+ TABLE_NAME + ";";
        Cursor c = db.rawQuery(q4, null);
        while(c.moveToNext()){
                buffer.append(c.getInt(0)+" "+c.getDouble(1)+" "+c.getDouble(2)+ " "+c.getDouble(3)+"\n");
        }
        db.close();
        String s1 = buffer.toString();
        Log.i("Query",s1);
        return s1;

    }


}
