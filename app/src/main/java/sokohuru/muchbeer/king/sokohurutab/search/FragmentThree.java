package sokohuru.muchbeer.king.sokohurutab.search;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOutOfMemoryException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import sokohuru.muchbeer.king.sokohurutab.*;
import sokohuru.muchbeer.king.sokohurutab.MainActivity;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.MyApplication;
import sokohuru.muchbeer.king.sokohurutab.connectdata.AppConfig;
import sokohuru.muchbeer.king.sokohurutab.connectdata.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThree extends Fragment {

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String KEY_NAME = "name";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_LINK = "link";
    private TextView txtName,txtPrice, txtContact,txtLink;
    private String name,price,contact;
    private String image;

    private static final int SHARING_CODE = 1;
    private static final String TAG_POSITION = "position";
    private String position;

    //INSERTING DATA
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private EditText edtPlace, edtDesc, edtName, edtPrice, edtContact;
    private String location, description;
 //   private static final String TAG = "Tell error";

    private static final String TAG = FragmentThree.class.getSimpleName();
    MainActivity actionIntent = new MainActivity();
    private TextView txtDesc, txtLocation;
    final static String ARG_STRING = "name";
    private String putName;
     String getName, getPrice, getContact;
    String naming = "jo";
    private String itemName;

    public FragmentThree() {
        // Required empty public constructor
    }



    //Getting username from Google
    private String SHARED_KEY = "Name";
    String login_username;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.search_fragment_three, container, false);


       edtPlace = (EditText) view.findViewById(R.id.edt_place);
        edtDesc = (EditText) view.findViewById(R.id.edt_desc);
    txtName = (TextView) view.findViewById(R.id.txTitle);
        edtName = (EditText) view.findViewById(R.id.edt_name);
        edtPrice = (EditText) view.findViewById(R.id.edt_price);
        edtContact = (EditText) view.findViewById(R.id.edt_phone);




        //INSERTTING RECORDING
        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);


        Button btnSubmit = (Button) view.findViewById(R.id.sendInformation);

    btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

                editor = sharedpreferences.edit();

                location  = edtPlace.getText().toString();
                description  = edtDesc.getText().toString();
                name = edtName.getText().toString();
                price = edtPrice.getText().toString();
                contact =edtContact.getText().toString();

                  image =    sharedpreferences.getString(KEY_LINK, "");
                login_username = sharedpreferences.getString(SHARED_KEY, "");// getting Long
               // Toast.makeText(getActivity(), "Email yako ni: " + username , Toast.LENGTH_LONG).show();


                //INSERTING STAFF
                if (!name.isEmpty() && !price.isEmpty() && !contact.isEmpty()
                        && image.length()>0 && !location.isEmpty() && !description.isEmpty()) {
                    registerUser();
                   // editor.clear();
                  //  editor.commit();
                    txtName.setText("");

                } else {


                        txtName.setText("Tafadhari, jaza sehemu zilizowazi kisha uzitunze pia " +
                                "hakikisha umeweka picha");


                  Toast.makeText(getActivity(), "The error is: " + image , Toast.LENGTH_LONG).show();
                }
            }


        });


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }


    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser() {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Subiri Unasajiriwa ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                String pointError = response.toString();
              //  edtPlace.setText("");
              //  edtDesc.setText("");

                if(pointError.contains("Ongera")) {
                    Toast.makeText(getActivity(), "Umeweza asilimia zote", Toast.LENGTH_LONG).show();

                    Intent getBackToMainActivity = new Intent(getActivity(),
                            sokohuru.muchbeer.king.sokohurutab.MainActivity.class);
                    getBackToMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(getBackToMainActivity);


                } else {
                    Toast.makeText(getActivity(), "kuna tatizo " + pointError, Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Tafadhari jaribu tena " +
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
              //  params.put("tag", "register");
                params.put("name", name);
                params.put("price", price);
                params.put("image", image);
                params.put("contact", contact);
                params.put("location", location);
                params.put("description", description);
                params.put("login_username", login_username);

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getsInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    // newInstance constructor for creating fragment with arguments
    public static FragmentThree newInstance(String location, String descr) {
        FragmentThree fragmentThird = new FragmentThree();
        Bundle args = new Bundle();
        // args.putInt("someInt", page);
        args.putString("location", location);
        args.putString("description", descr);
        fragmentThird.setArguments(args);
        return fragmentThird;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.d("SOKO HURU", "On Destroy FragmentThree");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("SOKO HURU", "On Detach FragmentThree");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("SOKO HURU", "OnPause Fragment three");

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("SOKO HURU", "OnDestroyView Fragment three");

       }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("SOKO HURU", "OnStart Fragment three");


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SOKO HURU", "OnResume Fragment three");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("SOKO HURU", "OnStop Fragment three");

    }
}
