package mobi.app.saralseva.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.MainActivity;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by kumardev on 12/10/2016.
 */

public class GPSLocation implements LocationListener {

    private Context context;

    AlertManager alertManager;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    Location location = null;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;

    private static final long MIN_TIME_BW_UPDATES = 0;

    LocationManager locationManager;
    private ConnectionDetector connectionDetector;


    public GPSLocation(Context ctx) {
        context = ctx;
        alertManager = new AlertManager(context);

        connectionDetector = new ConnectionDetector(context);
        // getCurrentLocation();
    }


    public Location getCurrentLocation() {

        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            isNetworkEnabled = connectionDetector.isConnectedToInternet();

//            if (!isGPSEnabled) {
//                // no network provider is enabled
//                showSettingsAlert();
//
//                //alertManager.showAlertDialog(context, "Network Info", "GPS or NETWORK not available", false);
//
//            }

//            if (!isNetworkEnabled) {
//                // no network provider is enabled
//               // showSettingsAlert();
//                alertManager.showAlertDialog(context, "Network Info", "NETWORK not available", false);
//
//            }
            //this.canGetLocation = true;
            if (isNetworkEnabled) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {
//                        googleMap.setMyLocationEnabled(true);
//                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    System.out.print("");

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            0);
                    System.out.print("");

                }


                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                Log.d("Network", "Network Enabled");
                if (locationManager != null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }else

            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    Log.d("GPS", "GPS Enabled");
                    if (locationManager != null) {

                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }

            if (location == null) {

                //location = getLastKnownLocation();
                locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, false);

//                List<String> providers = locationManager.getProviders(true);
//
//                for (int i=providers.size()-1; i>=0; i--) {
//                    location = locationManager.getLastKnownLocation(providers.get(i));
//                    if (location != null) break;
//                }

               location= locationManager.getLastKnownLocation(bestProvider);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return ;
            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    public double getLatitude(){
        if(location!=null){
            latitude=location.getLatitude();
        }
        return latitude;
    }


    public double getLongitude(){
        if(location!=null){
            latitude=location.getLongitude();
        }
        return latitude;
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        alertDialog.setTitle(R.string.message_gps_setting);


        alertDialog
                .setMessage(R.string.message_gps_msg);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.message_settings_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
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


    @Override
    public void onLocationChanged(Location location) {
        System.out.print("");
        //context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        System.out.print("");
    }

    @Override
    public void onProviderEnabled(String s) {
        System.out.print("");

    }

    @Override
    public void onProviderDisabled(String s) {
        System.out.print("");
    }
}
