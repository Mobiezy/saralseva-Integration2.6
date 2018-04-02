package mobi.app.saralseva.fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.BaseApp;
import mobi.app.saralseva.models.Login;
import mobi.app.saralseva.models.LoginResponse;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.AlertManager;
import mobi.app.saralseva.utils.Endpoints;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddNewVendorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewVendorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewVendorFragment extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View v;
    private EditText mobNum;
    private ImageView btnAdd;
    JSONObject params = new JSONObject();
    String secondaryPhoneNumber;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;

    JSONObject jsonObject;
    JSONArray jsonArrayVendors ;
    JSONArray jsonArrayVendorsType;
    JSONArray jsonArrayCustomerId;
    JSONArray jsonArrayVendorId;
    JSONArray jsonArrayCustomerName;
    JSONArray jsonArrayMerchantKey;

    private Gson gson;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private JSONObject vendorObject;
    private JSONObject secondaryVendors;
    private JSONArray alVendorsArray;
    private String primaryNum;
    private TextView txtAlert;

    private Button btnVerifyOTP,btnResendOTP;
    private EditText etOTP;

    String authKey="146596Ae1TZ6tee58d94ce2";

    String sendor="saralseva";
    String otp="5657";
//    String msg="Your%20otp%20is "+otp;
    String msg="Your otp is "+otp;
    private boolean verified=false;

    LinearLayout linearLayoutOTPSection;

    public AddNewVendorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AddNewVendorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewVendorFragment newInstance(String param1) {
        AddNewVendorFragment fragment = new AddNewVendorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Typeface robotoThin = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Thin.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Light.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(),
                "font/Roboto-Regular.ttf");
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_add_new_vendor, container, false);
        //getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        String title=getArguments().getString(ARG_PARAM1);
        getDialog().setTitle(title);
        progressDialog = new ProgressDialog(getActivity());

        sharedPreferences = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        primaryNum=sharedPreferences.getString("mobileNumber","");

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson=gsonBuilder.create();



        mobNum=(EditText)v.findViewById(R.id.et_addvendor_mobnum);
        txtAlert=(TextView)v.findViewById(R.id.txt_addvendor_alert);

        etOTP=(EditText)v.findViewById(R.id.edittext_OTP);
        btnVerifyOTP=(Button)v.findViewById(R.id.btn_verify_otp);
        btnResendOTP=(Button)v.findViewById(R.id.btn_resend_otp);

        linearLayoutOTPSection=(LinearLayout)v.findViewById(R.id.linear_OTP_section);


        btnVerifyOTP.setOnClickListener(this);
        btnResendOTP.setOnClickListener(this);

        txtAlert.setText("");



        mobNum.setTypeface(robotoLight);
        mobNum.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnAdd= (ImageView) v.findViewById(R.id.imageViewAddPhone);
        btnAdd.setOnClickListener(this);

        mobNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(TextUtils.isEmpty(charSequence.toString().trim())){
                        txtAlert.setVisibility(View.GONE);
                    }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String s) {
        if (mListener != null) {
            mListener.onFragmentInteraction(s);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageViewAddPhone:
                secondaryPhoneNumber=mobNum.getText().toString();

                if(secondaryPhoneNumber.length()<10 || secondaryPhoneNumber.length()==0 || secondaryPhoneNumber.length()>10){
                    txtAlert.setText(R.string.alert_desc_enter_valid_num);
                }else {
                    txtAlert.setText("");



                    if (BaseApp.isNetworkAvailable(getActivity())) {
                        if (secondaryPhoneNumber.equals(primaryNum)) {
                            //AlertManager.showAlertDialog(getActivity(),getString(R.string.alert_header_addvendor),getString(R.string.alert_desc_phonenumber_added),false);
                            txtAlert.setText(getString(R.string.alert_desc_phonenumber_added));

                        } else {
                            txtAlert.setText("");
                            btnResendOTP.setEnabled(true);
                            //authenticateNumberWithOTP(secondaryPhoneNumber);
                            getVendors();

                        }

                    } else {
                        //AlertManager.showAlertDialog(getActivity(),getString(R.string.alert_header_internet),getString(R.string.alert_desc_internet),false);
                        txtAlert.setText(getString(R.string.alert_desc_internet));
                        //getDialog().dismiss();
                    }
                }



                break;

            case R.id.btn_verify_otp:
                String enteredOTP=etOTP.getText().toString();
                if(enteredOTP.length()<0 || enteredOTP.length() >4 || enteredOTP.length()<4)
                    Toast.makeText(getActivity(),"Please enter a valid OTP",Toast.LENGTH_LONG).show();
                else
                    verifySecondaryOTP(enteredOTP);

                break;

            case R.id.btn_resend_otp:
                etOTP.setText("");
                resendOTP();
                break;
        }
    }

    private void resendOTP() {
        String uri = String.format(Endpoints.OTP_RESEND_URL+"?authkey=%1$s&mobile=%2$s&retrytype=text",authKey,secondaryPhoneNumber);

        requestQueue= RequestSingletonClass.getInstance(getActivity()).getRequestQueue();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                uri,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String res="";
                        if(progressDialog.isShowing())
                            progressDialog.hide();

                        try {
                            res=response.getString("type");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(res.equals("error")) {
                            //txtAlert.setText("OTP is not verified, please try again");
                        }
                        else if(res.equals("success")) {
                            txtAlert.setText("");
                            //getVendors();
                            Toast.makeText(getActivity(),"OTP  Sent",Toast.LENGTH_LONG).show();
                        }

                        System.out.print("");


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(jsonObjReq);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Resending OTP..");
        progressDialog.show();




    }

    private void verifySecondaryOTP(String enteredOTP) {

        String uri = String.format(Endpoints.OTP_VERIFY_URL+"?authkey=%1$s&mobile=%2$s&otp=%3$s",authKey,secondaryPhoneNumber,enteredOTP);

        requestQueue= RequestSingletonClass.getInstance(getActivity()).getRequestQueue();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                uri,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String res="";
                        if(progressDialog.isShowing())
                            progressDialog.hide();

                        try {
                            res=response.getString("type");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(res.equals("error"))
                            txtAlert.setText("OTP is not verified");
                        else if(res.equals("success")) {
                            txtAlert.setText("");
                            verified=true;
                            getVendors();
                            Toast.makeText(getActivity(),"OTP  verified",Toast.LENGTH_LONG).show();
                        }

                        System.out.print("");


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(jsonObjReq);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Validating OTP..");
        progressDialog.show();



    }

    private void authenticateNumberWithOTP(String secondaryPhoneNumber) {

        int randomPIN = (int)(Math.random()*9000)+1000;
        otp= ""+randomPIN;

        msg="Your OTP number to confirm the mobile number "+secondaryPhoneNumber+" is "+otp+". Please do not share with anybody.";

        String uri = String.format(Endpoints.OTP_URL+"?authkey=%1$s&mobile=%2$s&message=%3$s&sender=%4$s&otp=%5$s",authKey,secondaryPhoneNumber,msg,sendor,otp);

        requestQueue= RequestSingletonClass.getInstance(getActivity()).getRequestQueue();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                uri,null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String res="";
                        if(progressDialog.isShowing())
                            progressDialog.hide();

                        try {
                            res=response.getString("type");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(res.equals("error"))
                            txtAlert.setText("Error occured, please validate number and try resending");
                        else
                            Toast.makeText(getActivity(),"OTP Sent",Toast.LENGTH_LONG).show();
                            linearLayoutOTPSection.setVisibility(View.VISIBLE);

                        System.out.print("");


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(jsonObjReq);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending OTP..");
        progressDialog.show();



    }


    private void getVendors() {
       String fToken= FirebaseInstanceId.getInstance().getToken();
        try {
            params.put("mobileNumber", secondaryPhoneNumber);
//            params.put("longitude",mCurrentLocation.getLongitude());
//            params.put("latitude",mCurrentLocation.getLatitude());
            params.put("longitude","lat");
            params.put("latitude","longit");
//            params.put("fireBaseToken",fToken);  // removed token for pilot
            params.put("fireBaseToken","");  // removed token for pilot
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue= RequestSingletonClass.getInstance(getActivity()).getRequestQueue();




        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.GET_LOGIN_DATA, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if(progressDialog.isShowing())
                            progressDialog.hide();

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

            progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait while we load associated data");
//        progressDialog.setMessage(getString(R.string.message_fetching));
        progressDialog.show();

    }

    private void modelData(JSONObject response) {

        // String str=sharedPreferences.getString("jsonSecondaryData","");

        jsonObject = new JSONObject();
//        try {
//            jsonObject.put(str,0);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        jsonArrayVendors = new JSONArray();
        jsonArrayVendorsType = new JSONArray();
        jsonArrayCustomerId = new JSONArray();
        jsonArrayVendorId = new JSONArray();
        jsonArrayCustomerName = new JSONArray();
        jsonArrayMerchantKey = new JSONArray();

        alVendorsArray = new JSONArray();


        Login login = (gson.fromJson(response.toString(), Login.class));

        int isSubscribed=login.getLoginResponses().length;
        if(isSubscribed>0) {

            // below code authenticates user with seconday phine and adds vendor details
//            if(!verified)
//                authenticateNumberWithOTP(secondaryPhoneNumber);
//
//            if(verified){
//
//                if (login.getLoginResponses().length > 0) {
//
//
//
//                    for (LoginResponse loginResponse1 : login.getLoginResponses()) {
//
//                        // write in asset files locally
//
//
//                        try {
////                    jsonObject.put("mobileNumber", params.get("mobileNumber"));
////                    jsonObject.put("customerName", loginResponse1.getCustomerFirstName());
////                    jsonObject.put("merchantId", loginResponse1.getMerchantId());
////
////                    //editor.putString("cName",loginResponse1.getCustomerFirstName());
////
//                            jsonObject.put("primaryPhone", loginResponse1.getCustomerPrimaryPhone());
//                            //editor.putString("cPhone",loginResponse1.getCustomerPrimaryPhone());
//
//                            //  vendorType.add(loginResponse1.getBusinessType());
//                            //vendors.add(loginResponse1.getVendorCompanyName());
//                            jsonArrayCustomerName.put(loginResponse1.getCustomerFirstName()+ " "+loginResponse1.getCustomerLastName());
//                            jsonArrayMerchantKey.put(loginResponse1.getMerchantId());
//
//                            jsonArrayVendors.put(loginResponse1.getVendorCompanyName());
//
//                            jsonArrayCustomerId.put(loginResponse1.getCustomerID());
//                            jsonArrayVendorId.put(loginResponse1.getVendorID());
//
//
//                            //vendorType.add(loginResponse1.getBusinessType());
//                            jsonArrayVendorsType.put(loginResponse1.getBusinessType());
//
//                            jsonObject.put("vendorArray", jsonArrayVendors);
//                            jsonObject.put("vendorTypeArray", jsonArrayVendorsType);
//                            jsonObject.put("customerIdArray", jsonArrayCustomerId);
//                            jsonObject.put("vendorIdArray", jsonArrayVendorId);
//                            jsonObject.put("customerNameArray", jsonArrayCustomerName);
//                            jsonObject.put("merchantKeyArray", jsonArrayMerchantKey);
//
//
//                            // jsonObject.put("vendorName",loginResponse1.getVendorCompanyName());
////                editor.putString("jsonSecondaryData", jsonObject.toString());
////                editor.commit();
//                            System.out.print("");
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    JSONArray jsonArray = null;
//                    JSONObject tempjsonObject = new JSONObject();
//                    boolean duplicateNum=false;
//                    List<JSONObject> jsonObjects = new ArrayList<JSONObject>(2);
//                    String s = sharedPreferences.getString("jsonSecondaryData", "");
//                    if (s.length() > 0) {
//                        try {
//                            jsonArray = new JSONArray(s);
//                            int i;
//                            for (i = 0; i < jsonArray.length(); i++) {
//                                tempjsonObject=(JSONObject)jsonArray.get(i);
////                        jsonObjects.add(i, (JSONObject) jsonArray.get(i));
//
//                                String newNum=jsonObject.getString("mobileNumber");
//                                String oldNum=tempjsonObject.getString("mobileNumber");
//
//                                if(newNum.equals(oldNum))
//                                    duplicateNum=true;
//
//                                jsonObjects.add(i, tempjsonObject);
//
//                            }
//                            if(!duplicateNum)
//                                jsonObjects.add(i, jsonObject);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    } else {
//                        jsonObjects.add(jsonObject);
//                    }
//
//
//                    editor.putString("jsonSecondaryData", jsonObjects.toString());
//                    editor.commit();
//
////        String s = sharedPreferences.getString("jsonData", "");
////
////        try {
////            vendorObject = new JSONObject(s);
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////
////        String secondStr = sharedPreferences.getString("jsonSecondaryData", "");
////
////                try {
////                    secondaryVendors = new JSONObject(secondStr);
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////
////        alVendorsArray.put(s);
////        alVendorsArray.put(secondStr);
//
////        alVendorsArray.put(vendorObject);
////        alVendorsArray.put(secondaryVendors);
//
////        editor.putString("jsonData", alVendorsArray.toString());
////        editor.commit();
//
//
//
//                    if (progressDialog.isShowing())
//                        progressDialog.dismiss();
//
//                    Toast.makeText(getActivity(), R.string.toast_vendor_notified,Toast.LENGTH_LONG).show();
//
//
//
//
//                    getDialog().dismiss();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    HomeFragment homeMenuFragment = new HomeFragment();
//
//                    fragmentManager.beginTransaction().replace(R.id.masterpage, homeMenuFragment).commit();
//
//
//
//                }else{
//                    if (progressDialog.isShowing())
//                        progressDialog.dismiss();
//                    txtAlert.setText(R.string.message_novendors_subscibed);
//                }
//
//            }
            txtAlert.setText(R.string.toast_vendor_notified);
        }else{
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            txtAlert.setText(R.string.message_novendors_subscibed);
        }






//        if (progressDialog.isShowing())
//            progressDialog.dismiss();
//
//
//
//
//        getDialog().dismiss();
//        FragmentManager fragmentManager = getFragmentManager();
//        HomeFragment homeMenuFragment = new HomeFragment();
//
//        fragmentManager.beginTransaction().replace(R.id.masterpage, homeMenuFragment).commit();
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
        void onFragmentInteraction(String s);


    }
}
