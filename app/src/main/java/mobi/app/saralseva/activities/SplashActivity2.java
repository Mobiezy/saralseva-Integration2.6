package mobi.app.saralseva.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import mobi.app.saralseva.R;
import mobi.app.saralseva.utils.SharedPreferenceManager;

public class SplashActivity2 extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private ProgressBar mProgress;
    ImageView imageView;
    SharedPreferences.Editor editor;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
   // private AnimatedSvgView svgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashsecond_xml);
       // svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        imageView=(ImageView)findViewById(R.id.splash) ;
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.tw__solid_white));
        }

             //    checkForMessageReadPermission();


//            }



//        svgView.postDelayed(new Runnable() {
//
//            @Override public void run() {
//               // svgView.start();
//            }
//        }, 500);
      //  mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

        if(getIntent()!=null){
            System.out.print("");
        }

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                doWork();

                startApp();
//                checkForMessageReadPermission();
                finish();
                overridePendingTransition(R.anim.slidein, R.anim.slideout);

            }


        }).start();

//        new Handler().postDelayed(new Runnable() {
//
//            /*
//             * Showing splash screen with a timer. This will be useful when you
//             * want to show case your app logo / company
//             */
//
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
//                Intent i = new Intent(SplashActivity.this, AuthActivity.class);
//                startActivity(i);
//
//                // close this activity
//                finish();
//            }
//        }, SPLASH_TIME_OUT);
    }


    private void doWork() {



        for (int progress=0; progress<=10; progress+=5) {
            try {
                Thread.sleep(1000);
            //    mProgress.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void startApp() {
//        checkForMessageReadPermission();

        String num= SharedPreferenceManager.getInstance(this).getPhoneNumber();
        if(num.length()==0){
          //  checkForMessageReadPermission();
            Intent intent = new Intent(SplashActivity2.this, AuthActivity_firebase.class);
            startActivity(intent);
        }else{
          //  checkForMessageReadPermission();
            Intent i=new Intent(SplashActivity2.this,MainActivity.class);
            i.putExtra("mobileNumber",num);
            i.putExtra("from","auth");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


//            Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
//            startActivity(intent);



    }


    private void checkForMessageReadPermission()
    {
        Log.v("pigmy","check permission");
        int permissionCheck = ContextCompat.checkSelfPermission(SplashActivity2.this,
                Manifest.permission.READ_SMS);

        if (ContextCompat.checkSelfPermission(SplashActivity2.this,
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

            ActivityCompat.requestPermissions(SplashActivity2.this,
                    new String[]{Manifest.permission.READ_SMS,Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.SYSTEM_ALERT_WINDOW},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//            }
        }

    }
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

}

