package mobi.app.saralseva.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mobi.app.saralseva.R;
import mobi.app.saralseva.SetupBoxNumberFragment;
import mobi.app.saralseva.fragments.AddNewVendorFragment;
import mobi.app.saralseva.utils_view.DataAdapter;

public class BillDetailActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtSetupBox;
    TextView txtCurrentPlan;
    TextView txtPrevDue;
   // *TextView txtTax;  edited on 22/12 by surbhi
   TextView txtgst,txtCGST,txtSGST;
    TextView txtTotalDue;
    TextView txtLastCollectedAmt;
    TextView txtLastCollectedDate;
    TextView txtCurrentBill;
    Button btnBillHistory;
    LinearLayout layoutSetupBox;
    private ImageView imageBackAction;
    private TextView titleBar;

    JSONObject jsonObject;
    private String vendorId;
    private String customerId;
    private TextView txtCurrentMonth;
    float tax,current_month_amt;
    LinearLayout lysgst,lygst,lycgst;


    Date billdate;
    String formattedDate;

    DateFormat readFormat;

    DateFormat writeFormat;
    private TextView textviewBadge;
    String setTopBoxNums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //*  setContentView(R.layout.activity_bill_detail);
        setContentView(R.layout.activity_bill_detail_edit);
        Typeface robotoLight = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Light.ttf");
        Typeface robotoMedium = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Medium.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Regular.ttf");
        titleBar= (TextView) findViewById(R.id.activityName);
        titleBar.setTypeface(robotoLight);
        titleBar.setText(getResources().getString(R.string.bill_details));
        imageBackAction= (ImageView) findViewById(R.id.actionButton);
        imageBackAction.setOnClickListener(this);


        readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        writeFormat = new SimpleDateFormat("MMM dd,yyyy");

        layoutSetupBox = (LinearLayout) findViewById(R.id.layoutSetbox);
        textviewBadge = (TextView) findViewById(R.id.txtSetTopBoxBadge);



        txtCurrentBill=(TextView)findViewById(R.id.textViewCurrBill);
        txtSetupBox=(TextView)findViewById(R.id.txtSetupBoxNum);
        txtCurrentPlan=(TextView)findViewById(R.id.txtCurrentPlan);
        txtPrevDue=(TextView)findViewById(R.id.txtPrevDue);
       //* txtTax=(TextView)findViewById(R.id.txtTax);
        txtTotalDue=(TextView)findViewById(R.id.txtTotalDue);
        txtLastCollectedAmt=(TextView)findViewById(R.id.txtLastCollectedAmt);
        txtLastCollectedDate=(TextView)findViewById(R.id.txtLastCollectedDate);
        btnBillHistory= (Button) findViewById(R.id.ButtonBillHistory);
        btnBillHistory.setTypeface(robotoMedium);
        txtCurrentMonth=(TextView)findViewById(R.id.txtLastmonthamt);
        txtgst=(TextView)findViewById(R.id.txtgst);
        txtCGST= (TextView)findViewById(R.id.txtCGST);
        txtSGST= (TextView)findViewById(R.id.txtSGST);
        lysgst=(LinearLayout)findViewById(R.id.lysgst);
        lygst=(LinearLayout)findViewById(R.id.lygst);
        lycgst=(LinearLayout)findViewById(R.id.lycgst);


        btnBillHistory.setOnClickListener(this);
        layoutSetupBox.setOnClickListener(this);

        if(getIntent()!=null) {
            //getIntent().getStringExtra("customer_billData");
            //jsonObject=(JSONObject)jsonObject;


                vendorId = getIntent().getStringExtra("vendorId");
                customerId = getIntent().getStringExtra("custId");

        }
        try {
            if(getIntent().hasExtra("customer_billData")) {
                jsonObject = new JSONObject(getIntent().getStringExtra("customer_billData"));


                setTopBoxNums=jsonObject.getString("setupbox_num");
                if(setTopBoxNums.equals("") || setTopBoxNums.equals("0")) {

                    textviewBadge.setVisibility(View.GONE);
                    txtSetupBox.setText("-");

                }else{
                    String[] s = setTopBoxNums.split(",");
                    textviewBadge.setVisibility(View.VISIBLE);
                    textviewBadge.setText(s.length+"");
                    txtSetupBox.setText("");
                    if(s.length==1){
                        txtSetupBox.setText(jsonObject.getString("setupbox_num"));
                        textviewBadge.setVisibility(View.GONE);
                    }
                }


                //txtSetupBox.setText(jsonObject.getString("setupbox_num"));
                txtCurrentPlan.setText(jsonObject.getString("subscription_desc"));
                current_month_amt=Float.parseFloat(jsonObject.getString("current_plan"));
                txtCurrentMonth.setText(getResources().getString(R.string.symbol_rupee)+" "+ current_month_amt);
               //* txtCurrentMonth.setText(getResources().getString(R.string.symbol_rupee)+" "+ jsonObject.getString("current_plan")+".0");

                txtPrevDue.setText(getResources().getString(R.string.symbol_rupee)+" "+ jsonObject.getString("prev_due")+".0");
                String i="0";
                if(jsonObject.getString("tax").equals("0")){

                  //  if(i.equals("0")){
                  // * txtTax.setText("Tax included");

                lysgst.setVisibility(View.GONE);
                lygst.setVisibility(View.GONE);
                lycgst.setVisibility(View.GONE);}
                else
                   //* txtTax.setText(getResources().getString(R.string.symbol_rupee)+" "+ jsonObject.getString("tax")+".0");
                 tax= Float.parseFloat(jsonObject.getString("tax"));

                txtgst.setText(getResources().getString(R.string.symbol_rupee)+" "+ tax);
                txtCGST.setText(getResources().getString(R.string.symbol_rupee)+" "+ tax/2);
                txtSGST.setText(getResources().getString(R.string.symbol_rupee)+" "+ tax/2);
                txtTotalDue.setText(getResources().getString(R.string.symbol_rupee)+" "+ jsonObject.getString("total_due")+".0");
                txtCurrentBill.setText(jsonObject.getString("total_due"));
                txtLastCollectedAmt.setText(getResources().getString(R.string.symbol_rupee)+" "+ jsonObject.getString("last_amt")+".0");
                String lastDate = jsonObject.getString("last_date");
                // Date d=parseDate(lastDate);
                try {
                    billdate= readFormat.parse(lastDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(billdate!=null)
                    formattedDate=writeFormat.format(billdate);
                txtLastCollectedDate.setText(formattedDate);
                //  txtLastCollectedDate.setText(d+"");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Date parseDate(String lastDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date date = null;
        try{
            date=format.parse(lastDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ButtonBillHistory:
                Intent billHistoryIntent=new Intent(BillDetailActivity.this,BillHistoryActivity.class);
                billHistoryIntent.putExtra("vendorId",vendorId);
                billHistoryIntent.putExtra("custId",customerId);
                startActivity(billHistoryIntent);
                break;
            case R.id.actionButton:
                super.onBackPressed();
                break;
            case R.id.layoutSetbox:
                if(textviewBadge.getVisibility()==View.VISIBLE) {
                    FragmentManager fabfragmentManager = getFragmentManager();
                    SetupBoxNumberFragment setupBoxNumberFragment = SetupBoxNumberFragment.newInstance(setTopBoxNums);
                    setupBoxNumberFragment.show(fabfragmentManager, "");
                }
        }
    }
}
