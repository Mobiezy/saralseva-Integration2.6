package mobi.app.saralseva.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;

import mobi.app.saralseva.R;

public class PayActivity extends AppCompatActivity {



    PaytmPGService Service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Service = PaytmPGService.getStagingService();

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("REQUEST_TYPE", "DEFAULT");
        paramMap.put("ORDER_ID", "TEST");
        paramMap.put("MID", "DIY12386817555501617");
        paramMap.put("CUST_ID","CUST110");
        paramMap.put("CHANNEL_ID", "WAP");
        paramMap.put("INDUSTRY_TYPE_ID", "Retail");
        paramMap.put("WEBSITE", "DIYtestingwap");
        paramMap.put("TXN_AMOUNT", "1.0");
        paramMap.put("CALLBACK_URL ", "https://mobicollector.com/paytm_mob/verifyChecksum.php");

        PaytmOrder Order = new PaytmOrder(paramMap);
        //Create new Merchant Object having all merchant configuration.

        //PaytmMerchant Merchant = new PaytmMerchant( "http://hostname/<checksum-gerneration-URL>", " http://hostname/<checksum-verification-URL>");
        PaytmMerchant Merchant = new PaytmMerchant(
                "http://mobicollector.com/paytm_mob/generateChecksum.php",
                "http://mobicollector.com/paytm_mob/verifyChecksum.php");

        //Set PaytmOrder and PaytmMerchant objects. Call this method and set both objects before starting transaction.
        Service.initialize(Order, Merchant, null);

        //Start the Payment Transaction. Before starting the transaction ensure that initialize method is called.
        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback()
        {
            @Override
            public void someUIErrorOccurred(String inErrorMessage)
            {
                // Some UI Error Occurred in Payment Gateway Activity.
                // This may be due to initialization of views in Payment Gateway Activity or may be due to initialization of webview.
                // Error Message details the error occurred.

            }

            @Override
            public void onTransactionSuccess(Bundle  inResponse)
            {
                // After successful transaction this method gets called.
                // Response bundle contains the merchant response parameters.
                Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTransactionFailure(String inErrorMessage, Bundle  inResponse)
            {
                // This method gets called if transaction failed.
                // Here in this case transaction is completed, but with a failure.
                // Error Message describes the reason for failure.
                // Response bundle contains the merchant response parameters.

                Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void networkNotAvailable()
            {
                // If network is not available, then this method gets called.
            }

            @Override
            public void clientAuthenticationFailed(String  inErrorMessage)
            {
                // This method gets called if client authentication failed.
                // Failure may be due to following reasons
                //      1. Server error or downtime.
                //      2. Server unable to generate checksum or checksum response is
                //         not in proper format.
                //      3. Server failed to authenticate that client. That is value of
                //         payt_STATUS is 2.
                // Error Message describes the reason for failure.
            }

            @Override
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingURL)
            {
                // This page gets called if some error occurred while loading some URL in Webview.
                // Error Code and Error Message describes the error.
                // Failing URL is the URL that failed to load.
            }

            @Override
            public void onBackPressedCancelTransaction() {

            }
        });





    }
}
