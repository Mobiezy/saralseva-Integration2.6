package mobi.app.saralseva.utils_view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.Endpoints;

/**
 * Created by admin on 1/27/2017.
 */

public class AdapterRegComplaint extends RecyclerView.Adapter<AdapterRegComplaint.RegComplaintViewHolder> {

    List<DataRegComplaint> dataRegComplaints;
    Context ctx;
    int rating;

    private RequestQueue requestQueue;
    JSONObject params = new JSONObject();
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private Gson gson;
    RatingBar ratingBar;
    Button btnRate;
    LinearLayout linearLayout;
    String currentStatus,ticketNum;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String cust_id;
    private String vendor_id;

    public AdapterRegComplaint(List<DataRegComplaint> dataRegComplaints,String vId,String cID){
        this.dataRegComplaints = dataRegComplaints;
        vendor_id=vId;
        cust_id=cID;
        requestQueue= RequestSingletonClass.getInstance(ctx).getRequestQueue();
        GsonBuilder gsonBuilder=new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson=gsonBuilder.create();

    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public AdapterRegComplaint.RegComplaintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.registred_row_layout, parent, false);
        ctx=parent.getContext();
        sharedPreferences = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        AdapterRegComplaint.RegComplaintViewHolder pvh = new AdapterRegComplaint.RegComplaintViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final AdapterRegComplaint.RegComplaintViewHolder holder, final int position) {
        holder.ticketNumber.setText(dataRegComplaints.get(position).getTicketNo());
        holder.issue.setText(dataRegComplaints.get(position).getIssue()+"- "+dataRegComplaints.get(position).getDetail());
        holder.status.setText(dataRegComplaints.get(position).getStatus());
        currentStatus=dataRegComplaints.get(position).getStatus();
        ticketNum=dataRegComplaints.get(position).getTicketNo();

        if(currentStatus.equals("CLOSED")){
           holder.linearLayout.setVisibility(View.VISIBLE);
        }else {
            holder.linearLayout.setVisibility(View.GONE);
        }

        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                //Toast.makeText(ctx, ""+ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                rating= (int) ratingBar.getRating();
                System.out.print("");
            }
        });
//        ratingBar=holder.ratingBar;
//        btnRate=holder.btnRate;
//        linearLayout=holder.linearLayout;

        holder.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String compId=dataRegComplaints.get(position).getTicketNo();
                System.out.print("");

                if(rating>0)
                    postRating(rating,compId,position);
                else
                    showAlert();
            }
        });



    }

    private void showAlert() {
    }

    private void postRating(int rating, String compId, final int position) {

        //cust_id=sharedPreferences.getString("cust_id","");

        try {
            params.put("vendorID", vendor_id);
            params.put("cmpRating", rating);
            params.put("cmpID", compId);
            params.put("custNum", cust_id);
//            params.put("cmpReviews", "good");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.POST_COMPLAINT_FEEDBACK, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        System.out.print("");

                        try {
                            if(response.getString("message").equals("FAILURE")){
                                Toast.makeText(ctx, R.string.feedback_error, Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(ctx, R.string.feedback_success, Toast.LENGTH_SHORT).show();
//                                ratingBar.setVisibility(View.GONE);
//                                btnRate.setVisibility(View.GONE);
                                dataRegComplaints.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,dataRegComplaints.size());

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

        RequestSingletonClass.getInstance(ctx).addToRequestQueue(jsonObjReq);

        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(ctx.getString(R.string.feedback_sharing));
        progressDialog.show();

    }

    @Override
    public int getItemCount() {
        return dataRegComplaints.size();
    }

    public static class RegComplaintViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView ticketNumber,issue,status;
        RatingBar ratingBar;
        Button btnRate;
        LinearLayout linearLayout;

        RegComplaintViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.regCompCardView);
            ticketNumber = (TextView)itemView.findViewById(R.id.textViewTicket);
            issue = (TextView)itemView.findViewById(R.id.textViewIssue);
            status = (TextView)itemView.findViewById(R.id.textViewStatus);
            ratingBar=(RatingBar)itemView.findViewById(R.id.ratingBar);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.linearRating);

            btnRate=(Button)itemView.findViewById(R.id.buttonRate);
        }
    }
}
