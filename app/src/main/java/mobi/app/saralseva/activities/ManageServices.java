package mobi.app.saralseva.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.StringDef;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
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
import com.paytm.pgsdk.PaytmPGActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import mobi.app.saralseva.R;
import mobi.app.saralseva.fragments.AddNewVendorFragment;
import mobi.app.saralseva.fragments.PayUFragment;
import mobi.app.saralseva.models.GlobalAdd;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.models.Vendor;
import mobi.app.saralseva.models.VendorResponse;
import mobi.app.saralseva.utils.AlertManager;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils.PayByPayUBiz;

import static android.R.attr.phoneNumber;
import static android.R.attr.theme;
import static android.R.id.input;

public class ManageServices extends AppCompatActivity implements View.OnClickListener {


    HorizontalAdapter horizontalAdapter;
    private List<GlobalAdd> data;

    private TextView txtCurrentPlan,txtCusName,txtCusID,txtTotalDue,titleBar;

    private RequestQueue requestQueue;
    JSONObject params = new JSONObject();
    private JSONObject jsonObject;
    private Gson gson;
    private ProgressDialog progressDialog;

    ImageView imageViewPay,imageViewCusSupport,imageViewBillDetail,imageViewPlanDetail,imageBackAction;
    TextView textViewPay,textViewCusSupport,textViewBillDetail,textViewPlanDetail;
    TextView tvCustName,tvCustId;
    CardView billcv,paycv,plancv,supcv;
    private String emailId;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String customerId;
    private String vendorId;
    private String merchantId;
    private String vendorName;

    private String cachedTotalDue;
    private String cachedPlanName;
    private String cachedVendorDetails;
    private String cachedCustName;
    private String cachedCustPhone;
    private String customerName;
    private String primaryPhone;
    private String custName;
    private String merchantKey;
    public static Activity activityContext;
    private static Pattern pattern;
    private static String email;
    private static Account[] account ;
    private static ArrayList<String> SampleArrayList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);

        Typeface robotoMedium = Typeface.createFromAsset(getApplication().getAssets(),
                "font/Roboto-Medium.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getApplication().getAssets(),
                "font/Roboto-Light.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getApplication().getAssets(),
                "font/Roboto-Regular.ttf");

        titleBar= (TextView) findViewById(R.id.activityName);
        titleBar.setTypeface(robotoLight);
//        titleBar.setText(getResources().getString(R.string.manage_services));
        imageBackAction= (ImageView) findViewById(R.id.actionButton);
        imageBackAction.setOnClickListener(this);
        pattern = Patterns.EMAIL_ADDRESS;

        SampleArrayList = new ArrayList<String>();
        activityContext=this;


        txtCurrentPlan= (TextView) findViewById(R.id.txtCurrentPlan);
     //   txtDuedate= (TextView) findViewById(R.id.txtDueDate);
        txtTotalDue= (TextView) findViewById(R.id.txtTotalDue);
        txtCusName=(TextView) findViewById(R.id.textViewCusName);
        txtCusID=(TextView) findViewById(R.id.textViewCusID);

        tvCustName=(TextView)findViewById(R.id.textViewCusName);
        tvCustId=(TextView)findViewById(R.id.textViewCusID);

        textViewPay = (TextView) findViewById(R.id.textViewPaymentDetail);
        textViewCusSupport = (TextView) findViewById(R.id.textViewSupport);
        textViewBillDetail = (TextView) findViewById(R.id.textViewBillDetail);
        textViewPlanDetail = (TextView) findViewById(R.id.textViewPlanDetail);

        txtCurrentPlan.setTypeface(robotoLight);
      //  txtDuedate.setTypeface(robotoRegular);
        txtTotalDue.setTypeface(robotoLight);

        textViewPay.setTypeface(robotoLight);
        textViewCusSupport.setTypeface(robotoLight);
        textViewBillDetail.setTypeface(robotoLight);
        textViewPlanDetail.setTypeface(robotoLight);
        txtCusName.setTypeface(robotoLight);
        txtCusID.setTypeface(robotoLight);


        imageViewPay=(ImageView)findViewById(R.id.imgViewPay);
        imageViewCusSupport=(ImageView)findViewById(R.id.imageViewCusSupport);
        imageViewBillDetail=(ImageView)findViewById(R.id.imageViewBillDetail);
        imageViewPlanDetail= (ImageView) findViewById(R.id.imageViewPlanDetail);


        billcv= (CardView) findViewById(R.id.billCArdView);
        paycv= (CardView) findViewById(R.id.payCardview);
        plancv= (CardView) findViewById(R.id.planCardView);
        supcv= (CardView) findViewById(R.id.suppCardView);


        textViewPay.setOnClickListener(this);
        textViewCusSupport.setOnClickListener(this);
        textViewBillDetail.setOnClickListener(this);
        textViewPlanDetail.setOnClickListener(this);

    /*    imageViewPay.setOnClickListener(this);
        imageViewBillDetail.setOnClickListener(this);
        imageViewCusSupport.setOnClickListener(this);
        imageViewPlanDetail.setOnClickListener(this);*/

        billcv.setOnClickListener(this);
        paycv.setOnClickListener(this);
        plancv.setOnClickListener(this);
        supcv.setOnClickListener(this);

        MobileAds.initialize(this, "ca-app-pub-9075533456353775~4753121441");

       // MobileAds.initialize(this, "ca-app-pub-2023478389400445~9371036614");

