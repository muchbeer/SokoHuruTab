package sokohuru.muchbeer.king.sokohurutab.tab;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.pushbots.push.Pushbots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sokohuru.muchbeer.king.sokohurutab.R;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.MyApplication;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;
import sokohuru.muchbeer.king.sokohurutab.adapters.AdapterSoko;
import sokohuru.muchbeer.king.sokohurutab.detail.MainActivityDetail;
import sokohuru.muchbeer.king.sokohurutab.extras.Constants;
import sokohuru.muchbeer.king.sokohurutab.loggin.L;
import sokohuru.muchbeer.king.sokohurutab.network.VolleySingleton;

import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_GENRE;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_IMAGE;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_RATING;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_SOKO;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_TITLE;

public class MainActivitySoko extends ActionBarActivity implements AdapterSoko.ClickListener, SearchView.OnQueryTextListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "position";
    private static final String ARG_PARAM2 = "param2";

    public static final String URL_SOKO = "http://sokouhuru.com/ccm/uchaguzi2.json";
    private static final String STATE_SOKO = "State Sokoni";
    private static final int SHARING_CODE = 1;
    private static final String TAG_POSITION = "position";
    private static final String TAG_POSITION2 = "position2";


    RecyclerView.LayoutManager mLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    public ArrayList<Soko> listMovies = new ArrayList<>();

    public RecyclerView listSokoni;
    private RecyclerView.LayoutManager sLayoutManager;

    //Searchable

    // private SearchView mSearchView;



    // private OnFragmentInteractionListener mListener;
    private VolleySingleton volleySingleton;

    private AdapterSoko adapterSoko;
    private TextView mTextError;
    private TextView txtPosition;
    private String result;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnRefresh;
    private SearchView searchView;
    private int positionSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_soko);

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();

        //Monitoring
        Pushbots.sharedInstance().init(this);

        try
        {
            Tracker t = ((MyApplication) getApplication()).getTracker(
                    MyApplication.TrackerName.APP_TRACKER);

            t.setScreenName("MainPage");

            t.send(new HitBuilders.AppViewBuilder().build());
        }
        catch(Exception  e)
        {
            Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);

        }
        mTextError = (TextView) findViewById(R.id.textVolleyError);
        txtPosition = (TextView) findViewById(R.id.position);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        //Swipe refresh
        //      swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);


        listSokoni = (RecyclerView) findViewById(R.id.listSokoni);
      listSokoni.setHasFixedSize(true);


        //   listSokoni.getAdapter().notifyDataSetChanged();

        // The number of Columns
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        listSokoni.setLayoutManager(mLayoutManager);
        //  listSokoni.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterSoko = new AdapterSoko(getApplicationContext());
        adapterSoko.setClickListener(this);
        listSokoni.setAdapter(adapterSoko);
        // listSearch = adapterSoko.setSokoList();
        if(savedInstanceState !=null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_SOKO);
            adapterSoko.setSokoList(listMovies);
        }
        else {
            sendJsonRequest();
            //     adapterSoko.notifyDataSetChanged();
        }

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendJsonRequest();
                btnRefresh.setVisibility(View.GONE);
            }
        });

    }

    public void sendJsonRequest() {
        // showing refresh animation before making http call
        // swipeRefreshLayout.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                URL_SOKO,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        //  parseJSONResponse(jsonObject);
                        // L.t(getActivity(), jsonObject.toString());
                        listMovies = parseJSONResponse(jsonObject);
                        adapterSoko.setSokoList(listMovies);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        //    L.t(getActivity(),error.toString());
                        //if any error occurs in the network operations, show the TextView that contains the error message
                        mTextError.setVisibility(View.VISIBLE);
                        btnRefresh.setVisibility(View.VISIBLE);
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


    private ArrayList<Soko> parseJSONResponse(JSONObject response) {
        ArrayList<Soko> listMovies = new ArrayList<>();

        if (response == null || response.length() == 0) {
            L.t(getApplicationContext(), "Refresh data");
        }
        else   if (response != null && response.length() > 0) {



            try {

                StringBuilder data = new StringBuilder();
                JSONArray arrayMoview = response.getJSONArray(KEY_SOKO);

                for (int i = 0; i < arrayMoview.length(); i++) {

                    String title = Constants.NA;
                    String imaging = Constants.NA;

                    String rating=Constants.NA;
                    String genre=Constants.NA;
                    JSONObject currentMarket = arrayMoview.getJSONObject(i);

                    if(currentMarket.has(KEY_TITLE) && !currentMarket.isNull(KEY_TITLE)) {
                        title = currentMarket.getString(KEY_TITLE);
                    }

                    if(currentMarket.has(KEY_IMAGE) && !currentMarket.isNull(KEY_IMAGE)) {

                        imaging = currentMarket.getString(KEY_IMAGE);

                    }// int releaseYear = currentMarket.getInt(KEY_YEAR);

                    if(currentMarket.has(KEY_RATING) && !currentMarket.isNull(KEY_RATING)) {

                        rating = currentMarket.getString(KEY_RATING);

                    }
                    if(currentMarket.has(KEY_GENRE) && !currentMarket.isNull(KEY_GENRE)) {

                        genre = currentMarket.getString(KEY_GENRE);
                    }

                    data.append(title + "\n");

                    Soko sokoni = new Soko();
                    // sokoni.setId(id);
                    sokoni.setTitle(title);
                    sokoni.setImage(imaging);
                    //   sokoni.setRating(rating);
                    sokoni.setGenre(genre);
                    //  sokoni.setReleaseYear(releaseYear);

                  /*
                   Release year has someKind of problem try observe that has you move forward
                   sokoni.setReleaseYear(releaseYear);
                    */

                    if(!title.equals(Constants.NA)) {
                        listMovies.add(sokoni);
                    }

                }


                // L.t(getActivity(), data.toString());

                //      L.T(getActivity(), listMovies.toString());

            } catch (JSONException e) {
                L.t(getApplicationContext(), e.toString());
            }

        }

        return listMovies;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);


        item.setActionView(searchView);

        return true;
    }





    @Override
    protected void onStart() {
        super.onStart();
    //    startTab();


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

    @Override
    public void itemClicked(View view, int position) {
        // Toast.makeText(getActivity(), "Item Clicked at " + position, Toast.LENGTH_LONG).show();
        // result = String.valueOf(position);
        Intent startIntent = new Intent(getApplicationContext(), MainActivityDetail.class);
        // positionSearch = getActivity().position;
        startIntent.putExtra(TAG_POSITION, position);
        //    startIntent.putExtra(TAG_POSITION2, positionSearch);
        startActivityForResult(startIntent, SHARING_CODE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Intent myIntent = getIntent(); // gets the previously created intent
        positionSearch = myIntent.getIntExtra(TAG_POSITION, -1);


        adapterSoko.setSokoList(listMovies);
        final ArrayList<Soko> filteredModelList = filter(listMovies,   query);
        adapterSoko.animateTo(filteredModelList);
        listSokoni.scrollToPosition(positionSearch);

        //searchView.clearFocus();
        //  this.notifyDataSentChanged();
        return true;
      //  return false;
    }

    private ArrayList<Soko> filter(ArrayList<Soko> models, String query) {
        query = query.toLowerCase();

        final ArrayList<Soko> filteredModelList = new ArrayList<>();
        for (Soko model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
