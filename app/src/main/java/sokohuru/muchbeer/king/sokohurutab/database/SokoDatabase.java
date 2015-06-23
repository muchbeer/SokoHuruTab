package sokohuru.muchbeer.king.sokohurutab.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;

/**
 * Created by muchbeer on 6/24/2015.
 */
public class SokoDatabase {

    private SokoHelper  mHelper;
    private SQLiteDatabase mDatabase;

    public SokoDatabase(Context context) {
        mHelper = new SokoHelper(context);
        mDatabase=mHelper.getWritableDatabase();

    }

    public void insertSokoOffice(ArrayList<Soko> listSoko,
                                 boolean clearPrevious) {
        if(clearPrevious) {
            deleteAll();

        }

        //create a sql prepared statement
        String sql = "INSERT INTO " + SokoHelper.tableSokoni +
                " VALUES (?,?,?,?,?):";

        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();

        for (int i = 0; i < listSoko.size(); i++ ) {
            Soko currentSoko = listSoko.get(i);
            statement.clearBindings();

            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentSoko.getTitle());
            statement.bindString(3, currentSoko.getImage());
            statement.bindString(4, currentSoko.getRating());
            statement.bindString(5, currentSoko.getGenre());
        }
    }
}
