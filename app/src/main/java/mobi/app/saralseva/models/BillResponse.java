package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 1/28/2017.
 */

public class BillResponse {

    @SerializedName("tran_ID")
    private String transId;

    @SerializedName("collectionDate")
    private String collectionDate;

    @SerializedName("modeOfPayment")
    private String paymentMode;

    @SerializedName("collectedAmount")
    private String collectionAmt;





    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getCollectionAmt() {
        return collectionAmt;
    }

    public void setCollectionAmt(String collectionAmt) {
        this.collectionAmt = collectionAmt;
    }
}
