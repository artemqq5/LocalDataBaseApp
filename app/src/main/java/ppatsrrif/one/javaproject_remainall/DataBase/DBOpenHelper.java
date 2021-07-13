package ppatsrrif.one.javaproject_remainall.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import ppatsrrif.one.javaproject_remainall.DataBase.DataBaseConstants;


public class DBOpenHelper extends SQLiteOpenHelper {

    // set name and version database
    public static final String DATABASE_NAME = "people_list";
    public static final int DATABASE_VERSION = 7;


    // string command to create datatable
    private static final String CREATE_TABLE = "CREATE TABLE " + DataBaseConstants.DATABASE_TABLE_NAME +
            " (" + DataBaseConstants._ID + " INTEGER PRIMARY KEY," + DataBaseConstants.DATABASE_NAME + " TEXT," +
            DataBaseConstants.DATABASE_LAST_NAME + " TEXT," + DataBaseConstants.DATABASE_EMAIL + " TEXT," +
            DataBaseConstants.DATABASE_GENDER + " TEXT);";

    // string command to delete datatable
    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + DataBaseConstants.DATABASE_TABLE_NAME + ";";

    // constructor DBOpenHelper
    public DBOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    // create datatable
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    // update database when version lift
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }
}