//


        AdView mAdView = (AdView) findViewById(R.id.adView2);
//
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        requestQueue= RequestSingletonClass.getInstance(this).getRequestQueue();

        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson=gsonBuilder.create();

        sharedPreferences = getSharedPreferences("prefs",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        cachedCustName=sharedPreferences.getString("cName","");
        cachedCustPhone=sharedPreferences.getString("cPhone","");

        // Subscribe to user specific Topic
        //FirebaseMessaging.getInstance().subscribeToTopic("/topics/"+cachedCustName.replaceAll("\\s",""));

//        txtCusID.setText(cachedCustPhone);
//        txtCusName.setText(cachedCustName);

        if(getIntent()!=null){
            customerId=getIntent().getStringExtra("custId");
            vendorId=getIntent().getStringExtra("vendorId");
            merchantId=getIntent().getStringExtra("merchantId");
            vendorName=getIntent().getStringExtra("vendorName");
            customerName=getIntent().getStringExtra("customerName");
            primaryPhone=getIntent().getStringExtra("primaryPhone");
            custName=getIntent().getStringExtra("custName");
            merchantKey=getIntent().getStringExtra("merchantKey");
            emailId=getIntent().getStringExtra("emailId");

//            editor.putString("cust_id",customerId);
//            editor.commit();
            titleBar.setText(vendorName);
        }

//        String s="";
//        try {
//            s=new String(customerName.getBytes(),"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        txtCusID.setText(customerName);
        txtCusID.setText(custName);
        txtCusName.setText(primaryPhone);





        if(BaseApp.isNetworkAvailable(this)){
            getSubscribedVendors();
        }else{
            //cachedVendorDetails=sharedPreferences.getString("jsonDataManage","");
            cachedPlanName=sharedPreferences.getString("cachedPlanName","");
            cachedTotalDue=sharedPreferences.getString("cachedTotalDue","");

            txtTotalDue.setText("-");
            txtCurrentPlan.setText("-");


        }


        data = fill_with_data();


        //horizontalAdapter=new HorizontalAdapter(data, getApplication());





       // LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(ManageServices.this, LinearLayoutManager.HORIZONTAL, false);
        //horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
       /* AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(i);
        Hor_RecylerView.getItemAnimator().setAddDuration(1000);
        Hor_RecylerView.setAdapter(new SlideInLeftAnimationAdapter(alphaAdapter));*/

        //horizontal_recycler_view.setAdapter(horizontalAdapter);



    }


    private void getSubscribedVendors() {
        try {
            params.put("vendorId", vendorId);
            //params.put("customerId", customerId);
            params.put("cust_num", customerId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

       // Cache.Entry entry = requestQueue.getCache().get(Endpoints.VENDORS_DATA);
       // Cache.Entry entry = requestQueue.getCache().invalidate(Endpoints.LOGIN_DATA,true);

//        if (entry != null) {
//            try {
//                final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
//                final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
//                long now = System.currentTimeMillis();
//                final long softExpire = now + cacheHitButRefreshed;
//                final long ttl = now + cacheExpired;
//                entry.softTtl=softExpire;
//                entry.ttl=ttl;
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



       // RequestSingletonClass.getInstance(this).getRequestQueue().getCache().invalidate(Endpoints.VENDORS_DATA,true);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.GET_CUST_DATA, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.print("");
                        String resp;



                        try {
                            if(response.has("message")){
                                resp=response.getString("message");
                                if(resp.equals("No Record Found")){
                                    AlertManager.showAlertDialog(ManageServices.this,getString(R.string.alert_header_vendor_info),getString(R.string.alert_desc_novendor),false);
                                    //onBackPressed();
                                }

                            }else {
                                modelData(response);

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
        });


        RequestSingletonClass.getInstance(this).addToRequestQueue(jsonObjReq);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.message_getting));
        progressDialog.show();
    }
