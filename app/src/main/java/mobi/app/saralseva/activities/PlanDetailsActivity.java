package mobi.app.saralseva.activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.models.PlanResponse;
import mobi.app.saralseva.models.Plans;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.models.Vendor;
import mobi.app.saralseva.models.VendorResponse;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils_view.AdapterPlanDetail;
import mobi.app.saralseva.utils_view.DataPlanDetail;


public class PlanDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private List<DataPlanDetail> dataPlanDetails;
    private ImageView imageBackAction;
    private TextView titleBar;

    String vId;
    private RequestQueue requestQueue;
    JSONObject params = new JSONObject();
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private Gson gson;
    RecyclerView rv;
    private TextView textViewNoPlans;
    ImageView imgPlanStatus;
    private String custId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_details);
        Typeface robotoRegular = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Regular.ttf");
        Typeface robotoLight = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Light.ttf");
        titleBar= (TextView) findViewById(R.id.activityName);
        titleBar.setTypeface(robotoLight);
        titleBar.setText(getResources().getString(R.string.plan_details));
        imageBackAction= (ImageView) findViewById(R.id.actionButton);
        imageBackAction.setOnClickListener(this);

        rv = (RecyclerView)findViewById(R.id.planDetailRecyclerView);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
       // initializeData();
        textViewNoPlans= (TextView) findViewById(R.id.noPlans);
        textViewNoPlans.setVisibility(View.GONE);

        imgPlanStatus=(ImageView)findViewById(R.id.imageViewNodata);


        if(getIntent()!=null){
            vId=getIntent().getStringExtra("vendorId");
            custId=getIntent().getStringExtra("custId");
        }

        requestQueue= RequestSingletonClass.getInstance(this).getRequestQueue();
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson=gsonBuilder.create();

        if(BaseApp.isNetworkAvailable(this)){
            textViewNoPlans.setVisibility(View.GONE);
            imgPlanStatus.setVisibility(View.GONE);
            getPlanDetails();
        }else{
            imgPlanStatus.setVisibility(View.VISIBLE);
            textViewNoPlans.setText(getResources().getString(R.string.messge_no_internet));
            textViewNoPlans.setVisibility(View.VISIBLE);
        }




    }

    private void getPlanDetails() {
        try {
//            params.put("vendor_ID", vId);
            params.put("vendorID", vId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

       // Cache.Entry entry = requestQueue.getCache().get(Endpoints.VENDORS_DATA);
        //Cache.Entry entry = requestQueue.getCache().invalidate(Endpoints.LOGIN_DATA,true);

//        if (entry != null) {
//            try {
//                String data = new String(entry.data, "UTF-8");
//                modelData(new JSONObject(data));
////                prepareAlbums();
//                System.out.print("");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }else{



            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Endpoints.GET_PLANS_DATA, params, //Not null.
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {


                            System.out.print("");



                             modelPlanData(response);



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
            progressDialog.setMessage(getString(R.string.loading_plans));
            progressDialog.show();
       // }

      
    }

    private void modelPlanData(JSONObject response) {

        jsonObject = new JSONObject();

        dataPlanDetails = new ArrayList<>();
        Plans plans = (gson.fromJson(response.toString(), Plans.class));
        if(plans.getPlanResponses()==null || plans.getPlanResponses().length==0){
            textViewNoPlans.setText(R.string.no_plans_available);
            textViewNoPlans.setVisibility(View.VISIBLE);
        }else {

            textViewNoPlans.setVisibility(View.GONE);
            for (PlanResponse planResponse : plans.getPlanResponses()) {

                dataPlanDetails.add(new DataPlanDetail(planResponse.getPlanName(), planResponse.getBasePrice(), planResponse.getTaxAmount(), planResponse.getTotalAmopunt(),planResponse.getPlanName()));

            }

            AdapterPlanDetail adapter = new AdapterPlanDetail(dataPlanDetails,this,vId,custId);
            rv.setAdapter(adapter);
        }

    }

//    private void initializeData(){
//        dataPlanDetails = new ArrayList<>();
//        dataPlanDetails.add(new DataPlanDetail("Rapid", "1000", "100","1100","Rapid"));
//        dataPlanDetails.add(new DataPlanDetail("Fast", "2000", "100","2100","Fast"));
//        dataPlanDetails.add(new DataPlanDetail("Blaze", "2500", "100","2600","Blaze"));
//        dataPlanDetails.add(new DataPlanDetail("Storm", "3000", "100","3100","Storm"));
//
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.actionButton:
                super.onBackPressed();
               // overridePendingTransition(0,R.anim.slide_out)
                break;
        }
    }
}

