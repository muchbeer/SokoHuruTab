package sokohuru.muchbeer.king.sokohurutab.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;
import sokohuru.muchbeer.king.sokohurutab.loggin.L;

/**
 * Created by muchbeer on 6/24/2015.
 */
public class SokoDatabase {

     SokoHelper2  mHelper;
    private SQLiteDatabase mDatabase;

    public SokoDatabase(Context context) {
        mHelper = new SokoHelper2(context);
        mDatabase=mHelper.getWritableDatabase();

    }

    public void insertSokoOffice(ArrayList<Soko> listSoko,
                                 boolean clearPrevious) {
        if(clearPrevious) {
            deleteAll();

        }

        //create a sql prepared statement
        String sql = "INSERT INTO " + SokoHelper2.tableSokoni +
                " VALUES (?,?,?,?,?,?,?,?,?)";

        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();

        for (int i = 0; i < listSoko.size(); i++ ) {
            Soko currentSoko = listSoko.get(i);
            statement.clearBindings();

            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentSoko.getName());
            statement.bindString(3, currentSoko.getPrice());
       //     statement.bindString(5, currentSoko.getDesc());
            statement.bindString(6, currentSoko.getLocation());
            statement.bindString(7, currentSoko.getContact());
          //  statement.bindString(8, currentSoko.getCreated());
            statement.bindString(9, currentSoko.getUsername());
            statement.bindString(10, currentSoko.getImage());

            //   statement.bindString(5, currentSoko.getGenre());

            L.m("inserting entry " + i);
            statement.execute();
        }

        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Soko> getAllItemFromMarket() {
        ArrayList<Soko> listSokoni = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                SokoHelper2.columnID,
                SokoHelper2.columnNAME,
                SokoHelper2.columnPRICE,
                SokoHelper2.columnDESC,
                SokoHelper2.columnLOC,
                SokoHelper2.columnCONTACT,
                SokoHelper2.columnCREATED,
                SokoHelper2.columnUSER,
                SokoHelper2.getColumnIMAGE

        };
        Cursor cursor =mDatabase.query(SokoHelper2.tableSokoni,
                columns, null,null,null,null,null);

        if(cursor !=null && cursor.moveToFirst()) {
            do {
                //create a new item object and retrieve the data
                //the cursor to be store in the item object

                Soko sokoni = new Soko();

                //each step is a 2 part process, find the index
                //column first
                //find the data of the column using that inded and finally set our blank movie object to contain our data

                sokoni.setName(cursor.getString(cursor.getColumnIndex(SokoHelper2.columnNAME)));
                sokoni.setPrice(cursor.getString(cursor.getColumnIndex(SokoHelper2.columnPRICE)));
                sokoni.setDesc(cursor.getString(cursor.getColumnIndex(SokoHelper2.columnDESC)));
                sokoni.setLocation(cursor.getString(cursor.getColumnIndex(SokoHelper2.columnLOC)));
                sokoni.setContact(cursor.getString(cursor.getColumnIndex(SokoHelper2.columnCONTACT)));
                sokoni.setCreated(cursor.getString(cursor.getColumnIndex(SokoHelper2.columnCREATED)));
                sokoni.setUsername(cursor.getString(cursor.getColumnIndex(SokoHelper2.columnUSER)));
                sokoni.setImage(cursor.getString(cursor.getColumnIndex(SokoHelper2.getColumnIMAGE)));

                L.m("getting movie object " + sokoni);
                listSokoni.add(sokoni);
            } while (cursor.moveToNext());
        }

        return listSokoni;
    }

    public void deleteAll() {
        mDatabase.delete(SokoHelper2.tableSokoni, null, null);
    }

    public class SokoHelper2 extends SQLiteOpenHelper {

public final String TAG = SokoHelper2.class.getSimpleName();

        private Context mContext;
        private static final String dbName = "sokoni";
        private static final int dbVersion = 1;
        public static final String tableSokoni = "sokoniTable";
        public static final String columnID = "pid";
        public static final String columnNAME = "name";
        public static final String columnPRICE = "price";
        public static final String columnDESC = "description";
        public static final String columnLOC = "location";
        public static final String columnCONTACT = "contact";
        public static final String columnCREATED = "created_at";
        public static final String columnUSER = "login_user";
        public static final String getColumnIMAGE="image";



        public SokoHelper2(Context context) {
            super(context, dbName, null, dbVersion);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                String CREATE_TABLE_SOKONI =
                        "CREATE TABLE " + tableSokoni + "(" +
                                columnID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                columnNAME + " TEXT NULL," +
                                columnPRICE + " TEXT NULL," +
                                columnDESC + " TEXT NULL," +
                                columnLOC + " TEXT NULL," +
                                columnCONTACT + " TEXT NULL," +
                                columnCREATED + " TEXT NULL," +
                                columnUSER + " TEXT NULL," +
                                getColumnIMAGE + " TEXT NULL" + ")";

                db.execSQL(CREATE_TABLE_SOKONI);

                L.m("create table sokoni executed");
            }catch (SQLiteException exception) {
                L.T(mContext, exception + "");
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int olderVersion, int newVersion) {


            // if(newVersion>olderVersion)
            //  copyDatabase();

            try {
                L.m("upgrade table sokoni executed");

                db.execSQL(" DROP TABLE IF EXISTS " + tableSokoni );
                onCreate(db);
            }catch (SQLiteException exception) {
                L.T(mContext, exception + "");
            }
        }
    }

}
