package mobi.app.saralseva.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.splunk.mint.Mint;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import mobi.app.saralseva.firebase.FirebaseAppUpdater;
import mobi.app.saralseva.models.UserProfile;
import mobi.app.saralseva.utils.SharedPreferenceManager;

/**
 * Created by kumardev on 1/20/2017.
 */

public class BaseApp extends Application {

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    Activity activity;
    String language;
    String locale="en";
    Fragment fragment;
    UserProfile userProfile;
    public static boolean appUpdatedRecently=false;

    private AuthCallback authCallback;
    private static final String TWITTER_KEY = "bW5NdE5KiMOQtBp6lHASoCYZe";
    private static final String TWITTER_SECRET = "gLuulGjyiuUtyqkfuD6hI7LFOeLP8EWZJFgckqO0UoBf52wivl";

    private static String fToken;


    public BaseApp() {
    }


    public static boolean didAppUpdatedRecently(){
        return appUpdatedRecently;
    }


    public BaseApp(final Context context) {
        this.context = context;
        this.activity= (Activity) context;


        Mint.initAndStartSession(context, "56679b9c");

        sharedPreferences = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);

//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(context, new TwitterCore(authConfig), new Digits.Builder().build());

        fToken=FirebaseInstanceId.getInstance().getToken();
        System.out.print("Main Activity Token" + fToken);
        userProfile=new UserProfile();
        String Mobno=SharedPreferenceManager.getInstance(BaseApp.this).getPhoneNumber();
        Log.v("baseApp Cons",Mobno+SharedPreferenceManager.getInstance(BaseApp.this).getPhoneNumber());
//        userProfile.setAadharNum("");
//
//        userProfile.setDob("");
//        userProfile.setAddress("");
//        userProfile.setGender("");
//        userProfile.setPhoneNum(Mobno);
//        SharedPreferenceManager.getInstance(BaseApp.this).setProfilePreferences(userProfile);

       // checkForMessageReadPermission();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkForMessageReadPermission();
//            }
//        }, 1000);

        //generateSSH();






//        authCallback=new AuthCallback() {
//
//
//            @Override
//            public void success(DigitsSession session, String phoneNumber) {
//
//                if (phoneNumber.length()  > 10) {
//                    phoneNumber = phoneNumber.substring(phoneNumber.length()-10, phoneNumber.length());
//                }
//
//
////                editor = sharedPreferences.edit();
////                editor.putString("mobileNumber",phoneNumber);
////                editor.commit();
//
//                SharedPreferenceManager.getInstance(context).setPrimaryPhoneNumber(phoneNumber);
//
//                userProfile.setAadharNum("");
//                userProfile.setDob("");
//                userProfile.setAddress("");
//                userProfile.setGender("");
//                userProfile.setPhoneNum(phoneNumber);
//                SharedPreferenceManager.getInstance(context).setProfilePreferences(userProfile);
//
//
//                Intent i=new Intent(context,MainActivity.class);
//                i.putExtra("mobileNumber",phoneNumber);
//                i.putExtra("from","auth");
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                context.startActivity(i);
//
//            }
//
//            @Override
//            public void failure(DigitsException error) {
//
//            }
//        };




        language=sharedPreferences.getString("lang","");
        if(language.equals("Hindi")){
            locale="hi";
        }else  if(language.equals("English")){
            locale="en";
        }else  if(language.equals("Kannada")){
            locale="kn";
        }
            setLocale(locale);
    }


    private void generateSSH() {
        MessageDigest md=null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        String s=Base64.encodeToString(md.digest(), Base64.DEFAULT);
        Log.i("SecretKey = ",Base64.encodeToString(md.digest(), Base64.DEFAULT));
    }

    private void checkForMessageReadPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_SMS);

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
//                    Manifest.permission.READ_SMS)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
//            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context=this;

        final FirebaseRemoteConfig firebaseRemoteConfig=FirebaseRemoteConfig.getInstance();

        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(FirebaseAppUpdater.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(FirebaseAppUpdater.KEY_CURRENT_VERSION, "1.0.0");
        remoteConfigDefaults.put(FirebaseAppUpdater.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=mobi.app.saralseva&hl=en");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);

        firebaseRemoteConfig.fetch(60).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    firebaseRemoteConfig.activateFetched();
                }
            }
        });







    }



    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setLocale(String lang) {
        String languageToLoad  = lang;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());
    }

    public AuthCallback getAuthCallback(){
        return authCallback;
    }


    public static String getUserToken(){
        return fToken;
    }





}
