package sokohuru.muchbeer.king.sokohurutab.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import sokohuru.muchbeer.king.sokohurutab.loggin.L;

/**
 * Created by muchbeer on 6/24/2015.
 */
public class SokoHelper extends SQLiteOpenHelper {



    private Context mContext;
    private static final String dbName = "sokoni";
    private static final int dbVersion = 1;
    public static final String tableSokoni = "sokoniTable";
   public static final String columnUID = "_id";
    public static final String columnTITLE = "title";
    public static final String columnIMAGE = "image";
    public static final String columnGENRE = "genre";
    public static final String columnRATING = "rating";
    public static final String columnYEAR = "releasingYear";

    private static final String CREATE_TABLE_SOKONI =
            "CREATE TABLE " + tableSokoni + "(" +
                    columnUID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    columnTITLE + " TEXT, " +
                    columnIMAGE + " TEXT, " +
                    columnRATING + " TEXT " +
                    columnGENRE + " TEXT " +
                    "):";

    public SokoHelper(Context context) {
        super(context, dbName, null, dbVersion);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE_SOKONI);
            L.m("create table sokoni executed");
        }catch (SQLiteException exception) {
            L.t(mContext, exception + "");
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int olderVersion, int newVersion) {


        if(newVersion>olderVersion)
          //  copyDatabase();

        try {
            L.m("upgrade table sokoni executed");

            db.execSQL(" DROP TABLE " + tableSokoni + " IF EXISTS: ");
            onCreate(db);
        }catch (SQLiteException exception) {
            L.t(mContext, exception + "");
        }
    }
}
