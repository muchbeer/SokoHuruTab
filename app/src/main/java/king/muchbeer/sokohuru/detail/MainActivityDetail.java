package king.muchbeer.sokohuru.detail;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import king.muchbeer.sokohuru.R;
import  king.muchbeer.sokohuru.Sokoni.MyApplication;
import  king.muchbeer.sokohuru.adapters.AdapterSoko;
import  king.muchbeer.sokohuru.itemclick.FragmentClick;
import  king.muchbeer.sokohuru.network.VolleySingleton;

public class MainActivityDetail extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);


        //Get a Tracker (should auto-report)
        ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);

        FragmentClick fragment = new FragmentClick();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container3, fragment);

        fragmentTransaction.commit();

        try
        {
            Tracker t = ((MyApplication) getApplication()).getTracker(
                    MyApplication.TrackerName.APP_TRACKER);

            t.setScreenName("Detail Activity");

            t.send(new HitBuilders.AppViewBuilder().build());
        }
        catch(Exception  e)
        {
            Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }  @Override
       public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }




    @Override
    protected void onStart() {
        super.onStart();



        //Get an Analytics tracker to report app starts & uncaught exceptions etc.
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        //  GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//Stop the analytics tracking
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }
}
