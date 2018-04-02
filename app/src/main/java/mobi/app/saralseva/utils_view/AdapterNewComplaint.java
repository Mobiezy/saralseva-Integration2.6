package mobi.app.saralseva.utils_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import mobi.app.saralseva.R;

/**
 * Created by admin on 1/27/2017.
 */

public class AdapterNewComplaint extends RecyclerView.Adapter<AdapterNewComplaint.NewComplaintViewHolder> implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    List<DataNewComplaint> dataNewComplaints;

    public AdapterNewComplaint(List<DataNewComplaint> dataNewComplaints){
        this.dataNewComplaints = dataNewComplaints;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public AdapterNewComplaint.NewComplaintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_complaint, parent, false);
        AdapterNewComplaint.NewComplaintViewHolder pvh = new AdapterNewComplaint.NewComplaintViewHolder(v);

        sharedPreferences = parent.getContext().getSharedPreferences("complaint_prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        return pvh;
    }

    @Override
    public void onBindViewHolder(AdapterNewComplaint.NewComplaintViewHolder holder, int position) {
        holder.issuedeatil1.setText(dataNewComplaints.get(position).getIssuedetail1());
        holder.issuedeatil2.setText(dataNewComplaints.get(position).getIssuedetail2());
        holder.issuedeatil3.setText(dataNewComplaints.get(position).getIssuedetail3());
        holder.issuedeatil4.setText(dataNewComplaints.get(position).getIssuedetail4());
        holder.issuedeatil1.setOnClickListener(this);
        holder.issuedeatil2.setOnClickListener(this);
        holder.issuedeatil3.setOnClickListener(this);
        holder.issuedeatil4.setOnClickListener(this);




    }

    @Override
    public int getItemCount() {
        return dataNewComplaints.size();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
//            case R.id.radioButtonNewComplaint1:
//                editor.putString("comp_type_desc",((AppCompatRadioButton) view).getText().toString());
//
//                break;
//            case R.id.radioButtonNewComplaint2:
//                editor.putString("comp_type_desc",((AppCompatRadioButton) view).getText().toString());
//                break;
//            case R.id.radioButtonNewComplaint3:
//                editor.putString("comp_type_desc",((AppCompatRadioButton) view).getText().toString());
//                break;
//            case R.id.radioButtonNewComplaint4:
//                editor.putString("comp_type_desc",((AppCompatRadioButton) view).getText().toString());
//                break;
        }

        editor.commit();

    }

    public static class NewComplaintViewHolder extends RecyclerView.ViewHolder {
     //   RadioGroup rg;
        RadioButton issuedeatil1,issuedeatil2,issuedeatil3,issuedeatil4;

        NewComplaintViewHolder(View itemView) {
            super(itemView);



      //      rg = (RadioGroup) itemView.findViewById(R.id.radioGroupNewComplaint);
//            issuedeatil1 = (RadioButton) itemView.findViewById(R.id.radioButtonNewComplaint1);
//            issuedeatil2 = (RadioButton) itemView.findViewById(R.id.radioButtonNewComplaint2);
//            issuedeatil3 = (RadioButton) itemView.findViewById(R.id.radioButtonNewComplaint3);
//            issuedeatil4 = (RadioButton) itemView.findViewById(R.id.radioButtonNewComplaint4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.print("");
                }
            });


        }
    }
}
