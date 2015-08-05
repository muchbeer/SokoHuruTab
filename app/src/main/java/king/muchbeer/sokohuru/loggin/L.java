package king.muchbeer.sokohuru.loggin;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by muchbeer on 6/5/2015.
 */
public class L {

    public static void m(String message) {
        Log.d("SOKO HURU DATABASE", "" + message);
    }

    public static void t(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }

    public static void T(Context context, String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_LONG).show();
    }
}
