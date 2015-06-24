package sokohuru.muchbeer.king.sokohurutab.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;
import sokohuru.muchbeer.king.sokohurutab.loggin.L;

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

            L.m("inserting entry " + i);
        }

        //set the transaction as successful and end the transaction
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public ArrayList<Soko> getAllItemFromMarket() {
        ArrayList<Soko> listSokoni = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {
                SokoHelper.columnUID,
                SokoHelper.columnTITLE,
                SokoHelper.columnIMAGE,
                SokoHelper.columnGENRE,
                SokoHelper.columnRATING

        };
        Cursor cursor =mDatabase.query(SokoHelper.tableSokoni,
                columns, null,null,null,null,null);

        if(cursor !=null && cursor.moveToFirst()) {
            do {
                //create a new item object and retrieve the data
                //the cursor to be store in the item object

                Soko sokoni = new Soko();

                //each step is a 2 part process, find the index
                //column first
                //find the data of the column using that inded and finally set our blank movie object to contain our data

                sokoni.setTitle(cursor.getString(cursor.getColumnIndex(SokoHelper.columnTITLE)));
                sokoni.setImage(cursor.getString(cursor.getColumnIndex(SokoHelper.columnIMAGE)));
                sokoni.setGenre(cursor.getString(cursor.getColumnIndex(SokoHelper.columnGENRE)));
                sokoni.setRating(cursor.getString(cursor.getColumnIndex(SokoHelper.columnRATING)));

                L.m("getting movie object " + sokoni);
                listSokoni.add(sokoni);
            } while (cursor.moveToNext());
        }

        return listSokoni;
    }

    public void deleteAll() {
        mDatabase.delete(SokoHelper.tableSokoni, null, null);
    }


}
