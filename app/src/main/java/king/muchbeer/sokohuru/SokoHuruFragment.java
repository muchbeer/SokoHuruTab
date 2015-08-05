package king.muchbeer.sokohuru;


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
import android.util.Log;
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


import java.util.ArrayList;

import king.muchbeer.sokohuru.Sokoni.MyApplication;
import king.muchbeer.sokohuru.Sokoni.Soko;
import king.muchbeer.sokohuru.adapters.AdapterSoko;
import king.muchbeer.sokohuru.detail.MainActivityDetail;
import king.muchbeer.sokohuru.extras.Constants;
import king.muchbeer.sokohuru.extras.Keys;
import king.muchbeer.sokohuru.loggin.L;
import king.muchbeer.sokohuru.network.VolleySingleton;

import static king.muchbeer.sokohuru.extras.Keys.EndpointBoxOffice.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class SokoHuruFragment extends Fragment implements AdapterSoko.ClickListenerSearch, AdapterSoko.ClickListener, SearchView.OnQueryTextListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "position";
    private static final String ARG_PARAM2 = "param2";

    //public static final String URL_SOKO = "http://sokouhuru.com/ccm/uchaguzi2.json";

    //public static final String URL_SOKO = "http://sokouhuru.com/kamaz_get_all_products.php";

    public static final String URL_SOKO = "http://sokouhuru.com/kamaz_get_all_products.php";

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

    public ArrayList<Soko> listFilterBest = new ArrayList<>();


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
    private ProgressBar circularProgress;
    private TextView txtswipeRefresh;
    private int findPosition;
    private int enterSearchZone;


    // TODO: Rename and change types and number of parameters
    public static SokoHuruFragment getInstance(int position ) {
        SokoHuruFragment fragment = new SokoHuruFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_SOKO, listMovies);
    }

    public SokoHuruFragment() {
        // Required empty public constructor
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

        // sendJsonRequest();


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
        String matchFound = "N";

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

                    //soko huru
                    String name = Constants.NA;
                    String price = Constants.NA;
                    String contact = Constants.NA;
                    String location = Constants.NA;

                    JSONObject currentMarket = arrayMoview.getJSONObject(i);

                    if(currentMarket.has(KEY_NAME) && !currentMarket.isNull(KEY_NAME)) {
                        name = currentMarket.getString(KEY_NAME);
                    }

                    if(currentMarket.has(KEY_IMAGE) && !currentMarket.isNull(KEY_IMAGE)) {

                        imaging = currentMarket.getString(KEY_IMAGE);

                    }// int releaseYear = currentMarket.getInt(KEY_YEAR);

                    if(currentMarket.has(KEY_PRICE) && !currentMarket.isNull(KEY_PRICE)) {

                        price = currentMarket.getString(KEY_PRICE);

                    }
                    if(currentMarket.has(KEY_CONTACT) && !currentMarket.isNull(KEY_CONTACT)) {

                        contact = currentMarket.getString(KEY_CONTACT);
                    }

                    if(currentMarket.has(KEY_LOCATION) && !currentMarket.isNull(KEY_LOCATION)) {

                        location = currentMarket.getString(KEY_LOCATION);
                    }

                    data.append(name+ "\n");

                    Soko sokoni = new Soko();
                    // sokoni.setId(id);
                    sokoni.setName(name);
                    sokoni.setImage(imaging);
                    //   sokoni.setRating(rating);
                    sokoni.setPrice(price);
                    sokoni.setContact(contact);
                    sokoni.setLocation(location);
                    //  sokoni.setReleaseYear(releaseYear);

                  /*
                   Release year has someKind of problem try observe that has you move forward
                   sokoni.setReleaseYear(releaseYear);
                    */

                    matchFound ="N";
                    /*
                    for (int j=0; j<listMovies.size(); j++) {
                        if(listMovies.get(j).getTitle().equals(sokoni.getTitle())) {
                            matchFound="Y";
                        }
                    }
                    if(matchFound =="N") {
                        listFilterBest.add(sokoni);
                    }

*/

                if(!name.equals(Constants.NA)) {
                        listMovies.add(sokoni);
                    }

                }


                // L.t(getActivity(), data.toString());
            //    L.T(getActivity(), "SOKONI NOW" + listMovies.toString());

            } catch (JSONException e) {
                L.t(getActivity(), e.toString());
            }

        }
     //   MyApplication.getWritableDatabase().insertSokoOffice(listMovies, true);
        return listMovies;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_soko_huru, container, false);
        // Inflate the layout for this fragment



        Bundle bundle = getArguments();
        if(bundle !=null) {
           // Toast.makeText(getActivity(),"The selected page is: " + bundle.getInt("position"), Toast.LENGTH_LONG).show();
        }

        mTextError = (TextView) view.findViewById(R.id.textVolleyError);
        txtPosition = (TextView) view.findViewById(R.id.position);
     //   btnRefresh = (Button) view.findViewById(R.id.btnRefresh);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        circularProgress = (ProgressBar) view.findViewById(R.id.progressBar);
        txtswipeRefresh = (TextView) view.findViewById(R.id.infoText);


        swipeRefreshLayout.setColorSchemeColors(
                Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);


        //Swipe refresh 
  //      swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);


        listSokoni = (RecyclerView) view.findViewById(R.id.listSokoni);
        listSokoni.setHasFixedSize(true);


     //   listSokoni.getAdapter().notifyDataSetChanged();

        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
