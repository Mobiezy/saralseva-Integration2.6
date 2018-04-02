package mobi.app.saralseva.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.BaseApp;
import mobi.app.saralseva.models.ComplaintResponse;
import mobi.app.saralseva.models.Complaints;
import mobi.app.saralseva.models.PlanResponse;
import mobi.app.saralseva.models.Plans;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils_view.AdapterPlanDetail;
import mobi.app.saralseva.utils_view.AdapterRegComplaint;
import mobi.app.saralseva.utils_view.DataPlanDetail;
import mobi.app.saralseva.utils_view.DataRegComplaint;


/**
 * Created by deviprasad on 1/22/2017.
 */

//Our class extending fragment
public class RegCompFragment extends Fragment {
    private List<DataRegComplaint> dataRegComplaints;
    private View v;

    private RequestQueue requestQueue;
    JSONObject params = new JSONObject();
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private Gson gson;
    RecyclerView rv;
    private TextView textViewNoComplaints;
    private String vendorId;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String customerId;
    boolean viewCreated=false;



    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        v= inflater.inflate(R.layout.registrered_layout, container, false);

        requestQueue= RequestSingletonClass.getInstance(getActivity()).getRequestQueue();
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson=gsonBuilder.create();

        textViewNoComplaints=(TextView)v.findViewById(R.id.noComplaints);
        textViewNoComplaints.setVisibility(View.GONE);

        sharedPreferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        rv = (RecyclerView)v.findViewById(R.id.registeredCompRecyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);


        if(getActivity().getIntent()!=null){
            vendorId=getActivity().getIntent().getStringExtra("vendorId");
            customerId=getActivity().getIntent().getStringExtra("cust_id");

        }

        viewCreated=true;

//        getComplaints();


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        rv = (RecyclerView)getView().findViewById(R.id.registeredCompRecyclerView);
//        rv.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//        rv.setLayoutManager(llm);
//
//        //initializeData();
////        AdapterRegComplaint adapter = new AdapterRegComplaint(dataRegComplaints);
////        rv.setAdapter(adapter);
//
//        if(getActivity().getIntent()!=null){
//            vendorId=getActivity().getIntent().getStringExtra("vendorId");
//            customerId=sharedPreferences.getString("cust_id","");
//
//        }






    }

    private void getComplaints() {
        try {
//            params.put("cust_number", customerId);
//            params.put("vendor_ID", vendorId);

            params.put("custBusinessID", customerId);
            params.put("vendorID", vendorId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.GET_COMPLAINTS, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        System.out.print("");
                        showComplaints(response);

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

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.complaint_getting));
        progressDialog.show();
        // }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.print("");
        if(viewCreated) {
            if (BaseApp.isNetworkAvailable(getActivity())) {
                textViewNoComplaints.setVisibility(View.GONE);
                if (isVisibleToUser) {
                    getComplaints();
                }
            } else {
                textViewNoComplaints.setText(R.string.messge_no_internet);
                textViewNoComplaints.setVisibility(View.VISIBLE);
            }
        }


    }



    private void showComplaints(JSONObject response) {

        jsonObject = new JSONObject();

//        boolean noComplaints=false;
//        try {
//            if(response.getString("message")!=null) {
//                 if(response.getString("message").equals("No Record found")){
//                     noComplaints=true;
//                 }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        dataRegComplaints = new ArrayList<>();
        Complaints complaints = (gson.fromJson(response.toString(), Complaints.class));

            if(complaints.getComplaintResponses()==null || complaints.getComplaintResponses().length==0){
                textViewNoComplaints.setText(R.string.no_complaints_available);
                textViewNoComplaints.setVisibility(View.VISIBLE);
            }else {

                textViewNoComplaints.setVisibility(View.GONE);
                for (ComplaintResponse complaintResponse : complaints.getComplaintResponses()) {


                    dataRegComplaints.add(new DataRegComplaint(complaintResponse.getTicketNum(), complaintResponse.getIssue(),complaintResponse.getDetail(), complaintResponse.getStatus()));



                }





                AdapterRegComplaint adapter = new AdapterRegComplaint(dataRegComplaints,vendorId,customerId);
                rv.setAdapter(adapter);
            }



    }


    private void initializeData(){
//        dataRegComplaints = new ArrayList<>();
//        dataRegComplaints.add(new DataRegComplaint("3535", "Slow Connection", "Pending"));
//        dataRegComplaints.add(new DataRegComplaint("6735", "Online Payment", "Pending"));
//        dataRegComplaints.add(new DataRegComplaint("78535", "No cable", "Pending"));
//        dataRegComplaints.add(new DataRegComplaint("9535", "Disconnected", "Approved"));

    }

}
