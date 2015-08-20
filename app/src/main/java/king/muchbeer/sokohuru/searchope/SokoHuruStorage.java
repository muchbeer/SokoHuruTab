package king.muchbeer.sokohuru.searchope;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;

import king.muchbeer.sokohuru.R;
import king.muchbeer.sokohuru.Sokoni.MyApplication;
import king.muchbeer.sokohuru.database.SokoDatabase;
import king.muchbeer.sokohuru.loggin.L;

/**
 * Created by muchbeer on 8/19/2015.
 */

public class SokoHuruStorage extends ActionBarActivity {

    private SearchHelperAdapter mDbHelper;
    private SokoDatabase dbSokoni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_storage);



populateListItem();
    }

        private void populateListItem() {
           // dbSokoni = new SokoDatabase();
            String data =    MyApplication.getWritableDatabase().getAllData();
           // = dbSokoni.getAllData();
            L.T(this,data);
            /**
             Cursor cursor = mDbHelper.getAllProduct();
             String[] fromFieldName = new String[] {
             SearchHelperAdapter.DatabaseHelper.COLUMN_ID,
             SearchHelperAdapter.DatabaseHelper.COLUMN_NAME
             };

             int[] toViewId = new int[] {
             R.id.textid, R.id.textit
             };

             SimpleCursorAdapter myCursoradapter;
             myCursoradapter = new SimpleCursorAdapter(getBaseContext(),
             R.layout.test_storage_list,
             cursor,
             fromFieldName,toViewId,0);

             ListView myList = (ListView) findViewById(R.id.listView);

             myList.setAdapter(myCursoradapter);
             **/
        }

}
