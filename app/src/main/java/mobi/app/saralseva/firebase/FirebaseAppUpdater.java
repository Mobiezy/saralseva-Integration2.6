package mobi.app.saralseva.firebase;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import mobi.app.saralseva.activities.BaseApp;

/**
 * Created by kumardev on 4/3/2017.
 */

public class FirebaseAppUpdater {

    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
    public static final String KEY_CURRENT_VERSION = "force_update_current_version";
    public static final String KEY_UPDATE_URL = "force_update_store_url";

    private  Context context;
    private  OnUpdateNeededListener onUpdateNeededListener;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }



    public FirebaseAppUpdater(@NonNull Context context,
                              OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public  void check(){
        FirebaseRemoteConfig firebaseRemoteConfig=FirebaseRemoteConfig.getInstance();
        if(firebaseRemoteConfig.getBoolean(KEY_UPDATE_REQUIRED)){
            String currentVersion = firebaseRemoteConfig.getString(KEY_CURRENT_VERSION);
            String appVersion = getAppVersion(context);
            String updateUrl = firebaseRemoteConfig.getString(KEY_UPDATE_URL);

            if(!TextUtils.equals(currentVersion,appVersion) && onUpdateNeededListener!=null){
                onUpdateNeededListener.onUpdateNeeded(updateUrl);
                BaseApp.appUpdatedRecently=true;

            }else{
                BaseApp.appUpdatedRecently=false;
            }
        }
    }

    public static String getAppVersion(Context context) {
        String result="";
        try {
            result=context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public FirebaseAppUpdater build() {
            return new FirebaseAppUpdater(context, onUpdateNeededListener);
        }

        public FirebaseAppUpdater check() {
            FirebaseAppUpdater forceUpdateChecker = build();
            forceUpdateChecker.check();

            return forceUpdateChecker;
        }
    }


}
