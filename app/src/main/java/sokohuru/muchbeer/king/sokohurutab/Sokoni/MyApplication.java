package sokohuru.muchbeer.king.sokohurutab.Sokoni;

import android.app.Application;
import android.content.Context;

import sokohuru.muchbeer.king.sokohurutab.database.SokoDatabase;

/**
 * Created by muchbeer on 5/19/2015.
 */
public class MyApplication extends Application{
    private static MyApplication sInstance;
    private static SokoDatabase mDatabase;

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


}
