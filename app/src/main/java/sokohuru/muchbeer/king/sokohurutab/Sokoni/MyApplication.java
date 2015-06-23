package sokohuru.muchbeer.king.sokohurutab.Sokoni;

import android.app.Application;
import android.content.Context;

/**
 * Created by muchbeer on 5/19/2015.
 */
public class MyApplication extends Application{
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance=this;
    }

    public static MyApplication getsInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
