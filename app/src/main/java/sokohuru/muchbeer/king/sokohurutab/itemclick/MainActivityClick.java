package sokohuru.muchbeer.king.sokohurutab.itemclick;

import android.app.Fragment;
import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import sokohuru.muchbeer.king.sokohurutab.R;

public class MainActivityClick extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_click);


        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment

        FragmentClick fragment = new FragmentClick();

        FragmentManager fm = getFragmentManager();

        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.replace(R.id.container2, fragment);

        fragmentTransaction.commit();




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_click, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
