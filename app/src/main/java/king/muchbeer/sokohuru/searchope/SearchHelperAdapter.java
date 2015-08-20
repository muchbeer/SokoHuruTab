package king.muchbeer.sokohuru.searchope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

import king.muchbeer.sokohuru.loggin.L;

/**
 * Created by muchbeer on 8/17/2015.
 */

public class SearchHelperAdapter {

    private static final String TAG = "Sokoni SearchHelper";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;


    public long insertProduct(String itemName) {

        mDb = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDbHelper.COLUMN_NAME, itemName);
        long id = mDb.insert(mDbHelper.FTS_VIRTUAL_TABLE, null, contentValues);
        return id;
    }

    public String getAllData() {
        mDb = mDbHelper.getWritableDatabase();

        String[] columns  = {
                mDbHelper.COLUMN_NAME
        };
        Cursor cursor = mDb.query(mDbHelper.FTS_VIRTUAL_TABLE, columns,
                null, null, null, null, null);

        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()) {
           String nameItem = cursor.getString(0);
            buffer.append(nameItem + "\n");
        }
        return buffer.toString();
    }

    public long createList(String name) {

        ContentValues initialValues = new ContentValues();


        initialValues.put(mDbHelper.COLUMN_NAME, name);

        return mDb.insert(mDbHelper.FTS_VIRTUAL_TABLE, null, initialValues);

    }


    public Cursor searchByInputText(String inputText) throws SQLException {

        String query = "SELECT docid as _id," +
                mDbHelper.COLUMN_NAME +  " from " + mDbHelper.FTS_VIRTUAL_TABLE +
                " where " + mDbHelper.COLUMN_NAME + " MATCH '" + inputText + "';";

        Cursor mCursor = mDb.rawQuery(query,null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }


    public boolean deleteAllNames() {

        int doneDelete = mDb.delete(mDbHelper.FTS_VIRTUAL_TABLE, null, null);
        return doneDelete > 0;

    }

    public Cursor getAllProduct() {
        String  name = null;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] columns = {
                mDbHelper.COLUMN_ID, mDbHelper.COLUMN_NAME
        };

        Cursor cursor = db.query(mDbHelper.FTS_VIRTUAL_TABLE, columns,
                null, null, null, null, null);

        if (cursor !=null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    private final Context context;

     static class DatabaseHelper extends SQLiteOpenHelper {
         public static final String COLUMN_NAME = "name";
         public static final String COLUMN_ID = "_id";

         private static final String DATABASE_NAME = "Sokoni";
         private static final String FTS_VIRTUAL_TABLE = "popular";
         private static final int DATABASE_VERSION = 2;

         //Create a FTS3 Virtual Table for fast searches
         private static final String DATABASE_CREATE =
                 "CREATE TABLE " + FTS_VIRTUAL_TABLE + " ("
                         + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                         + COLUMN_NAME +" VARCHAR (122));";

         private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
                 + FTS_VIRTUAL_TABLE;


         private Context context;
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
            L.T(context, "Constructor table is called");
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

                db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
                onCreate(db);

        }
    }

    public SearchHelperAdapter(Context ctx) {
        this.context = ctx;
    }

    public SearchHelperAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }




    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


}