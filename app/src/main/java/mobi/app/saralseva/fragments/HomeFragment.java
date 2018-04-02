package mobi.app.saralseva.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.BaseApp;
import mobi.app.saralseva.activities.MainActivity;
import mobi.app.saralseva.data.DataOperation;
import mobi.app.saralseva.models.GlobalAdd;
import mobi.app.saralseva.models.Login;
import mobi.app.saralseva.models.LoginResponse;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.models.UserProfile;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils.GPSLocation;
import mobi.app.saralseva.utils_view.HorizontalAdapter;
import mobi.app.saralseva.utils_view.RecyclerSubcriptionView;
import mobi.app.saralseva.utils_view.SubcriptionGrid;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

//public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<GlobalAdd> data;
    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    private RecyclerSubcriptionView adapter;
    private List<SubcriptionGrid> albumList;
    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;
    private Gson gson;

    JSONObject params = new JSONObject();

    //private static final String ENDPOINT = "http://saralseva.us-west-2.elasticbeanstalk.com/rest/login/get";

    private RequestQueue requestQueue;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private String loginData;
    private String loginMobileNumber;

    JSONObject jsonObject;
    JSONArray jsonArrayVendors ;
    JSONArray jsonArrayVendorsType;
    JSONArray jsonArrayCustomerId;
    JSONArray jsonArrayVendorId;

    JSONArray jsonArrayCustomerName;
    JSONArray jsonArrayMerchantKey;
    JSONArray jsonArrayMerchantKeys;




    ProgressDialog progressDialog;

    double currentLatitude;
    double currentLongitude;

    ImageView subImage,noData;
    TextView subTitle,subSubTitle,yourSub;

    TextView txtNoSubscription;
    private LinearLayout linearLayout;
    private View subLayout;

    MainActivity mainActivity;

    DataOperation dataOperation;
    List<LoginResponse> loginResponses;
    private FloatingActionButton fab;

    private JSONObject secondaryVendors;
    private JSONObject vendorObject;
    private JSONArray alVendorsArray;
    private JSONObject alVendorsObject;
    private JSONArray jsonDataArray;
    UserProfile userProfile;

    String s_mobNum,s_customerName,s_merchantId,s_vendorName,s_vendorType,s_customerId,s_vendorId;

    boolean primaryDataShown=false;
    private View mainContent;

    SwipeRefreshLayout swipeRefreshLayout;
    private boolean noVendors=false;
    private boolean isSecondaryDataPresent=false;
    private String App="Mobiezy";

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Typeface robotoThin = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Light.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Regular.ttf");


        txtNoSubscription = (TextView) getView().findViewById(R.id.txtNoSubscription);