//    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    private void modelData(JSONObject response) {
        jsonObject = new JSONObject();
        Vendor vendor = (gson.fromJson(response.toString(), Vendor.class));

        for (VendorResponse vendorResponse : vendor.getVendorResponses()) {

            //txtTotalDue.setText(vendorResponse.getTotalDue());
            txtTotalDue.setText(getResources().getString(R.string.symbol_rupee)+" "+vendorResponse.getTotalDue() +".0");


            txtCurrentPlan.setText(vendorResponse.getSubscriptionName());
//            editor.putString("cachedTotalDue",vendorResponse.getTotalDue());
//            editor.commit();
//            editor.putString("cachedPlanName",vendorResponse.getSubscriptionName());
//            editor.commit();



            try {
                jsonObject.put("setupbox_num", vendorResponse.getSetupBoxNumber());
                jsonObject.put("current_plan", vendorResponse.getCurrentPlan());
                jsonObject.put("prev_due", vendorResponse.getPrevDue());
                jsonObject.put("tax", vendorResponse.getTaxAmount());
                jsonObject.put("total_due", vendorResponse.getTotalDue());
                jsonObject.put("subscription_desc", vendorResponse.getSubscriptionName());
                jsonObject.put("last_amt", vendorResponse.getLastCollectionAmt());
                jsonObject.put("last_date", vendorResponse.getLastCollectedDate());

                editor.putString("jsonDataManage", jsonObject.toString());
                editor.commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public List<GlobalAdd> fill_with_data() {

        List<GlobalAdd> data = new ArrayList<>();

      //  data.add(new GlobalAdd( R.drawable.globaladd1, "Image 1"));
      //  data.add(new GlobalAdd( R.drawable.globaladd2, "Image 2"));
      //  data.add(new GlobalAdd( R.drawable.globaladd3, "Image 3"));
      //  data.add(new GlobalAdd( R.drawable.globaladd4, "Image 1"));


        return data;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.payCardview:
//                if(merchantId.equals("")){
//                    showPayAlert("OFFLINE");
//                }else {
//                    FragmentManager fabfragmentManager=getFragmentManager();
//                    PayUFragment payUFragment=PayUFragment.newInstance(customerId,custName,vendorName,primaryPhone,merchantId,vendorId,merchantKey);
//
//                    payUFragment.show(fabfragmentManager,"");
//
//
//
//
//                }

            case R.id.payCardview:
                if(merchantId.equals("")){
                    showPayAlert("OFFLINE");
                }else {

                    if(emailId==null || emailId.equalsIgnoreCase("")){
                        //  Log.v("Email","null email");

                        emailId=GetAccountsName();

                        Log.v("Email",""+emailId);

                    }

                    FragmentManager fabfragmentManager=getFragmentManager();
                    Toast.makeText(ManageServices.this,""+emailId+" Will be used for transaction.....",Toast.LENGTH_LONG).show();
                    PayUFragment payUFragment=PayUFragment.newInstance(customerId,custName,vendorName,primaryPhone,merchantId,vendorId,merchantKey,emailId);

                    payUFragment.show(fabfragmentManager,"");




                }

//                FragmentManager fabfragmentManager=getFragmentManager();
//                PayUFragment payUFragment=PayUFragment.newInstance(customerId,custName,primaryPhone,merchantId,vendorId);
//                payUFragment.show(fabfragmentManager,"");
                break;

            case R.id.suppCardView:
                Intent cusSupportIntent=new Intent(ManageServices.this,CusSupActivity.class);
//                editor.putString("cust_id",customerId);
//                editor.commit();
                cusSupportIntent.putExtra("vendorId",vendorId);
                cusSupportIntent.putExtra("cust_id",customerId);
                startActivity(cusSupportIntent);

                break;

            case R.id.billCArdView:
//                editor.putBoolean("pay_enable",false); // temp logic to switch from Not pay to pay
//                editor.commit();
                Intent billDetailIntent=new Intent(ManageServices.this,BillDetailActivity.class);
                if(BaseApp.isNetworkAvailable(this)){
                    cachedVendorDetails=sharedPreferences.getString("jsonDataManage","");
                    if(cachedVendorDetails!=null)
                        billDetailIntent.putExtra("customer_billData",cachedVendorDetails);
                }

                billDetailIntent.putExtra("vendorId",vendorId);
                billDetailIntent.putExtra("custId",customerId);

                startActivity(billDetailIntent);
                break;


            case R.id.planCardView:
                Intent planDetailIntent=new Intent(ManageServices.this,PlanDetailsActivity.class);
                planDetailIntent.putExtra("vendorId",vendorId);
                planDetailIntent.putExtra("custId",customerId);
                startActivity(planDetailIntent);
                break;
            case R.id.actionButton:
                super.onBackPressed();
                break;

        }
    }

    private String GetAccountsName() {
        {
            try {
                account = AccountManager.get(ManageServices.this).getAccounts();
                Log.v("Email","getaccmethod"+emailId);

                //  Toast.makeText(ManageServices.this, "" + account, Toast.LENGTH_LONG).show();

            } catch (SecurityException e) {

            }

            for (Account TempAccount : account) {

                if (pattern.matcher(TempAccount.name).matches()) {

                    SampleArrayList.add(TempAccount.name);
                    email = SampleArrayList.get(0);
                    Log.v("Email", "" + email);
                }
            }
            return email;
        }

    }

    private void showPayAlert(String tag) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.online_payment);
        if(tag.equals("OFFLINE")){
            alert.setMessage(R.string.vendor_no_online_payment);
            alert.setNegativeButton(R.string.message_setting_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editor.putBoolean("pay_enable",false);
                    editor.commit();
                    dialog.dismiss();
                }
            });
        }else {
            alert.setMessage(R.string.pay_message_enable);


            alert.setPositiveButton(R.string.message_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editor.putBoolean("pay_enable", true);
                    editor.commit();
                    dialog.dismiss();
                }
            });

            alert.setNegativeButton(R.string.message_setting_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    editor.putBoolean("pay_enable", false);
                    editor.commit();
                    dialog.dismiss();
                }
            });
        }

        alert.show();
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        List<GlobalAdd> horizontalList = Collections.emptyList();
        Context context;


        public HorizontalAdapter(List<GlobalAdd> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            public MyViewHolder(View view) {
                super(view);
                imageView=(ImageView) view.findViewById(R.id.imageView1);

            }
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.imageView.setImageResource(horizontalList.get(position).imageId);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    String list = horizontalList.get(position).txt.toString();
               //     Snackbar.make(v, list, Snackbar.LENGTH_SHORT).show();
                 //   Toast.makeText(ManageServices.this, list, Toast.LENGTH_SHORT).show();
                }

            });

        }


        @Override
        public int getItemCount()
        {
            return horizontalList.size();
        }
    }
}
