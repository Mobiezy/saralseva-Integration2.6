package mobi.app.saralseva.utils_view;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobi.app.saralseva.R;
import mobi.app.saralseva.models.UserProfile;
import mobi.app.saralseva.utils.SharedPreferenceManager;

public class ExpandableListDataPump {


     UserProfile userProfile=new UserProfile();




    public  HashMap<String, List<String>> getData(Context context) {

        userProfile=SharedPreferenceManager.getInstance(context).getProfileData();



        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Basic = new ArrayList<>();

//        Basic.add("Name"+"-"+"test");
//        Basic.add("Gender"+"-"+"testg");
//        Basic.add("Date of Birth"+"-"+"testb");
//        Basic.add("Phone Number"+"-"+"testph");
//        Basic.add("Email Address"+"-"+"testem");
//        Basic.add("Address"+"-"+"testadd");


        if(userProfile.getFirstname().length()>1)
            Basic.add(context.getString(R.string.label_profile_name)+"/"+userProfile.getFirstname()+" "+userProfile.getLastname());

        if(userProfile.getGender().length()>1)
            Basic.add(context.getString(R.string.label_profile_gender)+"/"+userProfile.getGender());

        if(userProfile.getDob().length()>1 && !userProfile.getDob().equals("0000-00-00"))
            Basic.add(context.getString(R.string.label_profile_dob)+"/"+userProfile.getDob());

        if(userProfile.getPhoneNum().length()>1)
            Basic.add(context.getString(R.string.label_profile_phone)+"/"+userProfile.getPhoneNum());
       // Basic.add("Email Address"+"/"+userProfile.getMailId());
        if(userProfile.getAddress().length()>1)
            Basic.add(context.getString(R.string.label_profile_address)+"/"+userProfile.getAddress());


        List<String> Personal = new ArrayList<String>();
//        Personal.add("Aadhar Card Number"+"-"+"testac");
        if(userProfile.getAadharNum().length()>1)
        Personal.add(context.getString(R.string.label_profile_aadhar)+"/"+userProfile.getAadharNum());




        expandableListDetail.put(context.getString(R.string.label_profile_personal), Personal);
        expandableListDetail.put(context.getString(R.string.label_profile_basic), Basic);

        return expandableListDetail;
    }
}