//        fabBtn=(FloatingActionButton) getView().findViewById(R.id.fabHomeBtn);
//        fabBtn.setOnClickListener(this);
//        fab = (FloatingActionButton) getView().findViewById(R.id.floatingActionButton);
//        //   fabBtn=(FloatingActionButton) getView().findViewById(R.id.fabHomeBtn);
//        fab.setOnClickListener(this);

        //   fabBtn=(FloatingActionButton) getView().findViewById(R.id.fabHomeBtn);


        yourSub = (TextView) getView().findViewById(R.id.textViewYourSub);
        yourSub.setTypeface(robotoRegular);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        noData = (ImageView) getView().findViewById(R.id.imageViewNodata);



        progressDialog = new ProgressDialog(getActivity());

        albumList = new ArrayList<>();
        adapter = new RecyclerSubcriptionView(getActivity(), albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new HomeFragment.GridSpacingItemDecoration(2, dpToPx(2), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



//        MobileAds.initialize(getActivity(), "ca-app-pub-9075533456353775~4753121441");

        MobileAds.initialize(getActivity(), "ca-app-pub-2023478389400445~9371036614");
       // ca-app-pub-2023478389400445/6277969419

        AdView mAdView = (AdView) getView().findViewById(R.id.adView);

        // AdRequest adRequest = new AdRequest.Builder().build();
//  AdRequest adRequest = new AdRequest.Builder().addTestDevice("38809E113B4E9A2136BBE0998D60101F").build();
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);
//mAdView.setAdListener(new AdListener(){
//
//
//
//    @Override
//    public void onAdClosed() {
//        super.onAdClosed();
//        Log.v(App,""+1)
//    }
//
//    @Override
//    public void onAdFailedToLoad(int i) {
//        super.onAdFailedToLoad(i);
//    }
//
//    @Override
//    public void onAdLeftApplication() {
//        super.onAdLeftApplication();
//    }
//
//    @Override
//    public void onAdOpened() {
//        super.onAdOpened();
//    }
//
//    @Override
//    public void onAdLoaded() {
//        super.onAdLoaded();
//    }
//
//    @Override
//    public void onAdClicked() {
//        super.onAdClicked();
//    }
//
//    @Override
//    public void onAdImpression() {
//        super.onAdImpression();
//    }
//});




        //horizontal_recycler_view= (RecyclerView) getView().findViewById(R.id.horizontal_recycler_view);

//        data = fill_with_data();
//
//        horizontalAdapter=new HorizontalAdapter(data, getActivity());
//        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
//        horizontal_recycler_view.setAdapter(horizontalAdapter);


        sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loginMobileNumber = sharedPreferences.getString("mobileNumber", "");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();


        GPSLocation gpsLocation = new GPSLocation(getActivity());
        gpsLocation.getCurrentLocation();
        currentLatitude = gpsLocation.getLatitude();
        currentLongitude = gpsLocation.getLongitude();

        loginResponses = new ArrayList<LoginResponse>();
        dataOperation = new DataOperation(getActivity());


        alVendorsArray = new JSONArray();
        alVendorsObject = new JSONObject();
        jsonDataArray = new JSONArray();

        userProfile=new UserProfile();
//        userProfile.setGender("");
//        userProfile.setFirstname("");
//        //userProfile.setLastname("");
//        userProfile.setDob("");
//        userProfile.setAddress("");
//        //userProfile.setMailId("");
//        userProfile.setPhoneNum("");
//        userProfile.setAadharNum("");
//
//        SharedPreferenceManager.getInstance(getActivity()).setProfilePreferences(userProfile);

        if(loginMobileNumber!=null){
            userProfile.setPhoneNum(loginMobileNumber);


            if(BaseApp.didAppUpdatedRecently()){
                getLoginData();
            }else {


                if (BaseApp.isNetworkAvailable(getActivity())) {
                    String s = sharedPreferences.getString("jsonData", null);
                    // Commented for Pilot
                    //String str = sharedPreferences.getString("jsonSecondaryData", "");
                    if (s != null) {
                        if (!s.equals("")) {
                            showOfflineData();
                        }


                    } else {
                        getLoginData();

                        // commented for pilot

//                    if(!str.equals("")){
//                        isSecondaryDataPresent=true;
//                        showSecondaryVendors(str);
//                    }
                    }
                } else {
                    showOfflineData();
                }
            }

        }


//        if (loginMobileNumber != null) {
//
//            userProfile.setPhoneNum(loginMobileNumber);
//
//            if (BaseApp.isNetworkAvailable(getActivity())) {
//                String s = sharedPreferences.getString("jsonData", null);
//                if (s == null) {
//
//                    getLoginData();
//                    String str = sharedPreferences.getString("jsonSecondaryData", "");
//                    if (str.length() > 0) { // if there are secondary vendors
//
//
//                        noVendors=false;
//                        showSecondaryVendors(str);
//
//
////                    }
//                    }else{
//                        noVendors=true;
//                    }
//                }
//                else if(s.length()>0 || !s.equals("")) {
//
//
////                    if (s.length() == 0 || s.equals("")) { // if there is no primary data stored locally
////
////                        getLoginData();
//
////                    String str = sharedPreferences.getString("jsonSecondaryData", "");
////                    if (str.length() > 0) { // if there are secondary vendors
////
////
////                        showSecondaryVendors(str);
////
//////                    }
////                    }
//
//
//
//                   // } else {
////                String str = sharedPreferences.getString("jsonSecondaryData", "");
////                if (str.length() > 0) {
////
////                    txtNoSubscription.setVisibility(View.GONE);
////                    noData.setVisibility(View.GONE);
////                    showSecondaryVendors(str);
////                }else{
////                    txtNoSubscription.setVisibility(View.VISIBLE);
////                    yourSub.setVisibility(View.GONE);
////                    noData.setVisibility(View.VISIBLE);
////                    if (progressDialog.isShowing())
////                        progressDialog.dismiss();
////                }
//                        showOfflineData();
//                    }
//                }else{
//                    showOfflineData();
//                }
//            if(noVendors){
//                txtNoSubscription.setVisibility(View.VISIBLE);
//                noData.setVisibility(View.VISIBLE);
//            }
//            }


        // call this method here..It will be done.. I guess
        //this i resolved actually screen overlay prblm was here



    }


    private void showOfflinePrimaryData() {
        String s = sharedPreferences.getString("jsonData", "");

        if(!primaryDataShown){
            txtNoSubscription.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            showDataFromDB(s);

        }
    }

    private void showOfflineData() {
        String s = sharedPreferences.getString("jsonData", "");

        if(!primaryDataShown && !s.equals("")){
            txtNoSubscription.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            showDataFromDB(s);

        }


// commented for pilot

//        String str = sharedPreferences.getString("jsonSecondaryData", "");
//        if (str.length() > 0) {
//            isSecondaryDataPresent=true;
//            txtNoSubscription.setVisibility(View.GONE);
//            noData.setVisibility(View.GONE);
//            showSecondaryVendors(str);
//        }
    }

    private void showSecondaryVendors(String str) {




        JSONArray jsonArray=null;
        JSONObject jsonObject= null;
        try {
//            jsonObject = new JSONObject(str);
            jsonArray=new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int[] covers = new int[]{
//                R.drawable.ic_cableservices,
//                R.drawable.car_services,
//                R.drawable.news_services,
//                R.drawable.ic_milkservices,

                R.drawable.cable_services,
                R.drawable.car_services,
                R.drawable.news_services,
                R.drawable.milk_services,
                R.drawable.gym_services
        };

        assert jsonArray != null;
        for (int j = 0; j<jsonArray.length(); j++) {

            try {
                jsonObject=jsonArray.getJSONObject(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray vendorArray=null;
            JSONArray customerIdArray=null;
            JSONArray vendorIdArray=null;
            JSONArray customerNameArray=null;
            JSONArray merchantKeyArray=null;

            try {
                vendorArray=jsonObject.getJSONArray("vendorArray");
                customerIdArray=jsonObject.getJSONArray("customerIdArray");
                vendorIdArray=jsonObject.getJSONArray("vendorIdArray");
                customerNameArray=jsonObject.getJSONArray("customerNameArray");
                merchantKeyArray=jsonObject.getJSONArray("merchantKeyArray");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(vendorArray==null){
                //Toast.makeText(getActivity(), "There are no vendors currently subscribed", Toast.LENGTH_SHORT).show();
                txtNoSubscription.setVisibility(View.VISIBLE);
                yourSub.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }else {
                txtNoSubscription.setVisibility(View.GONE);
                noData.setVisibility(View.GONE);
                yourSub.setVisibility(View.VISIBLE);

                JSONArray vendorTypeArray = null;
                try {
                    vendorTypeArray = jsonObject.getJSONArray("vendorTypeArray");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                int numOfSubscribers = vendorTypeArray.length();

                LoginResponse loginResponse;
                for (int i = 0; i < numOfSubscribers; i++) {
                    SubcriptionGrid subcriptionGrid = null;
                    try {
                        // if (vendorTypeArray.getString(i).equals(getResources().getString(R.string.type_cabletv))) {
                        if (vendorTypeArray.getString(i).equals("CABLE TV")) {
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"));
                            //subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"));
                            FirebaseMessaging.getInstance().subscribeToTopic("operator_cable");
                            FirebaseMessaging.getInstance().subscribeToTopic("operator_cable_"+vendorArray.getString(i).replaceAll("\\s",""));
                            // } else if (vendorTypeArray.getString(i).equals(getResources().getString(R.string.type_interet))) {
                        } else if (vendorTypeArray.getString(i).equals("INTERNET")) {
//                        FirebaseMessaging.getInstance().subscribeToTopic("/topics/INTERNET");
                            FirebaseMessaging.getInstance().subscribeToTopic("operator_internet");
                            FirebaseMessaging.getInstance().subscribeToTopic("operator_internet_"+vendorArray.getString(i).replaceAll("\\s",""));
                            //subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[1], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    albumList.add(subcriptionGrid);


                }


                adapter.notifyDataSetChanged();
            }
            //   }

            if (progressDialog.isShowing())
                progressDialog.dismiss();

        }


    }
//    }


    private void getLoginData() {




        try {
            params.put("mobileNumber", loginMobileNumber);
            params.put("longitude",currentLatitude);
            params.put("latitude",currentLongitude);
            params.put("fireBaseToken",BaseApp.getUserToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue= RequestSingletonClass.getInstance(getActivity()).getRequestQueue();

        // Cache.Entry entry = requestQueue.getCache().get(Endpoints.LOGIN_DATA);
        //Cache.Entry entry = requestQueue.getCache().invalidate(Endpoints.LOGIN_DATA,true);

//        if(entry!=null){
//            try {
//                String data = new String(entry.data, "UTF-8");
//                modelData(new JSONObject(data));
//                prepareAlbums();
//                System.out.print("");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            // process data
//        }else {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.GET_LOGIN_DATA, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.print("");
                        modelData(response);

                        //prepareAlbums();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });
        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(jsonObjReq);

//            progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.message_fetching));
        progressDialog.show();
    }
//    }

    private void modelData(JSONObject response) {
        jsonObject = new JSONObject();
        jsonArrayVendors = new JSONArray();
        jsonArrayVendorsType = new JSONArray();
        jsonArrayCustomerId = new JSONArray();
        jsonArrayVendorId = new JSONArray();
        jsonArrayCustomerName = new JSONArray();
        jsonArrayMerchantKey = new JSONArray();
        jsonArrayMerchantKeys =new JSONArray();






        Login login = (gson.fromJson(response.toString(), Login.class));


        if(login.getLoginResponses().length>0) {
            noVendors=false;


            for (LoginResponse loginResponse1 : login.getLoginResponses()) {

                // write in asset files locally


                try {

//                jsonObject.put("mobileNumber", params.get("mobileNumber"));
//
//                jsonObject.put("customerName", loginResponse1.getCustomerFirstName());
//                userProfile.setFirstname(loginResponse1.getCustomerFirstName());
//
//                jsonObject.put("customerLastName",loginResponse1.getCustomerLastName());
//                userProfile.setLastname(loginResponse1.getCustomerLastName());
//                editor.putString("cName",loginResponse1.getCustomerFirstName());
//
//                jsonObject.put("merchantId", loginResponse1.getMerchantId());

                    jsonObject.put("primaryPhone", loginResponse1.getCustomerPrimaryPhone());
                    userProfile.setPhoneNum(loginResponse1.getCustomerPrimaryPhone());
//                editor.putString("cPhone",loginResponse1.getCustomerPrimaryPhone());


                    jsonArrayCustomerName.put(loginResponse1.getCustomerFirstName() + " " + loginResponse1.getCustomerLastName());
                    jsonArrayMerchantKey.put(loginResponse1.getMerchantId());
                    //  vendorType.add(loginResponse1.getBusinessType());
                    //vendors.add(loginResponse1.getVendorCompanyName());
                    jsonArrayVendors.put(loginResponse1.getVendorCompanyName());

                    jsonArrayCustomerId.put(loginResponse1.getCustomerID());
                    jsonArrayVendorId.put(loginResponse1.getVendorID());


                    //vendorType.add(loginResponse1.getBusinessType());
                    jsonArrayVendorsType.put(loginResponse1.getBusinessType());

                    jsonArrayMerchantKeys.put(loginResponse1.getMerchantkey());

                    jsonObject.put("vendorArray", jsonArrayVendors);
                    jsonObject.put("vendorTypeArray", jsonArrayVendorsType);
                    jsonObject.put("customerIdArray", jsonArrayCustomerId);
                    jsonObject.put("vendorIdArray", jsonArrayVendorId);
                    jsonObject.put("customerNameArray", jsonArrayCustomerName);
                    jsonObject.put("merchantKeyArray", jsonArrayMerchantKey);
                    jsonObject.put("merchantKeysArray", jsonArrayMerchantKeys);

                    // jsonObject.put("vendorName",loginResponse1.getVendorCompanyName());
//                jsonDataArray.put(jsonObject);
                    editor.putString("jsonData", jsonObject.toString());
//                editor.putString("jsonData", jsonDataArray.toString());
                    editor.commit();

                    System.out.print("");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            prepareAlbums();

        }else {
            if(!isSecondaryDataPresent) {
                txtNoSubscription.setVisibility(View.VISIBLE);
                yourSub.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
            noVendors=true;
            if (progressDialog.isShowing())
                progressDialog.dismiss();

            editor.putString("jsonData", jsonObject.toString());
            editor.commit();
//
        }

//        userProfile.setAadharNum("");
//        userProfile.setDob("");
//        userProfile.setAddress("");
//        userProfile.setGender("");
//        SharedPreferenceManager.getInstance(getActivity()).setProfilePreferences(userProfile);


    }


    private void showDataFromDB(String s) {

        JSONArray jsonArray=null;
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(s);
//            jsonArray=new JSONArray(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int[] covers = new int[]{
//                R.drawable.ic_cableservices,
//                R.drawable.car_services,
//                R.drawable.news_services,
//                R.drawable.ic_milkservices,

                R.drawable.cable_services,
                R.drawable.car_services,
                R.drawable.news_services,
                R.drawable.milk_services,
                R.drawable.weightlifting,

             //   R.drawable.gym_services
        };

//        assert jsonArray != null;
//        for (int j = 0; j<jsonArray.length(); j++) {
//
//            try {
//                jsonObject=jsonArray.getJSONObject(j);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        JSONArray vendorArray=null;
        JSONArray customerIdArray=null;
        JSONArray vendorIdArray=null;
        JSONArray customerNameArray=null;
        JSONArray merchantKeyArray=null;
        JSONArray merchantKeysArray=null;

        try {
            vendorArray=jsonObject.getJSONArray("vendorArray");
            customerIdArray=jsonObject.getJSONArray("customerIdArray");
            vendorIdArray=jsonObject.getJSONArray("vendorIdArray");
            customerNameArray=jsonObject.getJSONArray("customerNameArray");
            merchantKeyArray=jsonObject.getJSONArray("merchantKeyArray");
            merchantKeysArray=jsonObject.getJSONArray("merchantKeysArray");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(vendorArray==null){
            //Toast.makeText(getActivity(), "There are no vendors currently subscribed", Toast.LENGTH_SHORT).show();
            txtNoSubscription.setVisibility(View.VISIBLE);
            yourSub.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }else {
            txtNoSubscription.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            yourSub.setVisibility(View.VISIBLE);

            JSONArray vendorTypeArray = null;
            try {
                vendorTypeArray = jsonObject.getJSONArray("vendorTypeArray");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int numOfSubscribers = vendorTypeArray.length();

            LoginResponse loginResponse;
            for (int i = 0; i < numOfSubscribers; i++) {
                SubcriptionGrid subcriptionGrid = null;
                try {
//                    // if (vendorTypeArray.getString(i).equals(getResources().getString(R.string.type_cabletv))) {
//                    if (vendorTypeArray.getString(i).equals("CABLE TV")) {
////                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"));
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));
//
//                        // for pilot
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable");
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable_"+vendorArray.getString(i).replaceAll("\\s",""));
//
//                        // } else if (vendorTypeArray.getString(i).equals(getResources().getString(R.string.type_interet))) {
//                    } else if (vendorTypeArray.getString(i).equals("INTERNET")) {
//
//                        // for pilot
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_internet");
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_internet_"+vendorArray.getString(i).replaceAll("\\s",""));
//
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[1], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));
//                    }

                    ///////added by surbhi

                    // if (vendorTypeArray.getString(i).equals(getResources().getString(R.string.type_cabletv))) {
                    if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_cable_tv))) {
                        Log.v(App,"cable type ");
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"));
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));

                        // for pilot
//                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable");
//                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable_"+vendorArray.getString(i).replaceAll("\\s",""));

                        // } else if (vendorTypeArray.getString(i).equals(getResources().getString(R.string.type_interet))) {
                    }

                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_internet)) ){
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[1], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));

                        //For pilot
//                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable,merle");
//                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable_"+vendorArray.getString(i).replaceAll("\\s",""));


                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_milk))) {
                        Log.v(App,"milk type ");
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[3], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));

                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_news_paper))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[2], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));

                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_magazine))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));


                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_school))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));


                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_gym))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[4], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));


                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_texi))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));


                    }

                    // else condition added by surbhi
                    else{
                        Log.v(App,"else ");
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));
                    }
                    //--condtion given initial which was getting creash
