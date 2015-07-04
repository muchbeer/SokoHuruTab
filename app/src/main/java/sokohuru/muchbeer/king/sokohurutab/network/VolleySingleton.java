package sokohuru.muchbeer.king.sokohurutab.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import sokohuru.muchbeer.king.sokohurutab.Sokoni.MyApplication;

/**
 * Created by muchbeer on 5/19/2015.
 */
public class VolleySingleton {
    private static  VolleySingleton sInstance=null;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;

  //  private static Context mCtx;


    private VolleySingleton() {

       // mCtx = context;
        mRequestQueue = getRequestQueue();

        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private LruCache<String, Bitmap> cache = new LruCache<>((int)Runtime.getRuntime().maxMemory()/1024/8);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

                cache.put(url, bitmap);
            }
        });
    }

    public  static synchronized VolleySingleton getsInstance() {
        if(sInstance==null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }


    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
