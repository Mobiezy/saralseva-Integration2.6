package mobi.app.saralseva.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import mobi.app.saralseva.R;
import mobi.app.saralseva.models.UserProfile;
import mobi.app.saralseva.utils.SharedPreferenceManager;

/**
 * Created by admin on 9/22/2017.
 */

public class AuthActivity_firebase extends AppCompatActivity {
    private static final String TAG ="PigMy" ;
    Button submit,submitOtp,resendOtp;
    EditText Enter_mob,Enter_otp;
    TextView mobileText,OtpText,enter_mobno_text;
    private FirebaseAuth mAuth;
    String phoneNumber;
    String mobile_number;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    // [END declare_auth]

    boolean mVerificationInProgress = false;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_firebase);
        submit=(Button) findViewById(R.id.enter_mob);
        Enter_mob=(EditText) findViewById(R.id.et_mob);
        Enter_otp=(EditText) findViewById(R.id.et_otp);
        submitOtp=(Button) findViewById(R.id.enter_otp);
        mobileText=(TextView) findViewById(R.id.viewmb);
        OtpText=(TextView) findViewById(R.id.tv_otp);
        resendOtp=(Button) findViewById(R.id.resen_otp);
        enter_mobno_text=(TextView) findViewById(R.id.enter_mobno_text) ;
        mAuth = FirebaseAuth.getInstance();
        Typeface FontStyle = Typeface.createFromAsset(getApplicationContext().getAssets(),
                getString(R.string.fontname));
        mobileText.setTypeface(FontStyle,Typeface.NORMAL);
        OtpText.setTypeface(FontStyle,Typeface.NORMAL);
        enter_mobno_text.setTypeface(FontStyle,Typeface.NORMAL);
        userProfile=new UserProfile();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              checkForMessageReadPermission();
            }
        }, 1000);


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
              //  SharedPreferenceManager.getInstance(AuthActivity_firebase.this).setPrimaryPhoneNumber(Enter_mob.getText().toString());
                Log.d(TAG, "onVerificationCompleted:" + credential);


            }



            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Invalid Mobile Number.",
                            Snackbar.LENGTH_SHORT).show();
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                Enter_mob.setVisibility(View.GONE);
                submit.setVisibility(View.GONE);
                mobileText.setVisibility(View.GONE);
                enter_mobno_text.setVisibility(View.GONE);
                Enter_otp.setVisibility(View.VISIBLE);
                submitOtp.setVisibility(View.VISIBLE);
                OtpText.setVisibility(View.VISIBLE);
                resendOtp.setVisibility(View.VISIBLE);

                // ...
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Enter_mob.getText().toString().length()==10) {

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + Enter_mob.getText().toString(),        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            AuthActivity_firebase.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                  //  SharedPreferenceManager.getInstance(AuthActivity_firebase.this).setPrimaryPhoneNumber(Enter_mob.getText().toString());
                    Toast.makeText(AuthActivity_firebase.this,getString(R.string.dgts__sending),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AuthActivity_firebase.this, R.string.valid_phone_number,Toast.LENGTH_SHORT).show();
                }
            }
        });
        submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Enter_otp.getText().toString().length()==6) {
                    phoneNumber = Enter_otp.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, Enter_otp.getText().toString());
                    Log.v(TAG, "" + credential);
                    Log.v(TAG, "" + Enter_otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
                else {
                    Toast.makeText(AuthActivity_firebase.this, R.string.blank_otp, Toast.LENGTH_SHORT).show();
                }
            }
        });
        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num=   Enter_mob.getText().toString();
              //  String num= SharedPreferenceManager.getInstance(AuthActivity_firebase.this).getPhoneNumber();
                Toast.makeText(AuthActivity_firebase.this,"Resending…",Toast.LENGTH_SHORT).show();
                resendVerificationCode(num, mResendToken);
                Log.v(TAG,""+num);
            }
        });
    }

    private void checkForMessageReadPermission()
    {
        Log.v(TAG,"check permission");
        int permissionCheck = ContextCompat.checkSelfPermission(AuthActivity_firebase.this,
                Manifest.permission.READ_SMS);

        if (ContextCompat.checkSelfPermission(AuthActivity_firebase.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(AuthActivity.this,
//                    Manifest.permission.READ_SMS)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(AuthActivity_firebase.this,
                        new String[]{Manifest.permission.READ_SMS,Manifest.permission.GET_ACCOUNTS,Manifest.permission.READ_PHONE_STATE,Manifest.permission.SYSTEM_ALERT_WINDOW},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
//            }
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            Toast.makeText(AuthActivity_firebase.this,"Verified!!!",Toast.LENGTH_SHORT).show();

                            Log.d(TAG, "number saved");
//                            editor = sharedPreferences.edit();
//                editor.putString("mobileNumber",phoneNumber);
//                editor.commit();

            //    SharedPreferenceManager.getInstance(getApplicationContext()).setPrimaryPhoneNumber(phoneNumber)SharedPreferenceManager.getInstance(AuthActivity_firebase.this).setPrimaryPhoneNumber(Enter_mob.getText().toString());
                            userProfile.setAadharNum("");
                           mobile_number= Enter_mob.getText().toString();
                userProfile.setDob("");
                userProfile.setAddress("");
                userProfile.setGender("");
                userProfile.setPhoneNum(mobile_number);
                SharedPreferenceManager.getInstance(getApplicationContext()).setProfilePreferences(userProfile);


                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("mobileNumber",mobile_number);
                            Log.v("mobiezy",mobile_number);
                i.putExtra("from","auth");
                            SharedPreferenceManager.getInstance(AuthActivity_firebase.this).setPrimaryPhoneNumber(Enter_mob.getText().toString());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(i);
//                            Intent i=new Intent(AuthActivity_firebase.this,MainActivity.class);
//                            startActivity(i);
                            finish();
                            FirebaseUser user = task.getResult().getUser();

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(AuthActivity_firebase.this,getString(R.string.wrong_otp),Toast.LENGTH_SHORT).show();
                            finish();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        Log.v(TAG,""+phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                Log.v(TAG,"otp"+message);
                Enter_otp.setText(message);

                //Do whatever you want with the code here
            }
        }
    };
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }


}
