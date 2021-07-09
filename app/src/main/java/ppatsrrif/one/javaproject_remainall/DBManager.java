package ppatsrrif.one.javaproject_remainall;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

public class DBManager {

    // variable: Context, sqliteDataBase, Class openHelper
    Context context;
    SQLiteDatabase sqLiteDatabase;
    DBOpenHelper dbOpenHelper;


    // get Context and create object DBOpenHelper
    DBManager(Context context) {
        this.context = context;
        dbOpenHelper = new DBOpenHelper(context);
    }


    // method open database
    public void openDB() {
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
    }

    // method for write data in database
    public void writeInDB(String name, String lastName, String email, String sex) {

        // variable who keep data
        ContentValues contentValues = new ContentValues();

        contentValues.put(DataBaseConstants.DATABASE_NAME, name);
        contentValues.put(DataBaseConstants.DATABASE_LAST_NAME, lastName);
        contentValues.put(DataBaseConstants.DATABASE_EMAIL, email);
        contentValues.put(DataBaseConstants.DATABASE_SEX, sex);

        // set variable to database
        sqLiteDatabase.insert(DataBaseConstants.DATABASE_TABLE_NAME, null, contentValues);
    }

    // check data on coincidence
    public boolean checkLogin(String str2, int i) {
        // get array with type data
        ArrayList<String> arrayList = readDB(i);
        Boolean b = true;

        // check on coincidence
        for(String str : arrayList) {
            if(str.equals(str2)) {
                b = false;
                break;
            }
        }

        // get result
        return b;
    }

    // method for read data from database
    public ArrayList<String> readDB(int i) {

        // create arraylist
        ArrayList<String> list = new ArrayList();

        // str type data
        String commandSQL;

        switch (i) {
            default:
            case 1:
                commandSQL = DataBaseConstants.DATABASE_NAME;
                break;
            case 2:
                commandSQL = DataBaseConstants.DATABASE_LAST_NAME;
                break;
            case 3:
                commandSQL = DataBaseConstants.DATABASE_EMAIL;
                break;
            case 4:
                commandSQL = DataBaseConstants.DATABASE_SEX;
                break;

        }

        String[] projection =  { commandSQL };;

        // create cursor with data type
        Cursor cursor = sqLiteDatabase.query(DataBaseConstants.DATABASE_TABLE_NAME, projection,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
             list.add(cursor.getString(cursor.getColumnIndex(commandSQL)));
             // write to arraylist all data
        }
        cursor.close();

        // get list with data
        return list;
    }


    // method for delete data from database
    public void deleteFromDB(String email) {
        // string command
        String selection = DataBaseConstants.DATABASE_EMAIL + " LIKE ?";
        String args[] = { email };

        // set delete from database
        sqLiteDatabase.delete(DataBaseConstants.DATABASE_TABLE_NAME, selection, args);
    }

    // close database
    public void closeDB() {
        sqLiteDatabase.close();
    }



}
