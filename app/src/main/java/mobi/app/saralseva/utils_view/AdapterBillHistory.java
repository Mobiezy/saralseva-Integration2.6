package mobi.app.saralseva.utils_view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mobi.app.saralseva.R;

/**
 * Created by admin on 1/28/2017.
 */

public class AdapterBillHistory  extends RecyclerView.Adapter<AdapterBillHistory.BillHistoryViewHolder> {

    List<DataBillHistory> dataBillHistroys;

    Context context;
    SimpleDateFormat sdf;
    Date newDate;
    Date billdate;
    String formattedDate;

    DateFormat readFormat;

    DateFormat writeFormat;

    public AdapterBillHistory(List<DataBillHistory> dataBillHistroys,Context context){
        this.dataBillHistroys = dataBillHistroys;
        this.context=context;
        readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        writeFormat = new SimpleDateFormat("MMM dd,yyyy");


    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public BillHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_history, parent, false);
        BillHistoryViewHolder pvh = new BillHistoryViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BillHistoryViewHolder holder, int position) {
//        holder.customerID.setText(dataBillHistroys.get(position).getCustomerID());
//        holder.customerName.setText(dataBillHistroys.get(position).getCustomerName());
        holder.paymentMode.setText(dataBillHistroys.get(position).getPaymentMode());
        holder.amountCollected.setText(context.getResources().getString(R.string.symbol_rupee)+" "+dataBillHistroys.get(position).getAmountCollected());
        holder.transactionID.setText(dataBillHistroys.get(position).getTransactionID());
        try {
            billdate= readFormat.parse(dataBillHistroys.get(position).getCollectionTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        holder.collectionTime.setText(dataBillHistroys.get(position).getCollectionTime());
        if(billdate!=null)
        formattedDate=writeFormat.format(billdate);
        holder.collectionTime.setText(formattedDate);

    }

    @Override
    public int getItemCount() {
        return dataBillHistroys.size();
    }

    public static class BillHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView paymentMode;
        CardView cv;
        TextView customerID,customerName,amountCollected,transactionID,collectionTime;

        BillHistoryViewHolder(View itemView) {
            super(itemView);
            Typeface robotoThin = Typeface.createFromAsset(itemView.getContext().getAssets(),
                    "font/Roboto-Thin.ttf");
            Typeface robotoLight = Typeface.createFromAsset(itemView.getContext().getAssets(),
                    "font/Roboto-Light.ttf");
            Typeface robotoRegular = Typeface.createFromAsset(itemView.getContext().getAssets(),
                    "font/Roboto-Regular.ttf");
            cv = (CardView)itemView.findViewById(R.id.billHistoryRecyclerView);
            //customerID = (TextView)itemView.findViewById(R.id.textViewCusID);
            paymentMode = (TextView)itemView.findViewById(R.id.textViewPaymentMode);
            amountCollected = (TextView)itemView.findViewById(R.id.textViewAmtColl);
            transactionID = (TextView)itemView.findViewById(R.id.textViewTransID);
            collectionTime = (TextView)itemView.findViewById(R.id.textViewCollTime);
            paymentMode.setTypeface(robotoRegular);
            amountCollected.setTypeface(robotoRegular);
            transactionID.setTypeface(robotoLight);
            collectionTime.setTypeface(robotoLight);

        }
    }
}
