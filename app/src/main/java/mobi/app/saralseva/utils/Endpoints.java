package mobi.app.saralseva.utils;

/**
 * Created by kumardev on 1/16/2017.
 */

public class Endpoints {



    public static final String GET_LOGIN_DATA = "https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/getValidateCustomer";




    public static final String GET_CUST_DATA = "https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/getCustBillDetails";

    public static final String PLANS_DATA = "http://cableguyapp.com/SaralSeva/getPlanDetails.php";

    public static final String GET_PLANS_DATA = "https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/getPlanDetails";

    public static final String POST_COMPLAINT = "https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/putRegisterComplaint";


    public static final String GET_COMPLAINTS = "https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/getRegisteredComplaint";



    public static final String GET_BILL_HISTORY = "https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/getCustBillHistroy";



    public static final String POST_COMPLAINT_FEEDBACK = "https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/getCompRatingUpdate";


    public static final String POST_PROFILE_INFO= "https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/putUsrProfileUpdate";


    public static final String OTP_URL= "https://control.msg91.com/api/sendotp.php";


    public static final String OTP_VERIFY_URL= "https://control.msg91.com/api/verifyRequestOTP.php";


    public static final String OTP_RESEND_URL= "https://control.msg91.com/api/retryotp.php";


    public static final String PAYU_HASH= " https://cmnc7utsk2.execute-api.us-west-2.amazonaws.com/Production/GeneratePayUHash";

    public static final String PAYU_GET_HASH= "https://wihkj1rpnl.execute-api.us-west-2.amazonaws.com/PayUmoney/generatePayUmoneyHash";






}
