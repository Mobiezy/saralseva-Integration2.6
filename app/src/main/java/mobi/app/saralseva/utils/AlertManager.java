package mobi.app.saralseva.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import mobi.app.saralseva.R;

/**
 * Created by kumardev on 12/10/2016.
 */

public class AlertManager {

    private Context context;
    public AlertManager(Context ctx){
        context=ctx;
    }

    public static void showAlertDialog(Context ctx,String title,String msg,Boolean status){
        AlertDialog alertDialog=new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        if(status!=null){
            alertDialog.setIcon((status) ? R.drawable.ic_check_circle_black_24dp: R.drawable.ic_highlight_off_black_24dp);
        }
        alertDialog.setButton(ctx.getString(R.string.message_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialog.show();
    }

}
