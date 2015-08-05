package  king.muchbeer.sokohuru.Sokoni;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import  king.muchbeer.sokohuru.R;
import  king.muchbeer.sokohuru.database.SokoDatabase;
import  king.muchbeer.sokohuru.detail.LruBitmapCache;

/**
 * Created by muchbeer on 5/19/2015.
 */
public class MyApplication extends Application{

    public static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication sInstance;
    private static SokoDatabase mDatabase;

    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance=this;
        mDatabase = new SokoDatabase(this);
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {

        return sInstance.getApplicationContext();
    }

    public synchronized static SokoDatabase getWritableDatabase() {
        if(mDatabase == null) {
            mDatabase = new SokoDatabase(getAppContext());
        }
        return mDatabase;
    }
public RequestQueue getRequestQueue() {
    if(mRequestQueue == null) {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }
    return mRequestQueue;
}

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
// set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }




        private static final String PROPERTY_ID = "UA-50504081-2";

        public static int GENERAL_TRACKER = 0;

        public enum TrackerName {
            APP_TRACKER,
            GLOBAL_TRACKER,
            ECOMMERCE_TRACKER,
        }

        HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

        public MyApplication() {
            super();
        }

     public  synchronized Tracker getTracker(TrackerName trackerId) {
            if (!mTrackers.containsKey(trackerId)) {

                GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
                analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
                Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                        : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(
                        R.xml.global_tracker)
                        : analytics.newTracker(R.xml.ecommerce_tracker);
                t.enableAdvertisingIdCollection(true);
                mTrackers.put(trackerId, t);
            }
            return mTrackers.get(trackerId);
        }
    }

