package mobi.app.saralseva.data;

/**
 * Created by kumardev on 3/2/2017.
 */

public class DbTableConstants {

    public static abstract class SARAL_VENDOR {
        public static final String TABLE_SARAL_VENDOR = "vendors";
        public static final String COL_VENDORID = "vendor_id";
        public static final String COL_VENDORNAME = "vendor_name";
        public static final String COL_VENDORTYPE = "vendor_type";
        public static final String COL_CUSTOMERBISID = "customer_biz_id";
        public static final String COL_MERCHANTID= "merchant_id";
        public static final String COL_CUSTOMERFIRSTNAME = "cust_name";

    }

    public static abstract class SARAL_CUSTOMER {
        public static final String TABLE_SARAL_CUSTOMER = "customers";
        public static final String COL_CUSTOMERBISID = "customer_biz_id";
        public static final String COL_CUSTOMERNAME = "customer_name";
        public static final String COL_CUSTOMERPHONE = "customer_phone";
    }

    public static abstract class SARAL_NOTIFICATION{
        public static final String TABLE_SARAL_NOTIFICATION = "notifications";
        public static final String COL_NOTIFICATIONID = "notification_id";
        public static final String COL_NOTIFICATIONTEXT = "notification_text";
        public static final String COL_NOTIFICATIONTME = "notification_time";

    }
}
