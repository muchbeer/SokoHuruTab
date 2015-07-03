package sokohuru.muchbeer.king.sokohurutab.Sokoni;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import sokohuru.muchbeer.king.sokohurutab.database.SokoDatabase;
import sokohuru.muchbeer.king.sokohurutab.detail.LruBitmapCache;

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
}
