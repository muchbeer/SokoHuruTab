package sokohuru.muchbeer.king.sokohurutab.syncItem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import sokohuru.muchbeer.king.sokohurutab.R;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.MyApplication;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;
import sokohuru.muchbeer.king.sokohurutab.adapters.AdapterSoko;
import sokohuru.muchbeer.king.sokohurutab.extras.Constants;
import sokohuru.muchbeer.king.sokohurutab.loggin.L;
import sokohuru.muchbeer.king.sokohurutab.network.VolleySingleton;

import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_GENRE;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_IMAGE;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_RATING;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_SOKO;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_TITLE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SokoHuruFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String URL_SOKO = "http://sokouhuru.com/ccm/uchaguzi2.json";
    private static final String STATE_SOKO = "State Sokoni";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private ArrayList<Soko> listMovies = new ArrayList<>();

    private RecyclerView listSokoni;
    private RecyclerView.LayoutManager sLayoutManager;

    // private OnFragmentInteractionListener mListener;
    private VolleySingleton volleySingleton;

    private AdapterSoko adapterSoko;
    private TextView mTextError;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSoko.
     */
    // TODO: Rename and change types and number of parameters
    public static SokoHuruFragment newInstance(String param1, String param2) {
        SokoHuruFragment fragment = new SokoHuruFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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


        // sendJsonRequest();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_soko_huru, container, false);
        // Inflate the layout for this fragment


        mTextError = (TextView) view.findViewById(R.id.textVolleyError);

        listSokoni = (RecyclerView) view.findViewById(R.id.listSokoni);
        listSokoni.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterSoko = new AdapterSoko(getActivity());
        listSokoni.setAdapter(adapterSoko);
        if(savedInstanceState !=null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_SOKO);

        }
        else {
      //      sendJsonRequest();

           listMovies =  MyApplication.getWritableDatabase().getAllItemFromMarket();

        }

        adapterSoko.setSokoList(listMovies);
        return view;

    }


}
