package mobi.app.saralseva.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by admin on 7/17/2017.
 */

public class IncomingSmsBroadcast extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    String abcd;

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        Log.v("Pigmy","onrecive");

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                Log.v("Pigmy","pdus"+pdusObj);
                for (int i = 0; i < pdusObj.length; i++) {
                    Log.v("Pigmy","inside Loop");
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    // b=sender.endsWith("WNRCRP");  //Just to fetch otp sent from WNRCRP
                    String messageBody = smsMessage.getMessageBody();
                    abcd=messageBody.replaceAll("[^0-9]","");
                    Log.v("Pigmy", "senderNum: " + "; message: " + abcd+smsMessage);

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message",abcd);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    // Show Alert

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.v("Pigmy", "Exception smsReceiver" +e);

        }
    }

   }
