package king.muchbeer.sokohuru;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;


public class LoginFragment extends Fragment implements
        ConnectionCallbacks, OnConnectionFailedListener,
        View.OnClickListener {


    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    private PendingIntent mSignInIntent;

    private SignInButton mSignInButton;
    private Button mSignOutButton;
    private Button mRevokeButton;
    private TextView mStatus;

    public static final String MyPREFERENCES = "MyPrefs";
 //   SharedPreferences sharedpreferences;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
      private String SHARED_KEY = "Name";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main_login, container, false);
        // Get references to all of the UI views
        mSignInButton = (SignInButton) view.findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) view.findViewById(R.id.sign_out_button);
        mRevokeButton = (Button) view.findViewById(R.id.revoke_access_button);
        mStatus = (TextView) view.findViewById(R.id.statuslabel);

        // Add click listeners for the buttons
        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRevokeButton.setOnClickListener(this);

        // Build a GoogleApiClient
        mGoogleApiClient = buildGoogleApiClient();

        //Save value
        return view;
    }



    private GoogleApiClient buildGoogleApiClient() {
        return new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope("email"))
                .build();
    }


    @Override
    public void onStart() {
        super.onStart();
       // mGoogleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onConnected(Bundle bundle) {

        mSignInButton.setEnabled(true);
        mSignOutButton.setEnabled(true);
        mRevokeButton.setEnabled(true);


        //set the shared preferences
        // Indicate that the sign in process is complete.
        mSignInProgress = SIGNED_IN;

        try {
            String emailAddress = Plus.AccountApi.getAccountName(mGoogleApiClient);

          preferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            editor = preferences.edit();
           editor.putString(SHARED_KEY, emailAddress);

            // Save the changes in SharedPreferences
            editor.commit(); // commit changes
            //mStatus.setText(String.format("Signed In to My App as %s", emailAddress));
            Toast.makeText(getActivity(),"Umejisajiri kama: " + emailAddress, Toast.LENGTH_LONG).show();
                mStatus.setVisibility(View.GONE);

            Intent connectsuccessful = new Intent(getActivity(),  king.muchbeer.sokohuru.search.MainActivity.class);
            startActivity(connectsuccessful);
        }
        catch(Exception ex){
            String exception = ex.getLocalizedMessage();
            String exceptionString = ex.toString();
            // Note that you should log these errors in a ‘real’ app to aid in debugging
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View view) {


        if (!mGoogleApiClient.isConnecting()) {
            switch (view.getId()) {
                case R.id.sign_in_button:
                    mStatus.setVisibility(View.VISIBLE);
                    mStatus.setText("Subiri au jaribu tena....");
                    mGoogleApiClient.connect();
                //    mGoogleApiClient.connect();
                    resolveSignInError();
                    break;
                case R.id.sign_out_button:
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    break;
                case R.id.revoke_access_button:
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                    mGoogleApiClient = buildGoogleApiClient();
                    mGoogleApiClient.connect();
                    break;
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = connectionResult.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }
        // Will implement shortly
        onSignedOut();
    }

    private void onSignedOut() {

        // Update the UI to reflect that the user is signed out.
        mSignInButton.setEnabled(true);
        mSignOutButton.setEnabled(false);
        mRevokeButton.setEnabled(false);

      //  mStatus.setText("");

    }

    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                getActivity().startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                mSignInProgress = STATE_SIGNING_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // You have a play services error -- inform the user
          //  Toast.makeText(getActivity(), "Tatizo la mtandao", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == Activity.RESULT_OK) {
                    mSignInProgress = STATE_SIGNING_IN;
                } else {
                    mSignInProgress = SIGNED_IN;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }

    }
}
