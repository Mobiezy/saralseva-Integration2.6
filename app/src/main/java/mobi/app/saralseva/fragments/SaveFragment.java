package mobi.app.saralseva.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.DialogFragment;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import mobi.app.saralseva.R;
import mobi.app.saralseva.activities.BaseApp;
import mobi.app.saralseva.models.RequestSingletonClass;
import mobi.app.saralseva.models.UserProfile;
import mobi.app.saralseva.utils.AlertManager;
import mobi.app.saralseva.utils.Endpoints;
import mobi.app.saralseva.utils.SharedPreferenceManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SaveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SaveFragment  extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View v;
    RadioGroup genderGroup;
    RadioButton radioMale,radioFemale,radiobtn;
    TextInputEditText tetFname,tetLname,tetDob;
    EditText etAddress,etAadhar,etEmail,etPhone;
    Button btnSave,brnCancel;
    JSONObject params = new JSONObject();

    String gender,fName,lName,dob,address,aadharNum,emailId,phoneNum;

    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;

    Calendar myCalendar = Calendar.getInstance();

    private ProgressDialog progressDialog;

    UserProfile userProfile,userSavedProfile;
    private String newAddressVal;

    public SaveFragment() {
        // Required empty public constructor
    }
    DatePickerDialog.OnDateSetListener onDateSet;


    private boolean isModal = false;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SaveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SaveFragment newInstance(String param1, String param2) {
        SaveFragment fragment = new SaveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.isModal = true;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    getDialog().cancel();
//                    return true;
//                }
//                return false;
//            }
//
//        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_save, container, false);



        genderGroup=(RadioGroup)v.findViewById(R.id.gender_grp);

        radioMale=(RadioButton)v.findViewById(R.id.radio_male);

        radioFemale=(RadioButton)v.findViewById(R.id.radio_female);
     //  radioFemale.setChecked(true);
        radioMale.setChecked(true);

        tetFname=(TextInputEditText)v.findViewById(R.id.et_fname);
        tetLname=(TextInputEditText)v.findViewById(R.id.et_lname);
        tetDob=(TextInputEditText)v.findViewById(R.id.et_dob);

        etPhone=(EditText)v.findViewById(R.id.et_phone);
        etAddress=(EditText)v.findViewById(R.id.et_address);
        //etEmail=(EditText)v.findViewById(R.id.et_email);
        etAadhar=(EditText)v.findViewById(R.id.et_aadhar);

        btnSave=(Button)v.findViewById(R.id.btnProfileSave);
  //      brnCancel=(Button)v.findViewById(R.id.btnProfileCancel);


        setFieldsWithSavedData();



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

        };





        tetDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(),date,myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();

            }
        });
        btnSave.setOnClickListener(this);
      //  brnCancel.setOnClickListener(this);

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);




        return v;
    }

    private void setFieldsWithSavedData() {
        userSavedProfile=new UserProfile();
        userSavedProfile=SharedPreferenceManager.getInstance(getActivity()).getProfileData();

        if(!userSavedProfile.getAadharNum().equals("")){
            etAadhar.setText(userSavedProfile.getAadharNum());
        }

        if(!userSavedProfile.getFirstname().equals("")){
            tetFname.setText(userSavedProfile.getFirstname());
        }

        if(!userSavedProfile.getLastname().equals("")){
            tetLname.setText(userSavedProfile.getLastname());
        }

        if(!userSavedProfile.getGender().equals("")){
            String s=userSavedProfile.getGender();
            if(s.equals("Male")){
                radioMale.setChecked(true);
            }else{
                radioFemale.setChecked(true);
            }

        }

        if(!userSavedProfile.getDob().equals("") && !userSavedProfile.getDob().equals("0000-00-00")){

            tetDob.setText(userSavedProfile.getDob());
        }

        if(!userSavedProfile.getAddress().equals("")){
            etAddress.setText(userSavedProfile.getAddress());
        }

//        if(!userSavedProfile.getMailId().equals("")){
//            etEmail.setText(userSavedProfile.getMailId());
//        }
        if(!userSavedProfile.getPhoneNum().equals("")){
            etPhone.setText(userSavedProfile.getPhoneNum());
        }




    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        tetDob.setText(sdf.format(myCalendar.getTime()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnProfileSave:
                if(BaseApp.isNetworkAvailable(getActivity())) {
                    saveProfileData();
                }else{
                    AlertManager.showAlertDialog(getActivity(),getString(R.string.alert_header_internet),getString(R.string.alert_desc_internet),false);
                }
                break;
       /*     case R.id.btnProfileCancel:

              //  FragmentManager fragmentManager=getFragmentManager();
             //   ProfileFragment profileFragment =new ProfileFragment();
           ///     fragmentManager.beginTransaction().replace(R.id.masterpage, profileFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

                break;
            case R.id.et_dob:

                break;*/
        }
    }






    private void saveProfileData() {

        getFieldValues();

        System.out.print("");





        try {
            params.put("subGender", gender);
            params.put("subFirstName", fName);
            params.put("subLastName", lName);
            params.put("subDOB", dob);
//            params.put("subAddress", address);
            params.put("subAddress", newAddressVal);
//            params.put("subEMAIL", address);
            params.put("subAadharNo", aadharNum);
            params.put("subPrimaryPhone", phoneNum);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.POST_PROFILE_INFO,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.print("");
//                        if(response.contains("SUCCESS")){
//                            if (progressDialog.isShowing())
//                                 progressDialog.dismiss();
//                            Toast.makeText(getActivity(), R.string.toast_success_save, Toast.LENGTH_SHORT).show();
//                            storeDataInPreferences();
//
//                        }else{
//                            Toast.makeText(getActivity(),"Data not saved", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }){
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                //return headers == null ? super.getHeaders() : headers;
////            }
//
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return params.toString().getBytes();
//            }
//        };




        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Endpoints.POST_PROFILE_INFO, params, //Not null.
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.print("");

                        try {
                            if(response.getString("message").equals("SUCCESS")){
                                if (progressDialog.isShowing())
                                     progressDialog.dismiss();
                                Toast.makeText(getActivity(), R.string.toast_success_save, Toast.LENGTH_SHORT).show();
                                storeDataInPreferences();
                                FragmentManager fragmentManager=getFragmentManager();
                                  ProfileFragment profileFragment =new ProfileFragment();
                                fragmentManager.beginTransaction().replace(R.id.masterpage, profileFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

                                dismiss();

                            }else{
                                Toast.makeText(getActivity(), R.string.toast_failure_save, Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dismiss();
                        }


                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });


        RequestSingletonClass.getInstance(getActivity()).addToRequestQueue(jsonObjReq);



        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.dialog_saving));
        progressDialog.show();

    }

    private void storeDataInPreferences() {
//        userProfile=new UserProfile(gender,fName,lName,dob,address,phoneNum,aadharNum);
        userProfile=new UserProfile(gender,fName,lName,dob,newAddressVal,phoneNum,aadharNum);

        SharedPreferenceManager.getInstance(getActivity()).setProfilePreferences(userProfile);


    }

    private void getFieldValues() {

        int selectedId=genderGroup.getCheckedRadioButtonId();

        if(selectedId == radioMale.getId()){
            gender=radioMale.getText().toString();
        }else{
            gender=radioFemale.getText().toString();
        }

        fName=tetFname.getText().toString();
        lName=tetLname.getText().toString();
        dob=tetDob.getText().toString();
        if(dob.length()==0){
            dob="0000-00-00";
        }
        address=etAddress.getText().toString();
      newAddressVal=address.replaceAll("\"","");

//        FirebaseMessaging.getInstance().subscribeToTopic("location_"+address);
//        emailId=etEmail.getText().toString();
        aadharNum=etAadhar.getText().toString();
        phoneNum=etPhone.getText().toString();








    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
