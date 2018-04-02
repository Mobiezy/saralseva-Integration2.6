package mobi.app.saralseva.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.jrummyapps.android.widget.AnimatedSvgView;

import mobi.app.saralseva.R;
import mobi.app.saralseva.utils.SharedPreferenceManager;

public class SplashActivity extends Activity {

    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private ProgressBar mProgress;
    private AnimatedSvgView svgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.tw__solid_white));
        }

        svgView.postDelayed(new Runnable() {

            @Override public void run() {
                svgView.start();
            }
        }, 500);
      //  mProgress = (ProgressBar) findViewById(R.id.splash_screen_progress_bar);

        if(getIntent()!=null){
            System.out.print("");
        }

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                doWork();

                startApp();
                finish();
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
            Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
            startActivity(intent);
        }else{

            Intent i=new Intent(SplashActivity.this,MainActivity.class);
            i.putExtra("mobileNumber",num);
            i.putExtra("from","auth");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


//            Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
//            startActivity(intent);



    }


        private void checkForMessageReadPermission() {
            int permissionCheck = ContextCompat.checkSelfPermission(SplashActivity.this,
                    Manifest.permission.READ_SMS);

            if (ContextCompat.checkSelfPermission(SplashActivity.this,
                    Manifest.permission.READ_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
//                        Manifest.permission.READ_SMS)) {
//
//                    // Show an explanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//
//                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[]{Manifest.permission.READ_SMS},
                            MY_PERMISSIONS_REQUEST_READ_SMS);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
//                }
            }
        }
    }

