package sokohuru.muchbeer.king.sokohurutab.extras;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import sokohuru.muchbeer.king.sokohurutab.R;

/**
 * Created by muchbeer on 7/7/2015.
 */
public class AddNewItem extends ActionBarActivity{

    SharedPreferences preferences;
    private String SHARED_KEY = "Name";
    String name;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        userName = (TextView) findViewById(R.id.user_name);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
      name = preferences.getString(SHARED_KEY, "");
        if(!name.equalsIgnoreCase(""))
        {
           name = "Welcome: " + name;  /* Edit the value here*/
        }
        userName.setText(name);


    }
}
