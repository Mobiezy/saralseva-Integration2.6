package mobi.app.saralseva.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.models.BillResponse;
import mobi.app.saralseva.models.Bills;
import mobi.app.saralseva.models.PlanResponse;
import mobi.app.saralseva.models.Plans;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils_view.AdapterBillHistory;
import mobi.app.saralseva.utils_view.AdapterPlanDetail;
import mobi.app.saralseva.utils_view.DataBillHistory;
import mobi.app.saralseva.utils_view.DataPlanDetail;

public class BillHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private List<DataBillHistory> dataBillHistorys;
    private RequestQueue requestQueue;
    JSONObject params = new JSONObject();
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private Gson gson;
    RecyclerView rv;
    private TextView textViewNoBills,txtBillStatus;
    ImageView imgvwBillStatus;
    private ImageView imageBackAction;
    private TextView titleBar;
    private String vendorId;
    private String billStatus;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_history);
        rv = (RecyclerView)findViewById(R.id.billHistoryRecyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        Typeface robotoRegular = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Regular.ttf");
        Typeface robotoLight = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Light.ttf");
        titleBar= (TextView) findViewById(R.id.activityName);
        titleBar.setTypeface(robotoLight);
        titleBar.setText(getResources().getString(R.string.bill_history));
        imageBackAction= (ImageView) findViewById(R.id.actionButton);
        imageBackAction.setOnClickListener(this);

        txtBillStatus= (TextView) findViewById(R.id.txtBillStatus);
        imgvwBillStatus= (ImageView) findViewById(R.id.imageViewNodata);
        txtBillStatus.setText(R.string.message_no_bills);
        txtBillStatus.setVisibility(View.GONE);
        imgvwBillStatus.setVisibility(View.GONE);

        requestQueue= RequestSingletonClass.getInstance(this).getRequestQueue();
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson=gsonBuilder.create();

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(getIntent()!=null){
                vendorId = getIntent().getStringExtra("vendorId");
                customerId = getIntent().getStringExtra("custId");
                //customerId=sharedPreferences.getString("cust_id","");
        }

        if(BaseApp.isNetworkAvailable(this)){
            txtBillStatus.setVisibility(View.GONE);
            imgvwBillStatus.setVisibility(View.GONE);
            getBillHistory();
        }else{
            txtBillStatus.setText(getResources().getString(R.string.messge_no_internet));
            txtBillStatus.setVisibility(View.VISIBLE);
            imgvwBillStatus.setVisibility(View.VISIBLE);
        }


//        initializeData();
//        AdapterBillHistory adapter = new AdapterBillHistory(dataBillHistorys);
//        rv.setAdapter(adapter);
    }

    private void getBillHistory() {
        try {
            params.put("vendorID", vendorId);
            params.put("custBusinessID", customerId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.GET_BILL_HISTORY, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        System.out.print("");



                        showHistory(response);



                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

        RequestSingletonClass.getInstance(this).addToRequestQueue(jsonObjReq);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_bill_history));
        progressDialog.show();
        // }
    }

    private void showHistory(JSONObject response) {

        jsonObject = new JSONObject();

        dataBillHistorys = new ArrayList<>();
        Bills bills = (gson.fromJson(response.toString(), Bills.class));
        if(bills.getBillResponses()==null){
            //textViewNoPlans.setVisibility(View.VISIBLE);
            txtBillStatus.setVisibility(View.VISIBLE);
        }else {

            BillResponse billResponse;
            txtBillStatus.setVisibility(View.GONE);
            for(int i=bills.getBillResponses().length;i>0;i--){
                billResponse=bills.getBillResponses()[i-1];
                dataBillHistorys.add(new DataBillHistory(billResponse.getPaymentMode(),billResponse.getCollectionAmt(),billResponse.getTransId(),billResponse.getCollectionDate()));

            }
//            for (BillResponse billResponse : bills.getBillResponses()) {
//
//                dataBillHistorys.add(new DataBillHistory(billResponse.getPaymentMode(),billResponse.getCollectionAmt(),billResponse.getTransId(),billResponse.getCollectionDate()));
//
//            }


            AdapterBillHistory adapter = new AdapterBillHistory(dataBillHistorys,this);
            rv.setAdapter(adapter);
        }



    }

//    private void initializeData(){
//        dataBillHistorys = new ArrayList<>();
//        dataBillHistorys.add(new DataBillHistory("Raju", "100","675","2 dec 2016"));
//        dataBillHistorys.add(new DataBillHistory("Biru", "100","575","2 Mar 2016"));
//        dataBillHistorys.add(new DataBillHistory("Kiran", "100","775","2 Feb 2016"));
//        dataBillHistorys.add(new DataBillHistory("Sam", "100","875","2 Jan 2016"));
//
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actionButton:
                super.onBackPressed();
                break;
        }
    }
}
