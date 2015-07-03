package sokohuru.muchbeer.king.sokohurutab.detail;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import sokohuru.muchbeer.king.sokohurutab.R;
import sokohuru.muchbeer.king.sokohurutab.adapters.AdapterSoko;
import sokohuru.muchbeer.king.sokohurutab.network.VolleySingleton;

public class MainActivityDetail extends ActionBarActivity {

    private Button btGetPosition;
    //private static final int SHARING_CODE = 1;
    private TextView txGetResult;

   // private static final String TAG_POSITION = "position";
    private static final String TAG_NAME = "name";
    private String position;

// Getting good detail
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String URL_SOKO = "http://sokouhuru.com/ccm/uchaguzi2.json";
    private static final String STATE_SOKO = "State Sokoni";
    private static final int SHARING_CODE = 1;
    private static final String TAG_POSITION = "position";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;

    private RecyclerView listSokoni;
    private RecyclerView.LayoutManager sLayoutManager;




    // private OnFragmentInteractionListener mListener;
    private VolleySingleton volleySingleton;

    private AdapterSoko adapterSoko;
    private TextView mTextError;
    private TextView txtName;
    private String result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);


        //Getting item details from Intent
        Intent collectDataIntent = getIntent();
        position = collectDataIntent.getStringExtra(TAG_POSITION);
       txGetResult.setText(position);
        Toast.makeText(getApplication(), "The new position is: "+ position, Toast.LENGTH_LONG).show();


        btGetPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String message=txGetResult.setText();


                //  Intent intent=new Intent();
                //  intent.putExtra("MESSAGE",message);
                //  setResult(SHARING_CODE,intent);
                //     finish();//finishing activity



              //  setResult(SHARING_CODE, collectDataIntent);
              //  finish();



            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_detail, menu);
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
