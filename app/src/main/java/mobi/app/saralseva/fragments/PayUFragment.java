package mobi.app.saralseva.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.PayUMoneyActivity;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils.PayByPayUBiz;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PayUFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PayUFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayUFragment  extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CUSTNAME = "Customer Name";
    private static final String ARG_CUSTID = "Customer Id";
    private static final String ARG_CUSTEMAIL = "Customer Email";
    private static final String ARG_CUSTPHONE = "Customer Phone";
    private static final String ARG_MERCHANEID = "Merchant Id";
    private static final String ARG_VENDORID = "Vendor Id";
    private static final String ARG_MERCHANTKEY = "Merchnat Key";
    private static final String ARG_EMAILID = "Email Id";
    public static final int RequestPermissionCode = 1;
    private static ArrayList<String> SampleArrayList ;
    private static Pattern pattern;
    private static String email;
    public static final String TAG = "Payment";
    private static final String ARG_VENDORNAME ="Vendor Name" ;
    public static Context baseContext;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;
    private View v;
    private static Account[] account ;

    Button btnPay;
    private PayByPayUBiz payByPayUBiz = null;
    String amount;
    private EditText editAmt;
    private static Context mContext;
    JSONObject params = new JSONObject();
    private String haskKey;
    private ProgressDialog progressDialog;

    String cName,cId,cPhone,cMail,mId,vId,vName,mKey;





    public PayUFragment() {
        // Required empty public constructor
    }
