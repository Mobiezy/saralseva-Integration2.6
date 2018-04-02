package mobi.app.saralseva.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.LinkedBlockingQueue;

import mobi.app.saralseva.R;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.utils.Endpoints;

public class PayUMoneyActivity extends Activity {

	//private Button button;

	private static final String TAG = "MainActivity";
	WebView webviewPayment;
	WebView mwebview;
	TextView txtview;

	String amt;
	private String txnid;
	private String amountPay;
	private String firstname;
	private String email;
	private JSONObject productInfo;
	JSONObject params = new JSONObject();
	String key="BC50nb";

	private ProgressDialog progressDialog;
	private String haskKey;

	StringBuilder url_s;
	private JSONObject dJson;
	private String dummyHash;
	private String hash;
	private long mBackPressed;
	private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.

	String cName,cId,cPhone,cMail,mId,vId,vName,mKey;


	/*
	protected  void writeStatus(String str){
		txtview.setText(str);
	}*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payumoney);

		//button=(Button)findViewById(R.id.button1);

		if(getIntent()!=null){
			hash=getIntent().getStringExtra("hash");
			cId=getIntent().getStringExtra("managedCustId");
			vId=getIntent().getStringExtra("managedVendorId");
			mId=getIntent().getStringExtra("managedMerchantId");
			vName=getIntent().getStringExtra("managedVendorName");
			cName=getIntent().getStringExtra("managedCustName");
			cPhone=getIntent().getStringExtra("managedPrimaryPhone");
			mKey=getIntent().getStringExtra("managedMerchantKey");
		}

		//String dummyHash="key=4soAlZso&txnid=TXN_1588255255581&amount=1&productinfo={\"paymentIdentifiers\":[{\"field\":\"CompletionDate\",\"value\":\"31/10/2012\"}],\"paymentParts\":[{\"merchantId\":\"5804715\",\"name\":\"Cable\",\"description\":\"Cable_Billing\",\"commission\":\"0.20\",\"value\":\"0.80\"}]}&firstname=mobi&email=mobi@collector.com&phone=9164502515&surl=https://www.mobicollector.com&furl=https://www.payumoney.com/mobileapp/payumoney/failure.php&hash=37ef4d3888c6f23c764b19e623be76e8b459d45496e9ecc7aee07c6677c61bde0da5c038a8ebb360cf38a0fe3000d0f0079f3b30effbf7e3034e404ca4fb2db8&service_provider=payu_paisa";
		//String dummyHash="key=4soAlZso&txnid=TXN_111495358487704&amount=1&productinfo:{\"paymentIdentifiers\":[{\"field\":\"CompletionDate\",\"value\":\"31/10/2012\"}],\"paymentParts\":[{\"merchantId\":\"5804715\",\"name\":\"Cable\",\"description\":\"Cable_Billing\",\"commission\":\"0.20\",\"value\":\"0.80\"}]}&firstname=mobi&email=info@mobicollector.com&phone=9989888899&surl=https://www.mobicollector.com&furl=https://www.payumoney.com/mobileapp/payumoney/failure.php&hash=467c648083b022a995ebe37e49f260fb99aa0b9e02a32b1cacce741fd5b624672b9a97f955269604a6e6fb53acb619ac28a2b4b3794b1904bc1b71b1e649ed34&service_provider=payu_paisa";
		//String dummyHash="key=4soAlZso&txnid=TXN_111495363800414&amount=22&productinfo={\"paymentIdentifiers\":[{\"field\":\"CompletionDate\",\"value\":\"31/12/2017\"}],\"paymentParts\":[{\"merchantId\":\"\",\"name\":\"Cable\",\"description\":\"Cable_Billing\",\"commission\":\"0.00\",\"value\":\"22\"}]}&firstname=Mobi&email=info@mobicollector.com&phone=9989888899&udf12&udf2&surl=https://www.mobicollector.com//p/success.php&furl=https://www.payumoney.com/mobileapp/payumoney/failure.php&hash=298e05615ccdedda78ec79d84314073388d86a519eb2f0b2e882cb31af6e62be395a0b20a1d95f85670114f0130dde4b4df0250f167e944bea3d67d644cbd534&service_provider=payu_paisa";
		//String dummyHash="BC50nb&txnid=TXN_111495640518146&amount=22&productinfo={"paymentIdentifiers":[{"field":"CompletionDate","value":"31/12/2017"}],"paymentParts":[{"merchantId":"4825050","name":"Cable","description":"","commission":"0.00","value":"22"}]}&firstname=Mobi&email=info@mobicollector.com&phone=9989888899&udf1=2&udf2=1675&surl=https://www.mobicollector.com/p/success.php&furl=https://www.payumoney.com/mobileapp/payumoney/failure.php&hash=c2380d7971e1c682b9eb4cb60db03627e94c3b027f98b291f06aed09c066c9731f5b315392ab7856e8d578daa40945826e8daba7527f7c1a26ecf229506b4a56&service_provider=payu_paisa"




		webviewPayment = (WebView) findViewById(R.id.webView1);
		webviewPayment.getSettings().setJavaScriptEnabled(true);
		webviewPayment.getSettings().setDomStorageEnabled(true);
		webviewPayment.getSettings().setLoadWithOverviewMode(true);
		webviewPayment.getSettings().setUseWideViewPort(true);
		webviewPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webviewPayment.getSettings().setSupportMultipleWindows(true);
		webviewPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");

		url_s = new StringBuilder();


		if(mKey.equals("T")){
			url_s.append("https://test.payu.in/_payment");
		}else if(mKey.equals("P")) {
			url_s.append("https://secure.payu.in/_payment");
		}
		//url_s.append("https://test.payu.in/_payment");

//		url_s.append("https://secure.payu.in/_payment");

		Log.e(TAG, "call url " + url_s);


	//	webviewPayment.postUrl(url_s.toString(),EncodingUtils.getBytes(hash), "utf-8"));


//		key  = "BC50nb"; // your key
////
////		String salt  = "teEkuVg2";
//		String salt  = "Bwxo1cPe"; // your salt
//
//		txnid = "mbcl"+System.currentTimeMillis();
//		//txnid = "mbcl1493643224680556";
////		String amount = "200";
//		amountPay = amt;
//		firstname = "mobi";
//		email = "mobi@collector.com";
////		String productInfo = "Product1";
//		productInfo = getInfo();
//
//		dJson=new JSONObject();
//
//		try {
//			dJson.put("key",key);
//			dJson.put("txnid",txnid);
//			dJson.put("amountPay",amountPay);
//			dJson.put("productInfo",productInfo);
//			dJson.put("firstname",firstname);
//			dJson.put("email",email);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
////
////		getHashFromServer(dJson);
//
//		dummyHash=getDummyHash(dJson);



//		webviewPayment.postUrl(url_s.toString(),dummyHash.getBytes(Charset.forName("UTF-8")));
		webviewPayment.postUrl(url_s.toString(),hash.getBytes(Charset.forName("UTF-8")));

		webviewPayment.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if(url.equals("https://www.mobicollector.com/p/success.php")){
					ManageServices.activityContext.finish();

					new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

						@Override
						public void run() {
							// This method will be executed once the timer is over
							// Start your app main activity
							Intent i=new Intent(PayUMoneyActivity.this,ManageServices.class);
							//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							i.putExtra("custId",cId);
							i.putExtra("custName",cName);
							i.putExtra("vendorId",vId);
							i.putExtra("merchantId",mId);
							i.putExtra("vendorName",vName);
							i.putExtra("customerName",cName);
							i.putExtra("primaryPhone",cPhone);
							PayUMoneyActivity.this.finish();

							startActivity(i);

//							PayUMoneyActivity.this.finish();



						}
					}, 3000);

				}


			}

			@SuppressWarnings("unused")
			public void onReceivedSslError(WebView view, SslErrorHandler handler) {
				Log.e("Error", "Exception caught!");
				handler.cancel();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});


	}

	private final class PayUJavaScriptInterface {
        PayUJavaScriptInterface() {
        }

		@JavascriptInterface
        public void success( long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {

                	Toast.makeText(PayUMoneyActivity.this, "Status is txn is success "+" payment id is "+paymentId, Toast.LENGTH_LONG).show();
                	//String str="Status is txn is success "+" payment id is "+paymentId;
                  // new MainActivity().writeStatus(str);

                	TextView txtview;
                	txtview = (TextView) findViewById(R.id.textView1);
                	txtview.setText("Status is txn is success "+" payment id is "+paymentId);

					Intent i=new Intent(PayUMoneyActivity.this,ManageServices.class);
					//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);





                }
            });
        }
		@JavascriptInterface
		public void failure( long id, final String paymentId) {
			runOnUiThread(new Runnable() {
				public void run() {

					Toast.makeText(PayUMoneyActivity.this, "Status is txn is failed "+" payment id is "+paymentId, Toast.LENGTH_LONG).show();
					//String str="Status is txn is failed "+" payment id is "+paymentId;
					// new MainActivity().writeStatus(str);

					TextView txtview;
					txtview = (TextView) findViewById(R.id.textView1);
					txtview.setText("Status is txn is failed "+" payment id is "+paymentId);

				}
			});
		}

    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private String getPostString()
	{
//		String key  = "dRQuiA";
//		key  = "BC50nb"; // your key
////
////		String salt  = "teEkuVg2";
//		String salt  = "Bwxo1cPe"; // your salt
//
//		txnid = "mbcl"+System.currentTimeMillis();
////		String amount = "200";
//		amountPay = amt;
//		firstname = "mobi";
//		email = "mobi@collector.com";
////		String productInfo = "Product1";
//		productInfo = getInfo();

		StringBuilder post = new StringBuilder();
		post.append("key=");
		post.append(key);
		post.append("&");
		post.append("txnid=");
		post.append(txnid);
		post.append("&");
		post.append("amount=");
		post.append(amountPay);
		post.append("&");
		post.append("productinfo=");
		post.append(productInfo);
		post.append("&");
		post.append("firstname=");
		post.append(firstname);
		post.append("&");
		post.append("email=");
		post.append(email);
		post.append("&");
		post.append("phone=");
		post.append("9989873322");
		post.append("&");
		post.append("surl=");
//		post.append("https://www.payumoney.com/mobileapp/payumoney/success.php");
		post.append("https://mobicollector.com/p/success.php");
//		post.append("https://mobicollector.com/payumoney/admin/success.php");
		//https://www.payumoney.com/mobileapp/payumoney/success.php
		//https://www.payumoney.com/mobileapp/payumoney/failure.php
		post.append("&");
		post.append("furl=");
		post.append("https://www.payumoney.com/mobileapp/payumoney/failure.php");
		post.append("&");

		post.append("hash=");


//		JSONObject dJson=new JSONObject();
//		try {
//			dJson.put("key",key);
//			dJson.put("txnid",txnid);
//			dJson.put("amountPay",amountPay);
//			dJson.put("productInfo",productInfo);
//			dJson.put("firstname",firstname);
//			dJson.put("email",email);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//
		//String dummyHash=getDummyHash(dJson);

//		post.append(haskKey);
//		post.append("&");

//		post.append(haskKey);
//		post.append("&");

		post.append(dummyHash);
		post.append("&");

		post.append("service_provider=");
		post.append("payu_paisa");
		return post.toString();





		// Commented to call web service;.. uncommment to test locally


//		StringBuilder checkSumStr = new StringBuilder();
//		/* =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt) */
//	    MessageDigest digest=null;
//	    String hash;
//	    try {
//	        digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");
//
//	        checkSumStr.append(key);
//	        checkSumStr.append("|");
//	        checkSumStr.append(txnid);
//	        checkSumStr.append("|");
//	        checkSumStr.append(amountPay);
//	        checkSumStr.append("|");
//	        checkSumStr.append(productInfo);
//	        checkSumStr.append("|");
//	        checkSumStr.append(firstname);
//	        checkSumStr.append("|");
//	        checkSumStr.append(email);
//	        checkSumStr.append("|||||||||||");
//	        checkSumStr.append(salt);
//
//	        digest.update(checkSumStr.toString().getBytes());
//
//	        hash = bytesToHexString(digest.digest());
//	    	post.append("hash=");
//	        post.append(hash);
//	        post.append("&");
//	        Log.i(TAG, "SHA result is " + hash);
//	    } catch (NoSuchAlgorithmException e1) {
//	        // TODO Auto-generated catch block
//	        e1.printStackTrace();
//	    }


	}

	private void getHashFromServer(JSONObject dJson) {

		try {
			params.put("amnt", "25");

		} catch (JSONException e) {
			e.printStackTrace();
		}

		final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				Endpoints.PAYU_HASH, params, //Not null.
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {


						System.out.print("");

						try {
							haskKey=response.getString("response");
						} catch (JSONException e) {
							e.printStackTrace();
						}


						if (progressDialog.isShowing())
							progressDialog.dismiss();

						webviewPayment.postUrl(url_s.toString(),getPostString().getBytes(Charset.forName("UTF-8")));
					}
				}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {

				if (progressDialog.isShowing())
					progressDialog.dismiss();
			}
		});


		RequestSingletonClass.getInstance(this).addToRequestQueue(jsonObjReq);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Generating Hash Please wait...");
		progressDialog.show();

	}

	private String getDummyHash(JSONObject dJson) {
		StringBuilder checkSumStr = new StringBuilder();
		/* =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt) */
		MessageDigest digest=null;
		String hash="";
		try {
			digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");

			try {
				checkSumStr.append(dJson.get("key"));
				checkSumStr.append("|");
				checkSumStr.append(dJson.get("txnid"));
				checkSumStr.append("|");
				checkSumStr.append(dJson.get("amountPay"));
				checkSumStr.append("|");
				checkSumStr.append(dJson.get("productInfo"));
				checkSumStr.append("|");
				checkSumStr.append(dJson.get("firstname"));
				checkSumStr.append("|");
				checkSumStr.append(dJson.get("email"));
				checkSumStr.append("|||||||||||");
				checkSumStr.append("Bwxo1cPe");
			} catch (JSONException e) {
				e.printStackTrace();
			}


			digest.update(checkSumStr.toString().getBytes());

			hash = bytesToHexString(digest.digest());

		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return  hash;
	}


	private JSONObject getInfo()
	{
		try {
			//create payment part object
			JSONObject productInfo = new JSONObject();

			JSONObject jsonPaymentPart = new JSONObject();
			jsonPaymentPart.put("name", "Invoice#265~11d6e214-344f-45a3-971e-e654763185ce");
			jsonPaymentPart.put("description", "Lunchcombo");
			jsonPaymentPart.put("value", "100");
			jsonPaymentPart.put("merchantId", "4825050");
			jsonPaymentPart.put("commission", "5.00");

			//create payment part array
			JSONArray jsonPaymentPartsArr = new JSONArray();
			jsonPaymentPartsArr.put(jsonPaymentPart);

			//paymentIdentifiers
//			JSONObject jsonPaymentIdent = new JSONObject();
//			jsonPaymentIdent.put("field", "CompletionDate");
//			jsonPaymentIdent.put("value", "31/10/2012");

			//create payment part array
//			JSONArray jsonPaymentIdentArr = new JSONArray();
//			jsonPaymentIdentArr.put(jsonPaymentIdent);

			productInfo.put("paymentParts", jsonPaymentPartsArr);
			//productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);

			Log.e(TAG, "product Info = " + productInfo.toString());
			return productInfo;


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private JSONObject getProductInfo()
	{
		try {
			//create payment part object
			JSONObject productInfo = new JSONObject();

			JSONObject jsonPaymentPart = new JSONObject();
			jsonPaymentPart.put("name", "TapFood");
			jsonPaymentPart.put("description", "Lunchcombo");
			jsonPaymentPart.put("value", "500");
			jsonPaymentPart.put("isRequired", "true");
			jsonPaymentPart.put("settlementEvent", "EmailConfirmation");

			//create payment part array
			JSONArray jsonPaymentPartsArr = new JSONArray();
			jsonPaymentPartsArr.put(jsonPaymentPart);

			//paymentIdentifiers
			JSONObject jsonPaymentIdent = new JSONObject();
			jsonPaymentIdent.put("field", "CompletionDate");
			jsonPaymentIdent.put("value", "31/10/2012");

			//create payment part array
			JSONArray jsonPaymentIdentArr = new JSONArray();
			jsonPaymentIdentArr.put(jsonPaymentIdent);

			productInfo.put("paymentParts", jsonPaymentPartsArr);
			productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);

			Log.e(TAG, "product Info = " + productInfo.toString());
			return productInfo;


		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');

            }
            sb.append(hex);
        }
        return sb.toString();
    }


	@Override
	public void onBackPressed() {
		int count = getFragmentManager().getBackStackEntryCount();

		if(count==0){
			if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
			{
				super.onBackPressed();
				return;
			}
			else { Toast.makeText(getBaseContext(), R.string.toast_pay_cancel, Toast.LENGTH_SHORT).show(); }

			mBackPressed = System.currentTimeMillis();
		}else{
			getFragmentManager().popBackStack();
		}

	}
}
