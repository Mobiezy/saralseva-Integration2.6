package mobi.app.saralseva.utils_view;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mobi.app.saralseva.R;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<String> countries;
    private ArrayList<String> timestamps;
    String stampVal;

    public DataAdapter(ArrayList<String> countries,ArrayList<String> timestamp) {
        this.countries = countries;
        this.timestamps=timestamp;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tv_country.setText(countries.get(i));
        stampVal=timestamps.get(i);
        String s[]=stampVal.split("_");

        viewHolder.tv_timestamp.setText(s[0]);
        viewHolder.tv_datestamp.setText(s[1]);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void addItem(String country) {
        countries.add(country);
        notifyItemInserted(countries.size());
    }

    public void removeItem(int position) {
        countries.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, countries.size());
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_country,tv_timestamp,tv_datestamp;
        public ViewHolder(View view) {
            super(view);
            Typeface robotoRegular = Typeface.createFromAsset(view.getContext().getAssets(),
                    "font/Roboto-Regular.ttf");
            Typeface robotoMedium = Typeface.createFromAsset(view.getContext().getAssets(),
                    "font/Roboto-Medium.ttf");
            tv_country = (TextView)view.findViewById(R.id.tv_country);
            tv_timestamp = (TextView)view.findViewById(R.id.textViewTimeStamp);
            tv_datestamp = (TextView)view.findViewById(R.id.textViewDateStamp);
            tv_country.setTypeface(robotoMedium);
            tv_timestamp.setTypeface(robotoRegular);
            tv_datestamp.setTypeface(robotoRegular);
        }
    }
}