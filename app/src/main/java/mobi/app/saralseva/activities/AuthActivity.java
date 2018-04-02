package mobi.app.saralseva.activities;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DialogTitle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.CountryListSpinner;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import mobi.app.saralseva.R;
import mobi.app.saralseva.models.Login;
import mobi.app.saralseva.models.LoginResponse;
import mobi.app.saralseva.models.RequestSingletonClass;


public class AuthActivity extends AppCompatActivity {


    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "bW5NdE5KiMOQtBp6lHASoCYZe";
    private static final String TWITTER_SECRET = "gLuulGjyiuUtyqkfuD6hI7LFOeLP8EWZJFgckqO0UoBf52wivl";

    private static final String ENDPOINT = "http://saralseva.us-west-2.elasticbeanstalk.com/rest/login/get";

    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private RequestQueue requestQueue;

    private Gson gson;

    JSONObject params = new JSONObject();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    BaseApp baseApp;
    private AuthCallback authCallback;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        TextView loginText;
        super.onCreate(savedInstanceState);

//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());


//        Digits.Builder digitsBuilder = new Digits.Builder().withTheme(R.style.CustomDigitsTheme);
//        Fabric.with(this, new TwitterCore(authConfig), digitsBuilder.build());



//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkForMessageReadPermission();
//            }
//        }, 1000);


        Typeface robotoThin = Typeface.createFromAsset(getBaseContext().getAssets(),
                "font/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getBaseContext().getAssets(),
                "font/Roboto-Light.ttf");
        sharedPreferences = getSharedPreferences("prefs",Context.MODE_PRIVATE);
        baseApp=new BaseApp(this);
        //baseApp.setLocale(sharedPreferences.getString("lang",""));
        setContentView(R.layout.activity_auth);







       // checkForMessageReadPermission();

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson=gsonBuilder.create();
        loginText= (TextView) findViewById(R.id.login_textView);
        loginText.setTypeface(robotoLight);
        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setText(R.string.text_login_with_phone);
        digitsButton.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        //use this.getAssets if you are calling from an Activity
        digitsButton.setTypeface(robotoThin);


        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
                .withAuthCallBack((baseApp).getAuthCallback())
                .withPhoneNumber("+91");

        Digits.authenticate(authConfigBuilder.build());

        digitsButton.setCallback((baseApp).getAuthCallback());

        //checkForMessageReadPermission();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkForMessageReadPermission();
            }
        }, 1000);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    private void checkForMessageReadPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(AuthActivity.this,
                Manifest.permission.READ_SMS);

        if (ContextCompat.checkSelfPermission(AuthActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(AuthActivity.this,
//                    Manifest.permission.READ_SMS)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(AuthActivity.this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // checkForMessageReadPermission();
    }
}
