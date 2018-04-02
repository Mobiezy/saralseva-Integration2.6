package mobi.app.saralseva.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import mobi.app.saralseva.R;

/**
 * Created by kumardev on 12/10/2016.
 */

public class ConnectionDetector {
    private Context context;
    AlertManager alertManager;

    public ConnectionDetector(Context ctx){
        context=ctx;
        alertManager=new AlertManager(context);
    }

    public boolean isConnectedToInternet(){
        boolean status=false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null){
                if(info.isConnected()){
                    //Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show();
                    //alertManager.showAlertDialog(context,"Network Info","You are connetced to internet",true);
                    status=true;
                }
            }else{
                //Toast.makeText(context, "Internet Not Connected", Toast.LENGTH_SHORT).show();
                //alertManager.showAlertDialog(context,context.getString(R.string.network_info),context.getString(R.string.network_unavailable),false);
            }


        }
        return status;
    }
}

