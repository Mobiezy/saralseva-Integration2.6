package mobi.app.saralseva.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import mobi.app.saralseva.firebase.FirebaseAppUpdater;
import mobi.app.saralseva.fragments.AddNewVendorFragment;
import mobi.app.saralseva.fragments.HomeFragment;
import mobi.app.saralseva.fragments.MoreFragment;
import mobi.app.saralseva.fragments.NotifyFragment;
import mobi.app.saralseva.fragments.ProfileFragment;
import mobi.app.saralseva.models.GlobalAdd;
import mobi.app.saralseva.models.Login;
import mobi.app.saralseva.models.LoginResponse;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils_view.HorizontalAdapter;
import mobi.app.saralseva.R;
import mobi.app.saralseva.services.FetchAddressIntentService;
import mobi.app.saralseva.utils.AlertManager;
import mobi.app.saralseva.utils.ConnectionDetector;
import mobi.app.saralseva.utils.Constants;
import mobi.app.saralseva.utils.GPSLocation;
import mobi.app.saralseva.utils_view.RecyclerSubcriptionView;
import mobi.app.saralseva.utils_view.SubcriptionGrid;


public class MainActivity extends AppCompatActivity implements

        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        FirebaseAppUpdater.OnUpdateNeededListener{
    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    FrameLayout masterFrameLayout;
    private List<GlobalAdd> data;
    private static final String TAG = "Login";
    ImageView imageView;
    ConnectionDetector connectionDetector;
    GPSLocation gpsLocation;
    Location myCurrentLocation;
    Location mCurrentLocation;

    TextView txtLatitude;
    TextView txtLongitude;
    FloatingActionButton fab;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

    private AlertManager alertManager;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private GeoAddressReceiver geoAddressReceiver;
    private String geoData;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;

    private RecyclerView recyclerView;
    private RecyclerSubcriptionView adapter;
    private List<SubcriptionGrid> albumList;

    private Gson gson;
    View baseView;

    JSONObject params = new JSONObject();

//    public static final String ENDPOINT = "http://saralseva.us-west-2.elasticbeanstalk.com/rest/login/get";

    private RequestQueue requestQueue;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String loginData;
    private String loginMobileNumber;

    JSONObject jsonObject;

    ProgressDialog progressDialog;

    LinearLayout linLayHome, linLayProfile, linLayNotify, linLayMore;
    ImageView IVhome, IVprofile, IVnotify, IVmore;
    TextView TVhome, TVprofile, TVnotify, TVmore, subName, vendorName;
    private View toolbar_items;
    private TextView txtLocation;
    private ImageView imgLocation;
    private Locale locale;
    BaseApp baseApp;

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    private String notifText;

    SwipeRefreshLayout swipeRefreshLayout;
    private String APP="Mobiezy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
        Typeface robotoThin = Typeface.createFromAsset(getBaseContext().getAssets(),
                "font/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getBaseContext().getAssets(),
                "font/Roboto-Light.ttf");
        ImageView subscptionImage;
        TextView subName, vendorName;
        //subscptionImage = (ImageView) findViewById(R.id.thumbnail);

        //sharedPreferences = getSharedPreferences("prefs",Context.MODE_PRIVATE);

        //baseApp.setLocale(sharedPreferences.getString("lang",""));
        // baseApp.setLocale();
        baseApp = new BaseApp(this);



        setContentView(R.layout.activity_main);
        masterFrameLayout = (FrameLayout) findViewById(R.id.masterpage);
        txtLocation = (TextView) findViewById(R.id.tvLocation);
        txtLocation.setTypeface(robotoLight);
        imgLocation = (ImageView) findViewById(R.id.ivLocator);

        imgLocation.setOnClickListener(this);

        gpsLocation = new GPSLocation(this);
        gpsLocation.getCurrentLocation();


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();


        connectionDetector = new ConnectionDetector(this);

//        // gpsLocation=new GPSLocation(this);
//
        alertManager = new AlertManager(this);
        geoAddressReceiver = new GeoAddressReceiver(new Handler(), this);
        baseView = findViewById(R.id.main_content);
        createGoogleApi();
        checkForMessageReadPermission();

        if (getIntent() != null) {

            String fromActivity = getIntent().getStringExtra("from");
            if (fromActivity != null) {
                if (fromActivity.equals("auth")) {
                    String phoneNum = getIntent().getStringExtra("mobileNumber");
                    Snackbar.make(baseView, getString(R.string.text_login_auth_success), Snackbar.LENGTH_LONG).show();
                }
            }




        }



//        toolbar_items=findViewById(R.id.toolbar_items);
//        txtLocation=(TextView) toolbar_items.findViewById(R.id.tvLocation);
//        imgLocation=(ImageView) toolbar_items.findViewById(R.id.ivLocator);
//        imgLocation.setOnClickListener(this);


        linLayHome = (LinearLayout) findViewById(R.id.linLayHome);
        linLayProfile = (LinearLayout) findViewById(R.id.linLayProfile);
        linLayNotify = (LinearLayout) findViewById(R.id.linLayNotify);
        linLayMore = (LinearLayout) findViewById(R.id.linLayMore);

        IVhome = (ImageView) findViewById(R.id.ivHome);
        IVprofile = (ImageView) findViewById(R.id.ivProfile);
        IVnotify = (ImageView) findViewById(R.id.ivNotify);
        IVmore = (ImageView) findViewById(R.id.ivMore);

        TVhome = (TextView) findViewById(R.id.tvHome);
        TVprofile = (TextView) findViewById(R.id.tvProfile);
        TVnotify = (TextView) findViewById(R.id.tvNotify);
        TVmore = (TextView) findViewById(R.id.tvMore);

        TVhome.setTypeface(robotoLight);
        TVprofile.setTypeface(robotoLight);
        TVnotify.setTypeface(robotoLight);
        TVmore.setTypeface(robotoLight);

        View bottomView = findViewById(R.id.bottomview);
        ColorStateList csl = AppCompatResources.getColorStateList(getApplication(), R.color.colorPrimary);
        resetTintColor();
        TVhome.setTextColor(getResources().getColor(R.color.menu_color));
        TVhome.setTextSize(12);
        Drawable drawableIVhome = DrawableCompat.wrap(IVhome.getDrawable());
        DrawableCompat.setTintList(drawableIVhome, csl);
        IVhome.setImageDrawable(drawableIVhome);
        TVhome.setTextColor(getResources().getColor(R.color.colorPrimary));
        TVhome.setTextSize(13);

        linLayHome.setOnClickListener(this);
        linLayProfile.setOnClickListener(this);
        linLayNotify.setOnClickListener(this);
        linLayMore.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
//                            .build();
//
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                                    .setFilter(typeFilter)
//                                    .build(MainActivity.this);
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    // TODO: Handle the error.
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    // TODO: Handle the error.
//                }
//            }
//        });


        // Based on where user has clicked to open the app, logic is described below [ Notification or from the app launcher ]

        if(getIntent().getStringExtra("notificationFrag")!=null){
            FragmentManager fragmentManager = getFragmentManager();
            NotifyFragment notifyFragment = new NotifyFragment();


            String str=getIntent().getStringExtra("notificationFrag");


//            ColorStateList csl = AppCompatResources.getColorStateList(getApplication(), R.color.colorPrimary);
//            resetTintColor();
            //fragmentManager.beginTransaction().replace(R.id.masterpage, notifyFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

            Bundle bundle=new Bundle();
            bundle.putString("msg",str);
            notifyFragment.setArguments(bundle);
            masterFrameLayout.removeAllViews();
            fragmentManager.beginTransaction().add(R.id.masterpage, notifyFragment).commit();
            resetTintColor();
            TVhome.setTextColor(getResources().getColor(R.color.menu_color));
            TVhome.setTextSize(12);
            Drawable drawableIVnotify = DrawableCompat.wrap(IVnotify.getDrawable());
            DrawableCompat.setTintList(drawableIVnotify, csl);
            IVnotify.setImageDrawable(drawableIVnotify);
            TVnotify.setTextColor(getResources().getColor(R.color.colorPrimary));
            TVnotify.setTextSize(13);
            fab.setVisibility(View.GONE);
            swipeRefreshLayout.setEnabled(false);
        }else {


            FragmentManager fragmentManager = getFragmentManager();
            HomeFragment homeMenuFragment = new HomeFragment();
            masterFrameLayout.removeAllViews();
            fragmentManager.beginTransaction().add(R.id.masterpage, homeMenuFragment).commit();

        }


        swipeRefreshLayout.setOnRefreshListener(this);
        onUpdateNeeded("https://play.google.com/store/apps/details?id=mobi.app.saralseva&hl=en");
        FirebaseAppUpdater.with(this).onUpdateNeeded(this).check();

    }

    private void checkForMessageReadPermission()
    {
        Log.v(TAG,"check permission");
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
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

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_SMS,Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.SYSTEM_ALERT_WINDOW},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//            }
        }

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = PlaceAutocomplete.getPlace(this, data);
//                Log.i(TAG, "Place: " + place.getName());
//                txtLongitude.setText(place.getAddress());
//            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                Status status = PlaceAutocomplete.getStatus(this, data);
//                // TODO: Handle the error.
//                Log.i(TAG, status.getStatusMessage());
//
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//    }


    private void createGoogleApi() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        googleApiClient.disconnect();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if(count==0){
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
            {
                super.onBackPressed();
                return;
            }
            else { Toast.makeText(getBaseContext(), R.string.toast_backpress, Toast.LENGTH_SHORT).show(); }

            mBackPressed = System.currentTimeMillis();
        }else{
            getFragmentManager().popBackStack();
        }



    }

    @Override
    protected void onResume() {
        super.onResume();

//        System.out.print("Main Activity Token" + FirebaseInstanceId.getInstance().getToken());
        System.out.print("");



        gpsLocation.getCurrentLocation();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // connectionDetector.isConnectedToInternet();

        if (!isGPSEnabled) {
            // no network provider is enabled
            showSettingsAlert();

            //alertManager.showAlertDialog(context, "Network Info", "GPS or NETWORK not available", false);

        }


        int resp = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (resp != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, resp, 1).show();
        } else {
            Log.d(TAG, "google play services available");
        }

        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }

        //startLocationUpdates();

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


