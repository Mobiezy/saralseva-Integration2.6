package mobi.app.saralseva.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kumardev on 3/2/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Saral.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    private static final String CREATE_SARAL_TABLE_VENDOR_LITE="CREATE TABLE "+DbTableConstants.SARAL_VENDOR.TABLE_SARAL_VENDOR + " ( "+
            DbTableConstants.SARAL_VENDOR.COL_VENDORID + INTEGER_TYPE + " PRIMARY KEY, " +
            DbTableConstants.SARAL_VENDOR.COL_VENDORNAME + TEXT_TYPE + COMMA_SEP +
            DbTableConstants.SARAL_VENDOR.COL_VENDORTYPE + TEXT_TYPE + COMMA_SEP +
            DbTableConstants.SARAL_VENDOR.COL_MERCHANTID + TEXT_TYPE + COMMA_SEP +
            DbTableConstants.SARAL_VENDOR.COL_CUSTOMERBISID + TEXT_TYPE + COMMA_SEP +
            DbTableConstants.SARAL_VENDOR.COL_CUSTOMERFIRSTNAME + TEXT_TYPE  +
            " ) ";


    private static final String CREATE_SARAL_TABLE_NOTIFICATION_LITE="CREATE TABLE "+DbTableConstants.SARAL_NOTIFICATION.TABLE_SARAL_NOTIFICATION + " ( "+
            DbTableConstants.SARAL_NOTIFICATION.COL_NOTIFICATIONID + INTEGER_TYPE + " PRIMARY KEY, " +
            DbTableConstants.SARAL_NOTIFICATION.COL_NOTIFICATIONTEXT + TEXT_TYPE + COMMA_SEP +
            DbTableConstants.SARAL_NOTIFICATION.COL_NOTIFICATIONTME + TEXT_TYPE +
            " ) ";




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SARAL_TABLE_VENDOR_LITE);
        db.execSQL(CREATE_SARAL_TABLE_NOTIFICATION_LITE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+DbTableConstants.SARAL_VENDOR.TABLE_SARAL_VENDOR);
        db.execSQL("DROP TABLE IF EXISTS "+DbTableConstants.SARAL_NOTIFICATION.TABLE_SARAL_NOTIFICATION);
        onCreate(db);

    }
}
