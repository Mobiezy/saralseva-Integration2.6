package mobi.app.saralseva.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mobi.app.saralseva.R;
import mobi.app.saralseva.utils.PayByPayUBiz;

public class PayUActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPay;
    private PayByPayUBiz payByPayUBiz = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_u);

        btnPay=(Button)findViewById(R.id.btnPay);

        btnPay.setOnClickListener(this);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        payByPayUBiz.activityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPay:
                payByPayUBiz = PayByPayUBiz.getInstance();
                payByPayUBiz.setProductionEnvironment(false);
                payByPayUBiz.execute(this, "gtKFFx", "Sagar", "sagar@gmail.india.com", "100.00"); //qTg1so
                break;
        }
    }
}