//        FragmentManager fragmentManager=getFragmentManager();
//        HomeFragment homeMenuFragment =new HomeFragment();
//
//        fragmentManager.beginTransaction().replace(R.id.masterpage, homeMenuFragment).commit();


    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);


        alertDialog.setTitle(R.string.message_gps_setting);


        alertDialog
                .setMessage(R.string.message_gps_msg);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.message_settings_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.message_setting_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }
    private void onLanguageChanged(Configuration conf) {

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        checkForMessageReadPermission();
        // Get last known recent location.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED  &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                System.out.print("");
//
//            } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.SYSTEM_ALERT_WINDOW},
                    0);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            //}
        }
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            //Toast.makeText(this, "from on connected", Toast.LENGTH_SHORT).show();
            startIntentService();
            //getLoginData();

        }
        // Begin polling for new location updates.


    }

    private boolean startIntentService() {


        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }

        // Toast.makeText(this, "start intent", Toast.LENGTH_SHORT).show();

        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED  &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                checkForMessageReadPermission();
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, geoAddressReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mCurrentLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);

        return (mCurrentLocation!=null);
    }

    private void startLocationUpdates() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED  && ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED  &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    0);
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.SYSTEM_ALERT_WINDOW},
                    0);
            checkForMessageReadPermission();
        }
        if(googleApiClient.isConnected())
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest, this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(baseView, "Location access permitted", Snackbar.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED  &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    if (mCurrentLocation != null) {
                        // Print current location if not null
                        Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
                        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                        // Toast.makeText(this, "on request permission", Toast.LENGTH_SHORT).show();
                        startIntentService();

                    }
                    startLocationUpdates();

                } else {

                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Snackbar.make(baseView,  R.string.toast_disconnect_please_reconnect, Snackbar.LENGTH_SHORT).show();

        } else if (i == CAUSE_NETWORK_LOST) {
            Snackbar.make(baseView, R.string.toast_network_lost, Snackbar.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        String msg = getString(R.string.message_updated_location) +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        //Snackbar.make(baseView,msg, Snackbar.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

    }

    public double getLatitude(){
        return mCurrentLocation.getLatitude();
    }


    public double getLongitude(){
        return mCurrentLocation.getLongitude();
    }

    public void resetTintColor(){
        IVhome.setImageResource(R.drawable.ic_homesvg);
        IVprofile.setImageResource(R.drawable.ic_profilesvg);
        IVnotify.setImageResource(R.drawable.ic_notifysvg);
        IVmore.setImageResource(R.drawable.ic_moresvg);
        ColorStateList cslReset = AppCompatResources.getColorStateList(getApplication(), R.color.colorPrimaryDark);
        Drawable drawableIVhomeR = DrawableCompat.wrap(IVhome.getDrawable());
        Drawable drawableIVprofileR = DrawableCompat.wrap(IVprofile.getDrawable());
        Drawable drawableIVnotifyR = DrawableCompat.wrap(IVnotify.getDrawable());
        Drawable drawableIVmoreR = DrawableCompat.wrap(IVmore.getDrawable());
        DrawableCompat.setTintList(drawableIVhomeR, cslReset);
        DrawableCompat.setTintList(drawableIVprofileR, cslReset);
        DrawableCompat.setTintList(drawableIVnotifyR, cslReset);
        DrawableCompat.setTintList(drawableIVmoreR, cslReset);

        IVhome.setImageDrawable(drawableIVhomeR);
        IVprofile.setImageDrawable(drawableIVprofileR);
        IVnotify.setImageDrawable(drawableIVnotifyR);
        IVmore.setImageDrawable(drawableIVmoreR);

    }
    @Override
    public void onClick(View view) {
        FragmentManager fragmentManager=getFragmentManager();
        ColorStateList csl = AppCompatResources.getColorStateList(getApplication(), R.color.colorPrimary);

        switch (view.getId()){
            case R.id.ivLocator:
                boolean b=startIntentService();
                if(b){
                    Snackbar.make(view, R.string.location_refresh, Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(view, R.string.location_detection_error, Snackbar.LENGTH_SHORT).show();
                }

                //   Toast.makeText(this, "Location Refreshed..", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linLayHome:
                HomeFragment homeMenuFragment =new HomeFragment();
                masterFrameLayout.removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.masterpage, homeMenuFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                resetTintColor();
                Drawable drawableIVhome = DrawableCompat.wrap(IVhome.getDrawable());
                DrawableCompat.setTintList(drawableIVhome, csl);
                IVhome.setImageDrawable(drawableIVhome);
                TVhome.setTextColor(getResources().getColor(R.color.colorPrimary));
                TVhome.setTextSize(13);
                TVprofile.setTextColor(getResources().getColor(R.color.menu_color));
                TVprofile.setTextSize(12);
                TVnotify.setTextColor(getResources().getColor(R.color.menu_color));
                TVnotify.setTextSize(12);
                TVmore.setTextColor(getResources().getColor(R.color.menu_color));
                TVmore.setTextSize(12);
                fab.setVisibility(view.VISIBLE);
                swipeRefreshLayout.setEnabled(true);
                break;
            case R.id.linLayProfile:
                ProfileFragment profileFragment =new ProfileFragment();
                resetTintColor();
                masterFrameLayout.removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.masterpage, profileFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                TVhome.setTextColor(getResources().getColor(R.color.menu_color));
                TVhome.setTextSize(12);
                Drawable drawableIVprofile = DrawableCompat.wrap(IVprofile.getDrawable());
                DrawableCompat.setTintList(drawableIVprofile, csl);
                IVprofile.setImageDrawable(drawableIVprofile);
                TVprofile.setTextColor(getResources().getColor(R.color.colorPrimary));
                TVprofile.setTextSize(13);
                TVnotify.setTextColor(getResources().getColor(R.color.menu_color));
                TVnotify.setTextSize(12);
                TVmore.setTextColor(getResources().getColor(R.color.menu_color));
                TVmore.setTextSize(12);
                fab.setVisibility(view.GONE);
                swipeRefreshLayout.setEnabled(false);
                break;
            case R.id.linLayNotify:
                NotifyFragment notifyFragment =new NotifyFragment();
                resetTintColor();
                masterFrameLayout.removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.masterpage, notifyFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                TVhome.setTextColor(getResources().getColor(R.color.menu_color));
                TVhome.setTextSize(12);
                TVprofile.setTextColor(getResources().getColor(R.color.menu_color));
                TVprofile.setTextSize(12);
                Drawable drawableIVnotify = DrawableCompat.wrap(IVnotify.getDrawable());
                DrawableCompat.setTintList(drawableIVnotify, csl);
                IVnotify.setImageDrawable(drawableIVnotify);
                TVnotify.setTextColor(getResources().getColor(R.color.colorPrimary));
                TVnotify.setTextSize(13);
                TVmore.setTextColor(getResources().getColor(R.color.menu_color));
                TVmore.setTextSize(12);
                fab.setVisibility(view.GONE);
                swipeRefreshLayout.setEnabled(false);
                break;
            case R.id.linLayMore:
                MoreFragment moreFragment =new MoreFragment();
                resetTintColor();
                masterFrameLayout.removeAllViews();
                fragmentManager.beginTransaction().replace(R.id.masterpage, moreFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                TVhome.setTextColor(getResources().getColor(R.color.menu_color));
                TVhome.setTextSize(12);
                TVprofile.setTextColor(getResources().getColor(R.color.menu_color));
                TVprofile.setTextSize(12);
                TVnotify.setTextColor(getResources().getColor(R.color.menu_color));
                TVnotify.setTextSize(12);
                Drawable drawableIVmore = DrawableCompat.wrap(IVmore.getDrawable());
                DrawableCompat.setTintList(drawableIVmore, csl);
                IVmore.setImageDrawable(drawableIVmore);
                TVmore.setTextColor(getResources().getColor(R.color.colorPrimary));
                TVmore.setTextSize(13);
                fab.setVisibility(view.GONE);
                swipeRefreshLayout.setEnabled(false);
                break;
            case R.id.floatingActionButton:
                FragmentManager fabfragmentManager=getFragmentManager();
                AddNewVendorFragment addNewVendorFragment=AddNewVendorFragment.newInstance(getString(R.string.header_add_vendor));
                addNewVendorFragment.show(fabfragmentManager,"");
                break;


        }
    }

    @Override
    public void onRefresh() {
        if(BaseApp.isNetworkAvailable(this)) {

            startIntentService();
            editor.remove("jsonData");
            editor.commit();

            FragmentManager fragmentManager = getFragmentManager();
            HomeFragment homeMenuFragment = new HomeFragment();
            masterFrameLayout.removeAllViews();
            fragmentManager.beginTransaction().replace(R.id.masterpage, homeMenuFragment).commit();
            swipeRefreshLayout.setRefreshing(false);

        }else{
            AlertManager.showAlertDialog(this,getString(R.string.alert_header_internet),getString(R.string.alert_desc_internet),false);
            swipeRefreshLayout.setRefreshing(false);
        }

    }



    @Override
    public void onUpdateNeeded(final String updateUrl) {
        Log.v(APP,"on update needed");

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.alert_header_title_new_version)
                .setMessage(R.string.alert_desc_new_version)
                .setPositiveButton(R.string.alert_new_version_option_update,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton(R.string.alert_new_version_option_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();

    }

    private void redirectStore(String updateUrl) {
        Log.v(APP,"redirect store");
// if shared prefernce not get
        //context.getSharedPreferences("YOUR_PREFS", 0).edit().clear().commit();
//        String s = sharedPreferences.getString("jsonData", null);
//        Log.v(APP,"prefence"+s);
//       // SharedPreferences settings = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
//        editor.remove("jsonData").commit();
//        String s1 = sharedPreferences.getString("jsonData", null);
//        Log.v(APP,"after erase prefence"+s1);
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public class GeoAddressReceiver extends ResultReceiver {

        private Context context;

        public GeoAddressReceiver(Handler handler, Context ctx) {
            super(handler);
            context=ctx;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);

            geoData=resultData.getString(Constants.RESULT_DATA_KEY);
            txtLocation.setText(geoData);
            //Toast.makeText(context, "geo: "+geoData, Toast.LENGTH_SHORT).show();




            if (resultCode == 0) {
                //Toast.makeText(context,"Address found",Toast.LENGTH_LONG).show();

            }

        }
    }






}
