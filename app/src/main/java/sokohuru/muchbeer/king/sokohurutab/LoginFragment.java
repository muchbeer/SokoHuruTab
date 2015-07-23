package sokohuru.muchbeer.king.sokohurutab;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import sokohuru.muchbeer.king.sokohurutab.adapters.AdapterSokoSearch;
import sokohuru.muchbeer.king.sokohurutab.detail.MainActivityDetail;
import sokohuru.muchbeer.king.sokohurutab.extras.Keys;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.*;

import java.util.ArrayList;

import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;
import sokohuru.muchbeer.king.sokohurutab.adapters.AdapterSokoSearch;
import sokohuru.muchbeer.king.sokohurutab.extras.Constants;
import sokohuru.muchbeer.king.sokohurutab.loggin.L;
import sokohuru.muchbeer.king.sokohurutab.network.VolleySingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment  implements AdapterSokoSearch.ClickListener, SearchView.OnQueryTextListener {

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
    public ArrayList<Soko> listMovies;
    private RecyclerView.LayoutManager sLayoutManager;

    private RecyclerView listSokoni;


    private VolleySingleton volleySingleton;

    private AdapterSokoSearch adapterSoko;
    private TextView mTextError;
    private TextView txtPosition;
    private String result;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btnRefresh;
    private SearchView searchView;
    private int positionSearch;
    private ProgressBar circularProgress;
    private TextView txtswipeRefresh;
    private ArrayList<Soko> mModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_login, container, false);



        listSokoni = (RecyclerView) view.findViewById(R.id.listSokoni);
        mTextError = (TextView) view.findViewById(R.id.textVolleyError);
        txtPosition = (TextView) view.findViewById(R.id.position);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        circularProgress = (ProgressBar) view.findViewById(R.id.progressBar);
        txtswipeRefresh = (TextView) view.findViewById(R.id.infoText);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         //   btnRefresh = (Button) view.findViewById(R.id.btnRefresh);

        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
   //  listSokoni.setHasFixedSize(true);

      // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
// Attach the layout manager to the recycler view
        listSokoni.setLayoutManager(gridLayoutManager);
        // listSokoni.setLayoutManager(new LinearLayoutManager(getActivity()));


        mModel = new ArrayList<>();
        adapterSoko = new AdapterSokoSearch(getActivity());
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

        if    (adapterSoko.getItemCount() == 0) {

            circularProgress.setVisibility(View.VISIBLE);

            //simulate doing something
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    //    swipeRefreshLayout.setRefreshing(false);
                    circularProgress.setVisibility(View.GONE);
                }

            }, 14000);


        } else {

            circularProgress.setVisibility(View.GONE);
        }


        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        /**
         // TextFilter
         //     listSokoni.setTextFilterEnabled(true);
         //  swipeRefreshLayout.setOnRefreshListener(this);

         /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    public void itemClicked(View view, int position) {

        Intent startIntent = new Intent(getActivity(), MainActivityDetail.class);
        // positionSearch = getActivity().position;
        startIntent.putExtra(TAG_POSITION, position);
        //    startIntent.putExtra(TAG_POSITION2, positionSearch);
        startActivityForResult(startIntent, SHARING_CODE);
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
                        //    btnRefresh.setVisibility(View.VISIBLE);
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
            L.t(getActivity(), "Refresh data");
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
                L.t(getActivity(), e.toString());
            }

        }

        return listMovies;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case  KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            return true;
                        case KeyEvent.KEYCODE_DEL:
                               // sendJsonRequest();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        searchView.setOnQueryTextListener(this);


        item.setActionView(searchView);
        //return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        if (query.length() > 2 ) {
            //      listMovies.clear();

            adapterSoko.setSokoList(listMovies);
            final ArrayList<Soko> filteredModelList = filter(listMovies, query);
            adapterSoko.animateTo(filteredModelList);
            listSokoni.scrollToPosition(0);

        }
        else {
            sendJsonRequest();
        }

        //searchView.clearFocus();
        //  this.notifyDataSentChanged();
        return true;    }

    private ArrayList<Soko> filter(ArrayList<Soko> models, String query) {
        query = query.toLowerCase();

        final ArrayList<Soko> filteredModelList = new ArrayList<>();
     //   if (query.length() > 2 ) {
      //      listMovies.clear();

            for (int i=0; i < models.size(); i++) {
                final String text = models.get(i).getTitle().toLowerCase();

                if (text.contains(query.toLowerCase())) {
                    filteredModelList.add(models.get(i));
                }
            }



        /**
     else {
            sendJsonRequest();
        }
         **/
        return filteredModelList;
    }
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener(){

        @Override
        public void onRefresh() {
            //textInfo.setText("WAIT: doing something");
            mTextError.setVisibility(View.GONE);
            sendJsonRequest();

            //simulate doing something
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    //  textInfo.setText("DONE");
                }

            }, 4000);
        }};
}
