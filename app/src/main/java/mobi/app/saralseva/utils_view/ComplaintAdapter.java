package mobi.app.saralseva.utils_view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobi.app.saralseva.R;

/**
 * Created by kumardev on 3/12/2017.
 */

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ComplaintOptionHolder> {

    List<String> complaintStrings=new ArrayList<>();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Context context;


    public ComplaintAdapter(List<String> complaintStrings) {
        this.complaintStrings = complaintStrings;
    }


    @Override
    public ComplaintOptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_complaint, parent, false);
        ComplaintAdapter.ComplaintOptionHolder cp = new ComplaintAdapter.ComplaintOptionHolder(v);

        context=parent.getContext();
        sharedPreferences = parent.getContext().getSharedPreferences("complaint_prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        return cp;

    }

    @Override
    public void onBindViewHolder(ComplaintOptionHolder holder, int position) {

        for(int i=0;i<complaintStrings.size();i++){
            final RadioButton radioButton=new RadioButton(context);
            radioButton.setText(complaintStrings.get(i));
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    editor.putString("comp_type_desc",radioButton.getText().toString());
                    editor.commit();





                }
            });
            holder.radioGroup.addView(radioButton);
        }



    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ComplaintOptionHolder extends RecyclerView.ViewHolder {

        RadioGroup radioGroup;



        public ComplaintOptionHolder(View itemView) {
            super(itemView);

            radioGroup= (RadioGroup) itemView.findViewById(R.id.radioGroupNewComplaint);



        }
    }
}
