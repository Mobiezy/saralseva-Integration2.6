package mobi.app.saralseva.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.twitter.sdk.android.core.models.User;

import mobi.app.saralseva.models.UserProfile;

/**
 * Created by kumardev on 3/11/2017.
 */

public class SharedPreferenceManager {
    private static final String MY_PREFS="prefs";

    private SharedPreferences sharedPreferences;
    private static SharedPreferenceManager sharedPreferencesManager;

    public SharedPreferenceManager(Context context) {
        this.sharedPreferences = context.getApplicationContext().getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferenceManager getInstance(Context context){
        if(sharedPreferencesManager==null){
            sharedPreferencesManager=new SharedPreferenceManager(context);
        }
        return sharedPreferencesManager;
    }

    public void setProfilePreferences(UserProfile profile){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("gender",profile.getGender());
        editor.putString("fname",profile.getFirstname());
        editor.putString("lname",profile.getLastname());
        editor.putString("dob",profile.getDob());
        editor.putString("address",profile.getAddress());
       // editor.putString("mailId",profile.getMailId());
        editor.putString("phoneNum",profile.getPhoneNum());
        editor.putString("aadharNum",profile.getAadharNum());
        editor.commit();


    }


    public UserProfile getProfileData(){

        UserProfile userProfile=new UserProfile();
        userProfile.setGender(sharedPreferences.getString("gender",""));
        userProfile.setFirstname(sharedPreferences.getString("fname",""));
        userProfile.setLastname(sharedPreferences.getString("lname",""));
        userProfile.setDob(sharedPreferences.getString("dob",""));
        userProfile.setAddress(sharedPreferences.getString("address",""));
       // userProfile.setMailId(sharedPreferences.getString("mailId",""));
        userProfile.setPhoneNum(sharedPreferences.getString("phoneNum",""));
        userProfile.setAadharNum(sharedPreferences.getString("aadharNum",""));

        return userProfile;



    }

    public void setInitialProfile(boolean val){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("initial_profile",val);
        editor.commit();

    }

    public boolean getInitialProfile(){
       return sharedPreferences.getBoolean("initial_profile",true);
    }

    public void setPrimaryPhoneNumber(String num){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("mobileNumber",num);
        editor.commit();
    }

    public String getPhoneNumber(){
      return   sharedPreferences.getString("mobileNumber","");
    }



}
