package sokohuru.muchbeer.king.sokohurutab.itemclick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sokohuru.muchbeer.king.sokohurutab.R;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;
import sokohuru.muchbeer.king.sokohurutab.adapters.AdapterSoko;
import sokohuru.muchbeer.king.sokohurutab.extras.Constants;
import sokohuru.muchbeer.king.sokohurutab.extras.Keys;
import sokohuru.muchbeer.king.sokohurutab.loggin.L;
import sokohuru.muchbeer.king.sokohurutab.network.VolleySingleton;

/**
 * Created by muchbeer on 7/1/2015.
 */
public class FragmentClick  extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String URL_SOKO = "http://sokouhuru.com/ccm/uchaguzi2.json";
    private static final String STATE_SOKO = "State Sokoni";
    private static final int SHARING_CODE = 1;
    private static final String TAG_POSITION = "position";

    private int position;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
   // private ArrayList<Soko> listMovies = new ArrayList<>();

    private RecyclerView listSokoni;
    private RecyclerView.LayoutManager sLayoutManager;




    // private OnFragmentInteractionListener mListener;
    private VolleySingleton volleySingleton;

    private AdapterSoko adapterSoko;
    private TextView mTextError;
    private TextView txtName;
    String result;
  //  private String position;
    private String title;
    //  private char[] title;


    public static FragmentClick newInstance(String mParam1, String mParam2){
        FragmentClick fragmentClick = new FragmentClick();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
        args.putString(ARG_PARAM2, mParam2);
        fragmentClick.setArguments(args);

        return fragmentClick;
    }


    public FragmentClick() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }


    public void sendJsonRequest(final int position) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL_SOKO,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //  parseJSONResponse(jsonObject);
                        // L.t(getActivity(), jsonObject.toString());
                        parseJSONResponse(jsonObject, position);
                       // adapterSoko.setSokoList(listMovies);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        L.t(getActivity(), error.toString());
                        //if any error occurs in the network operations, show the TextView that contains the error message
                        mTextError.setVisibility(View.VISIBLE);
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            mTextError.setText(R.string.error_timeout);

                        } else if (error instanceof AuthFailureError) {
                            mTextError.setText(R.string.error_auth_failure);
                            //TODO
                        } else if (error instanceof ServerError) {
                            mTextError.setText(R.string.error_auth_failure);
                            //TODO
                        } else if (error instanceof NetworkError) {
                            mTextError.setText(R.string.error_network);
                            //TODO
                        } else if (error instanceof ParseError) {
                            mTextError.setText(R.string.error_parser);
                            //TODO
                        }
                    }
                });
        requestQueue.add(request);
    }

    private void parseJSONResponse(JSONObject response, int position) {

        if (response == null || response.length() == 0) {
            L.t(getActivity(), "Refresh data");
        } else if (response != null && response.length() > 0) {


            try {
                StringBuilder data = new StringBuilder();
                JSONArray arrayMoview = response.getJSONArray(Keys.EndpointBoxOffice.KEY_SOKO);

                title = Constants.NA;
                String imaging = Constants.NA;

                String rating = Constants.NA;
                String genre = Constants.NA;
                JSONObject currentMarket = arrayMoview.getJSONObject(position);


                if (currentMarket.has(Keys.EndpointBoxOffice.KEY_TITLE) && !currentMarket.isNull(Keys.EndpointBoxOffice.KEY_TITLE)) {
                    title = currentMarket.getString(Keys.EndpointBoxOffice.KEY_TITLE);
                }

                Soko sokoni = new Soko();

                //sokoni setTitle
                sokoni.setTitle(title);

                txtName.setText(title);
                Toast.makeText(getActivity(), "Item is: " + title,Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                L.t(getActivity(), e.toString());
            }

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_click, container, false);
      //  mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
       // adapter = new ListAdapterHolder(mActivity);

        txtName = (TextView) rootView.findViewById(R.id.name);
        mTextError = (TextView) rootView.findViewById(R.id.errors);

        //Getting item details from Intent
        Intent collectDataIntent = getActivity().getIntent();
      //  position =
     //   collectDataIntent.getStringExtra(TAG_POSITION);

       position = collectDataIntent.getIntExtra(TAG_POSITION, -1);

      //  txtName.setText(position);
      //  Toast.makeText(getActivity(), "The new position is: " + position, Toast.LENGTH_LONG).show();

        sendJsonRequest(position);

        return rootView;
    }

}