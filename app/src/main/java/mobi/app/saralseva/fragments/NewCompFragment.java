package mobi.app.saralseva.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.BaseApp;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.AlertManager;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils_view.ComplaintAdapter;
import mobi.app.saralseva.utils_view.ComplaintOptions;
import mobi.app.saralseva.utils_view.DataNewComplaint;

/**
 * Created by deviprasad on 1/22/2017.
 */

//Our class extending fragment
public class NewCompFragment extends Fragment implements View.OnClickListener {
    private List<DataNewComplaint> dataNewComplaints;
    private ComplaintOptions complaintOptions;
    List<String> complaintList;
    LinearLayout serviceLayout,billingLayout,paymentLayout;
    TextView serviceText,billingText,paymentText;
    private View v;
    //Overriden method onCreateView
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    EditText editComments;
    Button btnSubmit;

    private RequestQueue requestQueue;
    JSONObject params;
    JSONArray paramsArray;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private Gson gson;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences_login;
    private String vendorId;
    private String customerId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.new_comp_layout, container, false);
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes

        requestQueue= RequestSingletonClass.getInstance(getActivity()).getRequestQueue();
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson=gsonBuilder.create();

        sharedPreferences = getActivity().getSharedPreferences("complaint_prefs",Context.MODE_PRIVATE);
        sharedPreferences_login = getActivity().getSharedPreferences("prefs",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        serviceLayout= (LinearLayout) v.findViewById(R.id.serviceLayout);
        billingLayout= (LinearLayout) v.findViewById(R.id.billingLayout);
        paymentLayout= (LinearLayout) v.findViewById(R.id.paymentLayout);

        editComments=(EditText)v.findViewById(R.id.etComments);
        btnSubmit=(Button)v.findViewById(R.id.btnSubmit);

        serviceText= (TextView) v.findViewById(R.id.textViewService);
        billingText= (TextView) v.findViewById(R.id.textViewBilling);
        paymentText= (TextView) v.findViewById(R.id.textViewPayment);

        serviceLayout.setOnClickListener(this);
        billingLayout.setOnClickListener(this);
        paymentLayout.setOnClickListener(this);

        if(getActivity().getIntent()!=null){
            vendorId=getActivity().getIntent().getStringExtra("vendorId");
            customerId=getActivity().getIntent().getStringExtra("cust_id");



        }

        btnSubmit.setOnClickListener(this);
        return v;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        callRecyclerView("internet_service");
        editor.putString("complaint_type","service");
        editor.commit();

    }

    private  void callRecyclerView(String issue){
        RecyclerView rv = (RecyclerView) getView().findViewById(R.id.newComplaintRecyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        initializeData(issue);
//        AdapterNewComplaint adapter = new AdapterNewComplaint(dataNewComplaints);
        ComplaintAdapter adapter = new ComplaintAdapter(complaintList);
        rv.setAdapter(adapter);

    }
    private void initializeData(String issue) {
        complaintList=new ArrayList<>();
        editor.remove("comp_type_desc");
        editor.commit();
        //getting shared prefernce of language
//        language=sharedPreferences.getString("lang","");
//        if(language.equals("Hindi")){
//            locale="hi";
        switch (issue){
//            case "internet_service":   dataNewComplaints = new ArrayList<>();
//                dataNewComplaints.add(new DataNewComplaint(getString(R.string.complaint_internet1),getString(R.string.complaint_internet2),getString(R.string.complaint_internet3),""));
//                break;
//            case "internet_payment":   dataNewComplaints = new ArrayList<>();
//                dataNewComplaints.add(new DataNewComplaint(getString(R.string.complaint_payment1),getString(R.string.complaint_payment2),getString(R.string.complaint_payment3),""));
//                break;
//            case "internet_billing":   dataNewComplaints = new ArrayList<>();
//                dataNewComplaints.add(new DataNewComplaint(getString(R.string.complaint_billing1),getString(R.string.complaint_billing2),getString(R.string.complaint_billing3),""));
//                break;


            case "internet_service":

                complaintList.clear();

                complaintList.add(getString(R.string.label_support_faulty_box));
                complaintList.add(getString(R.string.label_support_no_channel));
                complaintList.add(getString(R.string.label_support_channel_unavailable));
                complaintList.add(getString(R.string.label_support_channel_stuck));
                complaintList.add(getString(R.string.label_support_audio));
                complaintList.add(getString(R.string.label_support_bad_quality));
                complaintList.add(getString(R.string.label_support_remote_issue));
                complaintList.add(getString(R.string.label_support_channel_hd));
                complaintList.add(getString(R.string.label_support_others));
                complaintOptions = new ComplaintOptions(complaintList);
                //dataNewComplaints.add(new DataNewComplaint(getString(R.string.complaint_internet1),getString(R.string.complaint_internet2),getString(R.string.complaint_internet3),""));
                break;
            case "internet_payment":

                complaintList.clear();
                complaintList.add(getString(R.string.label_support_payment_refelction));
                complaintList.add(getString(R.string.label_support_payment_rejection));
                complaintList.add(getString(R.string.label_support_balance_wrong));
                complaintList.add(getString(R.string.label_support_agent_novisit));
                complaintList.add(getString(R.string.label_support_others));
                complaintOptions = new ComplaintOptions(complaintList);
                break;
            case "internet_billing":
                complaintList.clear();
                complaintList.add(getString(R.string.label_support_bill_difference));
                complaintList.add(getString(R.string.label_support_charged_extra));
                complaintList.add(getString(R.string.label_support_bill_double));
                complaintList.add(getString(R.string.label_support_bill_additionalpackages));
                complaintList.add(getString(R.string.label_support_others));
                complaintOptions = new ComplaintOptions(complaintList);

                break;

        }


    }
    protected void hideSoftKeyboard(EditText input) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.serviceLayout:
                editor.putString("complaint_type","service");
                editor.commit();
                callRecyclerView("internet_service");
                serviceText.setTextColor(getResources().getColor(R.color.colorPrimary));
                billingText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                paymentText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                hideSoftKeyboard(editComments);
                break;
            case R.id.billingLayout:
                editor.putString("complaint_type","billing");
                editor.commit();
                callRecyclerView("internet_billing");
                serviceText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                billingText.setTextColor(getResources().getColor(R.color.colorPrimary));
                paymentText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                hideSoftKeyboard(editComments);
                break;
            case R.id.paymentLayout:
                editor.putString("complaint_type","payment");
                editor.commit();
                callRecyclerView("internet_payment");
                serviceText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                billingText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                paymentText.setTextColor(getResources().getColor(R.color.colorPrimary));
                hideSoftKeyboard(editComments);
                break;
            case R.id.btnSubmit:
                if(BaseApp.isNetworkAvailable(getActivity())) {
                    if (validateComplaintSelection()) {
                        postComments();
                    } else {
                        AlertManager.showAlertDialog(getActivity(), getString(R.string.alert_header_support), getString(R.string.alert_desc_nocomplaintselected), true);
                    }
                }else{
                    AlertManager.showAlertDialog(getActivity(),getString(R.string.alert_header_internet),getString(R.string.alert_desc_internet),false);
                }

                break;

        }

    }

    private boolean validateComplaintSelection() {
        String s=sharedPreferences.getString("comp_type_desc","");

        if(s.length()>0){
            return true;
        }else{
            return false;
        }
    }

    private void postComments() {


        Random rand = new Random();
        int randNum = rand.nextInt(10000);
        String comp_type=sharedPreferences.getString("complaint_type","");
        String comp_type_desc=sharedPreferences.getString("comp_type_desc","");

        String comp_desc=editComments.getText().toString();

       // String cust_id=sharedPreferences_login.getString("cust_id","");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        params = new JSONObject();
        try {
//            params.put("mcomp_id","4778BTM13");
//            params.put("mcomp_id",randNum);
            params.put("cmpID",randNum+customerId);
//            params.put("mcust_id","13");
//            params.put("mcust_id",customerId);
            params.put("custNum",customerId);
//          p  params.put("mcmp_Type","PAYMENT");
//            params.put("mcmp_Type",comp_type);URLEncoder.encode(comp_type_desc, "UTF-8");
            params.put("cmpType",comp_type);

//            params.put("moper_id",vendorId);
            params.put("vendorID",vendorId);
//            params.put("mcmp_detail","PAYMENT PAID LAST MONTH");
//            params.put("mcmp_detail",comp_type_desc);
            //
            params.put("cmpDetail",comp_type_desc);
//            params.put("remarks",comp_desc);
            params.put("cmpRemarks",comp_desc);

           // params.put("magent_id","4");
//            params.put("mcomp_date","2017-01-12 12:33:55");
          //  params.put("mcomp_date",currentDateandTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }






//        paramsArray=new JSONArray();
//        paramsArray.put(params);
//
//        JSONObject mainObj=new JSONObject();
//        try {
//            mainObj.put("complaintsList",paramsArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_COMPLAINT,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.print("");
//                        if(response.contains("SUCCESS")){
//
//                            editor.remove("comp_type_desc");
//                            editor.remove("comp_type");
//                            System.out.print("");
//                            editor.commit();
//                            editComments.setText("");
//                            System.out.print("");
//
//
//                            if (progressDialog.isShowing())
//                                progressDialog.dismiss();
//
//
//                            Toast.makeText(getActivity(),R.string.complaint_register_success, Toast.LENGTH_SHORT).show();
//
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//               // headers.put("User-agent", "My useragent");
//                return headers;
//            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return params.toString().getBytes();
//            }
//        };


        System.out.print("");

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.POST_COMPLAINT, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        System.out.print("");


                        try {
                            if(response.getString("message").equals("SUCCESS")){
                                Toast.makeText(getActivity(), R.string.complaint_register_success, Toast.LENGTH_SHORT).show();
                                editor.remove("comp_type_desc");
                                editor.remove("comp_type");
                                System.out.print("");
                                editor.commit();
                                editComments.setText("");
                                System.out.print("");

                            }else{
                                Toast.makeText(getActivity(), R.string.complaint_register_failure, Toast.LENGTH_SHORT).show();
                            }
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
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
               // headers.put("User-agent", "My useragent");
                return headers;
            }
        };

        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(jsonObjReq);
//        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(stringRequest);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.message_loading_complaint));
        progressDialog.show();

    }

}


