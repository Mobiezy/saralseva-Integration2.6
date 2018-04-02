package mobi.app.saralseva.utils_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.ManageServices;

/**
 * Created by admin on 12/13/2016.
 */

public class RecyclerSubcriptionView extends RecyclerView.Adapter<RecyclerSubcriptionView.MyViewHolder> {

    private static final String APP ="Mobiezy" ;
    private Context mContext;
private List<SubcriptionGrid> albumList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title, count;
    public ImageView thumbnail, overflow;
    public CardView cv;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        count = (TextView) view.findViewById(R.id.count);
        cv= (CardView) view.findViewById(R.id.card_view);

        Typeface robotoThin = Typeface.createFromAsset(mContext.getAssets(),
                "font/Roboto-Thin.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(mContext.getAssets(),
                "font/Roboto-Regular.ttf");
        Typeface robotoLight = Typeface.createFromAsset(mContext.getAssets(),
                "font/Roboto-Light.ttf");


        title.setTypeface(robotoRegular);
        count.setTypeface(robotoLight);

        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        overflow = (ImageView) view.findViewById(R.id.overflow);
//        thumbnail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent manageInetnt=new Intent(mContext, ManageServices.class);
//                mContext.startActivity(manageInetnt);
//            }
//        });
    }
}


    public RecyclerSubcriptionView(Context mContext, List<SubcriptionGrid> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SubcriptionGrid album = albumList.get(position);
      int len=  album.getVendorName().length();
      Log.v(APP,""+len);
        String vendorName=album.getVendorName();
      if(len<=24){

        vendorName=vendorName+"\n";
//          String padded =  album.getVendorName();
//          for(int i=0;i<10;i++){
//              padded += new String(" ");
//          }
//          Log.v(APP,"if"+padded);
          holder.title.setText(vendorName);
      } else{
          holder.title.setText(vendorName);
//                try{
//         String s1= album.getVendorName().substring(0,24);
//          Log.v(APP,"else"+s1);
//          holder.title.setText(s1);
//                }catch (IndexOutOfBoundsException e){
//                    e.printStackTrace();
//                }

      }

        holder.count.setText(album.getVendorType() );


        // loading album cover using Glide library
       Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageInetnt=new Intent(mContext, ManageServices.class);
                manageInetnt.putExtra("custId",album.getCustomerId());
                manageInetnt.putExtra("custName",album.getCustomerName());
                manageInetnt.putExtra("vendorId",album.getVendorId());
                manageInetnt.putExtra("merchantId",album.getMerchantId());
                manageInetnt.putExtra("vendorName",album.getVendorName());
                manageInetnt.putExtra("customerName",album.getCustomerName());
                manageInetnt.putExtra("primaryPhone",album.getPrimaryPhone());
                manageInetnt.putExtra("merchantKey",album.getMerchantKey());
                mContext.startActivity(manageInetnt);
            }
        });
     /*   holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageInetnt=new Intent(mContext, ManageServices.class);
                manageInetnt.putExtra("custId",album.getCustomerId());
                manageInetnt.putExtra("vendorId",album.getVendorId());
                manageInetnt.putExtra("merchantId",album.getMerchantId());
                mContext.startActivity(manageInetnt);
            }
        });*/


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

/**
 * Click listener for popup menu items
 */
class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

    public MyMenuItemClickListener() {
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_add_favourite:
                Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_play_next:
                Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                return true;
            default:
        }
        return false;
    }
}

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}