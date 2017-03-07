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

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "location_db";
    public static final String TABLE_LOCA = "location_info";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LAT = "latitude";
    public static final String COLUMN_LONG = "longitude";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE"+TABLE_LOCA+"("+
                COLUMN_ID+ " INTERGER PRIMARY KEY AUTOINCREMENT "+
                COLUMN_LAT+ " REAL "+
                COLUMN_LONG+" REAL "+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_LOCA);
        onCreate(db);
    }

    public void addLocation(Location location){
        ContentValues values = new ContentValues();
        values.put(COLUMN_LAT, location.getLatitude());
        values.put(COLUMN_LONG, location.getLongitude());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_LOCA,null,values);
        db.close();
    }


}
