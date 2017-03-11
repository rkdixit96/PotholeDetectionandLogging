package in.rashtriyakaryadala.potholedetectionandlogging;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

/**
 * Created by Krishna on 26-02-2017.
 */

public class DBHandler extends SQLiteOpenHelper {


    public static final String TABLE_NAME = "location_info";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LONG = "longitude";
    public static final String COLUMN_NUM = "number";
    //Definition of database name, version and column names
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "location.db";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE"+TABLE_NAME+"("+
                COLUMN_ID+ " INTERGER PRIMARY KEY AUTOINCREMENT "+
                COLUMN_LAT+ " REAL "+
                COLUMN_LONG+" REAL "+
                COLUMN_NUM+ " INTEGER "+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_NAME);
        onCreate(db);
    }

    public void addLocation(Location location){
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("IF EXISTS (SELECT "+COLUMN_ID+" FROM " +TABLE_NAME+ " WHERE " + COLUMN_LONG+ "=" + Double.toString(location.getLongitude()) + " AND "+ COLUMN_LAT+"=" + Double.toString(location.getLatitude()) + ") THEN " +
                        "UPDATE "+TABLE_NAME+ " SET "+ COLUMN_NUM+"="+COLUMN_NUM+"+1" + " WHERE "  + COLUMN_LONG+ "=" + Double.toString(location.getLongitude()) + " AND "+ COLUMN_LAT+"=" + Double.toString(location.getLatitude())+
                    " ELSE" +
                        "INSERT INTO "+ TABLE_NAME +"VALUES(NULL,"+ Double.toString(location.getLatitude())+ ","+ Double.toString(location.getLongitude()) + ",1");
        db.insert(TABLE_NAME,null,values);
        db.close();
    }


}
