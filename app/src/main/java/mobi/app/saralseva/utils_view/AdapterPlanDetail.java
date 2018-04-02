package mobi.app.saralseva.utils_view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.PlanDetailsActivity;
import mobi.app.saralseva.models.Complaints;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.Endpoints;

/**
 * Created by admin on 1/27/2017.
 */

public class AdapterPlanDetail extends RecyclerView.Adapter<AdapterPlanDetail.PlanDetailViewHolder> {

    List<DataPlanDetail> dataPlanDetails;

    Context context;
    String vendorId,custId;
    private SharedPreferences sharedPreferences_login;
    private ProgressDialog progressDialog;


    public AdapterPlanDetail(List<DataPlanDetail> dataPlanDetails, PlanDetailsActivity planDetailsActivity, String vId,String cId){
        this.dataPlanDetails = dataPlanDetails;
        context=planDetailsActivity;
        vendorId=vId;
        custId=cId;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public PlanDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_detail, parent, false);
        PlanDetailViewHolder pvh = new PlanDetailViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(final PlanDetailViewHolder holder, int position) {


        holder.planName.setText(dataPlanDetails.get(position).getPlanName());
        holder.basePrice.setText(dataPlanDetails.get(position).getBasePrice());
        holder.tax.setText(dataPlanDetails.get(position).getTax());
        holder.total.setText(dataPlanDetails.get(position).getTotal());
        char[] charAr=dataPlanDetails.get(position).getPlanInitial().toCharArray();
        char Ar=Character.toUpperCase(charAr[0]);
        holder.initialText.setText(Ar+"");
//        holder.cvPlans.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                notifyDataSetChanged();
////                if(holder.cvrequestPlan.getVisibility()==View.VISIBLE){
////                    holder.cvrequestPlan.setVisibility(View.GONE);
////                }else{
////                    holder.cvrequestPlan.setVisibility(View.VISIBLE);
////                }
//
//                showPlanRequestDialog(holder.planName.getText().toString());
//
//            }
//        });

      /*  holder.imgRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlanRequestDialog(holder.planName.getText().toString());
            }
        });*/
        holder.cvrequestPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlanRequestDialog(holder.planName.getText().toString());
            }
        });

    }

    private void showPlanRequestDialog(final String planName) {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(R.string.title_request_plan);

        alert.setMessage(R.string.desc_requestplan);


        alert.setPositiveButton(R.string.message_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                sendRequest(planName);
            }
        });

        alert.setNegativeButton(R.string.message_setting_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();
            }
        });

        alert.show();


    }

    private void sendRequest(String planName) {


        sharedPreferences_login = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);

            Random rand = new Random();
            int randNum = rand.nextInt(10000);


            //String cust_id=sharedPreferences_login.getString("cust_id","");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());

        JSONObject params = new JSONObject();
//            try {
////            params.put("mcomp_id","4778BTM13");
//                params.put("mcomp_id",randNum);
////            params.put("mcust_id","13");
//                params.put("mcust_id",cust_id);
////          p  params.put("mcmp_Type","PAYMENT");
//                params.put("mcmp_Type","PLAN_REQUEST");
//
//                params.put("moper_id",vendorId);
////            params.put("mcmp_detail","PAYMENT PAID LAST MONTH");
//                params.put("mcmp_detail","Request for the plan");
//                params.put("remarks","Plan name "+ planName);
//
//                params.put("magent_id","4");
////            params.put("mcomp_date","2017-01-12 12:33:55");
//                params.put("mcomp_date",currentDateandTime);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        try {
            params.put("cmpID",randNum);
            params.put("custNum",custId);
            params.put("cmpType","PLAN REQUEST");

            params.put("vendorID",vendorId);
            params.put("cmpDetail","Request for the plan");
            params.put("cmpRemarks","Plan name "+ planName);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONArray paramsArray = new JSONArray();
            paramsArray.put(params);

//        JSONObject mainObj=new JSONObject();
//        try {
//            mainObj.put("complaintsList",paramsArray);
    //        } catch (JSONException e) {
    //            e.printStackTrace();
//        }


            System.out.print("");

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    Endpoints.POST_COMPLAINT, params, //Not null.
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {


                            System.out.print("");




                            try {
                                if(response.getString("message").equals("SUCCESS")){
                                    Toast.makeText(context, R.string.toast_success_request, Toast.LENGTH_SHORT).show();
                                  

                                }else{
                                    Toast.makeText(context, R.string.toast_failure_requestplan, Toast.LENGTH_SHORT).show();
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

            RequestSingletonClass.getInstance(context).addToRequestQueue(jsonObjReq);

            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.dialog_prompt_wait));
            progressDialog.show();


    }

    @Override
    public int getItemCount() {
        return dataPlanDetails.size();
    }

    public static class PlanDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cvPlans,cvrequestPlan;
        TextView planName,basePrice,tax,total,initialText;
        ImageView imgRequest;

        PlanDetailViewHolder(View itemView) {
            super(itemView);
            Typeface robotoRegular = Typeface.createFromAsset(itemView.getContext().getAssets(),
                    "font/Roboto-Regular.ttf");
            Typeface robotoMedium = Typeface.createFromAsset(itemView.getContext().getAssets(),
                    "font/Roboto-Medium.ttf");
            cvPlans = (CardView)itemView.findViewById(R.id.planDetailCardView);
            cvPlans.setOnClickListener(this);
            cvrequestPlan= (CardView) itemView.findViewById(R.id.planDetailCardView);
            planName = (TextView)itemView.findViewById(R.id.textViewPlanName);
            planName.setTypeface(robotoMedium);
            basePrice = (TextView)itemView.findViewById(R.id.textViewBasePrice);
            basePrice.setTypeface(robotoRegular);
            tax = (TextView)itemView.findViewById(R.id.textViewTax);
            tax.setTypeface(robotoRegular);
            total = (TextView)itemView.findViewById(R.id.textViewTotal);
            total.setTypeface(robotoRegular);
            initialText = (TextView)itemView.findViewById(R.id.textViewInitial);
         //   imgRequest=(ImageView)itemView.findViewById(R.id.imgvw_plan_request);
          //  initialText.setTypeface(robotoRegular);

        }



        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.planDetailCardView:

                    break;
            }

        }
    }
}
