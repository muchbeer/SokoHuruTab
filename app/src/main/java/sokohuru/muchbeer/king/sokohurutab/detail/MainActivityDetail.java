package sokohuru.muchbeer.king.sokohurutab.detail;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import sokohuru.muchbeer.king.sokohurutab.R;

public class MainActivityDetail extends ActionBarActivity {

    private Button btGetPosition;
    private static final int SHARING_CODE = 1;
    private TextView txGetResult;

    private static final String TAG_POSITION = "position";
    private static final String TAG_NAME = "name";
    private String position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_detail);

        btGetPosition = (Button) findViewById(R.id.btPosition);
        txGetResult = (TextView) findViewById(R.id.txtResult);

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