//    public PayUFragment (Context baseContext){
//        mContext = baseContext;
//        baseContext = baseContext;
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @param customerId
     * @param custName
     *@param primaryPhone

     */
    // TODO: Rename and change types and number of parameters
    public static PayUFragment newInstance(String customerId, String custName, String vendorName, String primaryPhone, String merchantId, String vendorId,String merchantKey,String emailId) {
        if(emailId==null || emailId.equalsIgnoreCase("")){
            Log.v("Email","null email");

            //  String email=GetAccountsName();
            Log.v("Email",""+email);

        }
        PayUFragment fragment = new PayUFragment();
        Bundle args = new Bundle();
        Log.v("Email Payu",""+emailId);
        args.putString(ARG_CUSTID, customerId);
        args.putString(ARG_CUSTNAME, custName);
        args.putString(ARG_VENDORNAME, vendorName);
        args.putString(ARG_CUSTEMAIL, emailId);
        args.putString(ARG_CUSTPHONE, primaryPhone);
        args.putString(ARG_MERCHANEID, merchantId);
        args.putString(ARG_VENDORID, vendorId);
        args.putString(ARG_MERCHANTKEY, merchantKey);
        Log.v("Email",""+ARG_CUSTEMAIL);

        fragment.setArguments(args);
        return fragment;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        pattern = Patterns.EMAIL_ADDRESS;
        Context context=getActivity();
        SampleArrayList = new ArrayList<String>();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        baseContext=context;
        super.onAttach(context);
        Activity baseContext;

        if (context instanceof Activity){
            baseContext=(Activity) context;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cId = getArguments().getString(ARG_CUSTID);
            cName = getArguments().getString(ARG_CUSTNAME);
            cPhone = getArguments().getString(ARG_CUSTPHONE);
            cMail = getArguments().getString(ARG_CUSTEMAIL);
            mId = getArguments().getString(ARG_MERCHANEID);
            vId = getArguments().getString(ARG_VENDORID);
            vName = getArguments().getString(ARG_VENDORNAME);
            mKey = getArguments().getString(ARG_MERCHANTKEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_pay_u, container, false);

        editAmt=(EditText)v.findViewById(R.id.et_amount);

        btnPay=(Button)v.findViewById(R.id.btnPay);

        btnPay.setOnClickListener(this);
        baseContext=getActivity();


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnPay:

                if(editAmt.getText().toString().length()>0) {
                    amount = editAmt.getText().toString();


                    // Call the service to get the hash from server



                    getHashFromServer(amount);

//                    Intent i = new Intent(getActivity(), PayUMoneyActivity.class);
//                    i.putExtra("amount", amount);
//                    getActivity().startActivity(i);

                }else{
                    Toast.makeText(getActivity(), R.string.toast_blank_amount, Toast.LENGTH_SHORT).show();
                }







//                if(editAmt.getText().toString().length()>0) {
//                    amount=(Integer.parseInt(editAmt.getText().toString()));
//
//                    payByPayUBiz = PayByPayUBiz.getInstance();
//                    payByPayUBiz.setProductionEnvironment(false);
////                    payByPayUBiz.execute(getActivity(), "gtKFFx", "Sagar", "sagar@gmail.india.com", amount + "");
//                    payByPayUBiz.execute(getActivity(), "4825050", "testchild1@bK.cc", "", amount + "");
//
////                    payByPayUBiz.execute(getActivity(), "qTg1so", "Sagar", "sagar@gmail.india.com", amount + "");
//                    getDialog().dismiss();
//                }else{
//                    Toast.makeText(getActivity(), R.string.toast_blank_amount, Toast.LENGTH_SHORT).show();
//                }


                break;
        }

    }

    private void getHashFromServer(String amt) {

        try {
//            params.put("amnt", amt);
//            params.put("tranId", "TXN_11"+System.currentTimeMillis());
//            params.put("firstName", "Mobi");
//            params.put("email", "info@mobicollector.com");
//            params.put("vendorID", "2");
//            params.put("phone", "9989888899");
//            params.put("name", "Cable");
//            params.put("descrption", "Cable_Billing");
//            params.put("custNum", "1675");
//            params.put("merchantID", "4825050");
            Log.v("payu"," getHashFromServer inside");

            params.put("amnt", amt);
            params.put("tranId", "TM_"+cId+System.currentTimeMillis());
         //   params.put("tranId", "TXN_11"+System.currentTimeMillis());
            params.put("firstName", cName);
            params.put("email", cMail);
            params.put("vendorID", vId);
            params.put("phone", cPhone);
            params.put("name", "Cable");
            params.put("descrption", "Cable_Billing");
            params.put("custNum", cId);
            params.put("merchantID", mId);

            Log.v("payu getHashFromServer","pay u server"+amt +","+ cName +","+ cMail +","+ vId +","+ cPhone +","+cId +","+ mId);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.PAYU_GET_HASH, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        System.out.print("");

                        try {
                            haskKey=response.getString("hashKey");


                            Intent i = new Intent(getActivity(), PayUMoneyActivity.class);
                            i.putExtra("hash", haskKey);



                            getActivity().startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (progressDialog.isShowing())
                            progressDialog.dismiss();


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();


                // the errror is due to a rreason: response Json not well formatted; but it is as per the PayU Hash standard hence adjustment is done as follows
                try{
                    haskKey= error.getMessage().substring(80,error.getMessage().length()-3);
                    Intent i = new Intent(getActivity(), PayUMoneyActivity.class);
                    i.putExtra("hash", haskKey);
                    i.putExtra("managedCustId",cId);
                    i.putExtra("managedVendorId",vId);
                    i.putExtra("managedMerchantId",mId);
                    i.putExtra("managedVendorName",vName);
                    i.putExtra("managedCustName",cName);
                    i.putExtra("managedPrimaryPhone",cPhone);
                    i.putExtra("managedMerchantKey",mKey);
                    getActivity().startActivity(i);
                    getDialog().dismiss();} catch (NullPointerException e){Log.e("payu",""+e);}
            }
        });



//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.PAYU_GET_HASH,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.print("");
//
//                            Intent i = new Intent(getActivity(), PayUMoneyActivity.class);
//                            i.putExtra("hashKey", response);
//                            getActivity().startActivity(i);
//
//                        if (progressDialog.isShowing())
//                            progressDialog.dismiss();
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (progressDialog.isShowing())
//                    progressDialog.dismiss();
//
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return params.toString().getBytes();
//            }
//        };


        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
//        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(stringRequest);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.message_pay_initiate));
        progressDialog.show();

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
