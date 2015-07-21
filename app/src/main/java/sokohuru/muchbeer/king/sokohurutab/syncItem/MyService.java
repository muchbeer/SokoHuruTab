package sokohuru.muchbeer.king.sokohurutab.syncItem;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

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
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import sokohuru.muchbeer.king.sokohurutab.R;
import sokohuru.muchbeer.king.sokohurutab.SokoHuruFragment;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.MyApplication;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.Soko;
import sokohuru.muchbeer.king.sokohurutab.extras.Constants;
import sokohuru.muchbeer.king.sokohurutab.loggin.L;
import sokohuru.muchbeer.king.sokohurutab.network.VolleySingleton;

import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_GENRE;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_IMAGE;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_RATING;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_SOKO;
import static sokohuru.muchbeer.king.sokohurutab.extras.Keys.EndpointBoxOffice.KEY_TITLE;

/**
 * Created by muchbeer on 6/24/2015.
 */
//@TargetApi(Build.VERSION_CODES.LOLLIPOP)
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyService extends JobService {

    private ImageLoader imageLoader;
    private static RequestQueue requestQueue;
    // private OnFragmentInteractionListener mListener;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        L.t(this, "onStartJob");

        new MyTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        L.t(this, "onStopJob");
        return false;
    }

    private static class MyTask extends AsyncTask<JobParameters, Void, JobParameters> {

        MyService myService;
        private VolleySingleton volleySingleton;


        MyTask(MyService myService) {

            this.myService = myService;
            volleySingleton = VolleySingleton.getsInstance();
            requestQueue = volleySingleton.getRequestQueue();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... jobParameterses) {


           JSONObject response =  sendJsonRequest();
           ArrayList<Soko> listSoko = parseJSONResponse(response);

            MyApplication.getWritableDatabase().insertSokoOffice(listSoko, true);
            return jobParameterses[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
           myService.jobFinished(jobParameters, false);
        }


        public JSONObject sendJsonRequest() {
            JSONObject response = null;

            RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    SokoHuruFragment.URL_SOKO,
                     null,
               requestFuture,requestFuture);
            requestQueue.add(request);

            try {
                 response = requestFuture.get(17000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                L.m(e + " ");
            } catch (ExecutionException e) {
               L.m(e +  " ");
            } catch (TimeoutException e) {
                L.m(e + " ");
            }

            return response;
        }


        private ArrayList<Soko> parseJSONResponse(JSONObject response) {
            ArrayList<Soko> listMovies = new ArrayList<>();

            if (response == null || response.length() == 0) {
             //   L.t(getActivity(), "Refresh data");
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

                  //  L.T(getActivity(), listMovies.toString());

                } catch (JSONException e) {
                  //  L.t(getActivity(), e.toString());
                }

            }

            return listMovies;
        }

        private boolean contains(JSONObject jsonObject, String key) {
            return jsonObject !=null && jsonObject.has(key) && !jsonObject.isNull(key)
                    ? true:false;
        }
    }
}