// Attach the layout manager to the recycler view
        listSokoni.setLayoutManager(gridLayoutManager);
 // listSokoni.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterSoko = new AdapterSoko(getActivity());
        adapterSoko.setClickListener(this);
        listSokoni.setAdapter(adapterSoko);
      // listSearch = adapterSoko.setSokoList();
        if(savedInstanceState !=null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_SOKO);
            adapterSoko.setSokoList(listMovies);
        }
        else {
            sendJsonRequest();
          //  MyApplication.getWritableDatabase().getAllItemFromMarket();
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

        return view;


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
     //   super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.menu_search);
       searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            return true;
                        case KeyEvent.KEYCODE_DEL:

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
    public void returnPosition( int position) {


        findPosition = position;
        //itemClicked2(view, position);
    }

    @Override
    public void itemClicked2(View view, int positionSearch) {
        //Toast.makeText(getActivity(),"Pata uondo kutoka Soko Huru:  ", Toast.LENGTH_LONG).show();

    }

    @Override
    public void itemClicked(View view, int position) {
        // Toast.makeText(getActivity(), "Item Clicked at " + position, Toast.LENGTH_LONG).show();
        // result = String.valueOf(position);
        if(enterSearchZone != 1) {
            Intent startIntent = new Intent(getActivity(), MainActivityDetail.class);
            // positionSearch = getActivity().position;
            startIntent.putExtra(TAG_POSITION, position);
            //    startIntent.putExtra(TAG_POSITION2, positionSearch);
            startActivityForResult(startIntent, SHARING_CODE);

        }

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        //adapterSoko.setClickListenerSearch(this);
        enterSearchZone = 1;
       // final ArrayList<Soko> filteredModelList = adapterSoko.filter(listMovies, query);
       // adapterSoko.filter( query);

        final ArrayList<Soko> filteredModelList = filterBestSearch(listFilterBest, query);
        adapterSoko.animateTo(filteredModelList);
      //  adapterSoko.filter(listMovies, query);
     //   listSokoni.scrollToPosition(0);
        Log.d("SOKO HURU SEARCH", String.valueOf(listMovies));

        //searchView.clearFocus();
      //  this.notifyDataSentChanged();
        return true;
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
        adapterSoko.notifyDataSetChanged();
        return filteredModelList;
    }

    private ArrayList<Soko> filterBestSearch(ArrayList<Soko> models, String query) {
        query = query.toLowerCase();


        ArrayList<Soko> filteredModelList = new ArrayList<>();
        if (query.length() == 0) {
            //  listSokoni.setAdapter(adapterSoko);
            sendJsonRequest();
            //   InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            //   imm.hideSoftInputFromWindow(View.getWindowToken(), 0);
        } else {
            for (int i = 0; i<listMovies.size(); i++) {
                final String text = listMovies.get(i).getName().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(listMovies.get(i));
                    positionSearch = i;
                }
            }

        }
        adapterSoko.notifyDataSetChanged();
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