//                    else if (vendorTypeArray.getString(i).equals("INTERNET")) {
//
//                        // for pilot
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_internet");
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_internet_"+vendorArray.getString(i).replaceAll("\\s",""));
//
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[1], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                albumList.add(subcriptionGrid);


            }



            adapter.notifyDataSetChanged();
        }
        //   }

        if (progressDialog.isShowing())
            progressDialog.dismiss();

        primaryDataShown=true;
    }



    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.cable_services,
                R.drawable.car_services,
                R.drawable.news_services,
                R.drawable.milk_services,
                R.drawable.weightlifting
              //  R.drawable.gym_services
        };


        JSONArray vendorArray=null;
        JSONArray customerIdArray=null;
        JSONArray vendorIdArray=null;
        JSONArray customerNameArray=null;
        JSONArray merchantKeyArray=null;
        JSONArray merchantKeysArray=null;

        try {
            vendorArray=jsonObject.getJSONArray("vendorArray");
            customerIdArray=jsonObject.getJSONArray("customerIdArray");
            vendorIdArray=jsonObject.getJSONArray("vendorIdArray");
            customerNameArray=jsonObject.getJSONArray("customerNameArray");
            merchantKeyArray=jsonObject.getJSONArray("merchantKeyArray");
            merchantKeysArray=jsonObject.getJSONArray("merchantKeysArray");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(vendorArray==null){
            //Toast.makeText(getActivity(), "There are no vendors currently subscribed", Toast.LENGTH_SHORT).show();
            txtNoSubscription.setVisibility(View.VISIBLE);
            yourSub.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }else {
            txtNoSubscription.setVisibility(View.GONE);
            noData.setVisibility(View.GONE);
            yourSub.setVisibility(View.VISIBLE);

            JSONArray vendorTypeArray = null;
            try {
                vendorTypeArray = jsonObject.getJSONArray("vendorTypeArray");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int numOfSubscribers = vendorTypeArray.length();

            LoginResponse loginResponse;
            for (int i = 0; i < numOfSubscribers; i++) {
                SubcriptionGrid subcriptionGrid = null;
                try {
                    //--commented previous conditions

//                    if (vendorTypeArray.getString(i).equals("CABLE TV")) {
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));
//
//                        //For pilot
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable,merle");
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable_"+vendorArray.getString(i).replaceAll("\\s",""));
//
//
//                    } else if (vendorTypeArray.getString(i).equals("INTERNET")) {
//
//
//                        // For pilot
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_internet");
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_internet_"+vendorArray.getString(i).replaceAll("\\s",""));
//
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[1], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));
//                    }
//--new added by surbhi
                    if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_cable_tv))) {
                        Log.v(App,"cable tv");
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"));
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));

                        // for pilot
