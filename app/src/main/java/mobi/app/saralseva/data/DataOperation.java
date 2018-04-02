package mobi.app.saralseva.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mobi.app.saralseva.models.LoginResponse;
import mobi.app.saralseva.models.Notification;

/**
 * Created by kumardev on 3/2/2017.
 */

public class DataOperation {
    Context context;
    DbHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;

    public DataOperation(Context context){
        this.context=context;
    }

    public void addVendorInDB(LoginResponse loginResponse){
        dbHelper=new DbHelper(context);
        sqLiteDatabase=dbHelper.getWritableDatabase();

        ContentValues contentValues=new ContentValues();
        contentValues.put(DbTableConstants.SARAL_VENDOR.COL_VENDORID,loginResponse.getVendorID());
        contentValues.put(DbTableConstants.SARAL_VENDOR.COL_VENDORNAME,loginResponse.getVendorCompanyName());
        contentValues.put(DbTableConstants.SARAL_VENDOR.COL_VENDORTYPE,loginResponse.getBusinessType());
        contentValues.put(DbTableConstants.SARAL_VENDOR.COL_MERCHANTID,loginResponse.getMerchantId());
        contentValues.put(DbTableConstants.SARAL_VENDOR.COL_CUSTOMERBISID,loginResponse.getCustomerID());
        contentValues.put(DbTableConstants.SARAL_VENDOR.COL_CUSTOMERFIRSTNAME,loginResponse.getCustomerFirstName());

        sqLiteDatabase.insert(DbTableConstants.SARAL_VENDOR.TABLE_SARAL_VENDOR,null,contentValues);

        sqLiteDatabase.close();
    }

    public List<LoginResponse> getAllVendors(){
        dbHelper=new DbHelper(context);
        sqLiteDatabase=dbHelper.getWritableDatabase();
        List<LoginResponse> loginResponses=new ArrayList<LoginResponse>();

        String s= "SELECT * FROM "+DbTableConstants.SARAL_VENDOR.TABLE_SARAL_VENDOR;
        Cursor cursor=sqLiteDatabase.rawQuery(s,null);

        if(cursor.moveToFirst()){
            do{
                LoginResponse loginResponse=new LoginResponse();
                loginResponse.setVendorID(cursor.getString(0));
                loginResponse.setVendorCompanyName(cursor.getString(1));
                loginResponse.setBusinessType(cursor.getString(2));
                loginResponse.setMerchantId(cursor.getString(3));
                loginResponse.setCustomerID(cursor.getString(4));
                loginResponse.setCustomerFirstName(cursor.getString(5));
                loginResponses.add(loginResponse);

            }while (cursor.moveToNext());
        }

        return loginResponses;
    }

    public void addNotification(Notification notification){
        dbHelper=new DbHelper(context);
        sqLiteDatabase=dbHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(String.valueOf(DbTableConstants.SARAL_NOTIFICATION.COL_NOTIFICATIONID),notification.getNotificationId());
        contentValues.put(DbTableConstants.SARAL_NOTIFICATION.COL_NOTIFICATIONTEXT,notification.getNotificationText());
        contentValues.put(DbTableConstants.SARAL_NOTIFICATION.COL_NOTIFICATIONTME,notification.getNotificationTime());

        sqLiteDatabase.insert(DbTableConstants.SARAL_NOTIFICATION.TABLE_SARAL_NOTIFICATION,null,contentValues);

        sqLiteDatabase.close();



    }

    public List<Notification> getNotifications(){

        dbHelper=new DbHelper(context);
        sqLiteDatabase=dbHelper.getWritableDatabase();
        List<Notification> notifications=new ArrayList<Notification>();

        String s= "SELECT * FROM "+DbTableConstants.SARAL_NOTIFICATION.TABLE_SARAL_NOTIFICATION;
        Cursor cursor=sqLiteDatabase.rawQuery(s,null);

        if(cursor.moveToFirst()){
            do{
                Notification notification=new Notification();
                notification.setNotificationId(Integer.parseInt(cursor.getString(0)));
                notification.setNotificationText(cursor.getString(1));
                notification.setNotificationTime(cursor.getString(2));

                notifications.add(notification);

            }while (cursor.moveToNext());
        }

        return notifications;

    }

    public void deleteNotification(int id){
        dbHelper=new DbHelper(context);
        sqLiteDatabase=dbHelper.getWritableDatabase();

        sqLiteDatabase.delete(DbTableConstants.SARAL_NOTIFICATION.TABLE_SARAL_NOTIFICATION,DbTableConstants.SARAL_NOTIFICATION.COL_NOTIFICATIONID+"= ?", new String[]{Integer.toString(id)});
    }
}