//                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable");
//                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable_"+vendorArray.getString(i).replaceAll("\\s",""));

                        // } else if (vendorTypeArray.getString(i).equals(getResources().getString(R.string.type_interet))) {
                    }

                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_internet))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[1], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));

                        //For pilot
//                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable,merle");
//                        FirebaseMessaging.getInstance().subscribeToTopic("operator_cable_"+vendorArray.getString(i).replaceAll("\\s",""));


                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_milk))) {
                        Log.v(App,"milk type ");
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[3], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));

                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_news_paper))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[2], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));

                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_magazine))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));


                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_school))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));


                    }
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_gym))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[4], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));


                    }
                    // else condition added by surbhi
                    else if (vendorTypeArray.getString(i).equals(getActivity().getResources().getString(R.string.buis_type_texi))) {
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));


                    }

                    else{
                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[0], merchantKeyArray.getString(i),customerNameArray.getString(i),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));
                    }
                    //--condtion given initial which was getting creash
//                    else if (vendorTypeArray.getString(i).equals("INTERNET")) {
//
//                        // for pilot
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_internet");
////                        FirebaseMessaging.getInstance().subscribeToTopic("operator_internet_"+vendorArray.getString(i).replaceAll("\\s",""));
//
//                        subcriptionGrid = new SubcriptionGrid(customerIdArray.getString(i), vendorArray.getString(i), vendorTypeArray.getString(i), vendorIdArray.getString(i), covers[1], jsonObject.getString("merchantId"),jsonObject.getString("customerName"),jsonObject.getString("primaryPhone"),merchantKeysArray.getString(i));
//                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



                albumList.add(subcriptionGrid);


            }


            adapter.notifyDataSetChanged();
        }

        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {

    }




    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homemenu, container, false);

        mainContent=(View) view.findViewById(R.id.main_content);






        return inflater.inflate(R.layout.fragment_homemenu, container, false);
    }

    public List<GlobalAdd> fill_with_data() {

        List<GlobalAdd> data = new ArrayList<>();

        //  data.add(new GlobalAdd( R.drawable.globaladd1, "Image 1"));
        //   data.add(new GlobalAdd( R.drawable.globaladd2, "Image 2"));
        //   data.add(new GlobalAdd( R.drawable.globaladd1, "Image 3"));
        //   data.add(new GlobalAdd( R.drawable.globaladd2, "Image 1"));


        return data;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
//        if(loginMobileNumber!=null)
//            getLoginData();



    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
