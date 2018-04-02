package mobi.app.saralseva.utils_view;

/**
 * Created by admin on 1/28/2017.
 */

public class DataBillHistory {
    String paymentMode;
    String amountCollected;
    String transactionID;
    String collectionTime;

    public DataBillHistory(String paymentMode, String amountCollected, String transactionID, String collectionTime) {
        this.paymentMode = paymentMode;
        this.amountCollected = amountCollected;
        this.transactionID = transactionID;
        this.collectionTime = collectionTime;
    }

    public String getAmountCollected() {
        return amountCollected;
    }

    public void setAmountCollected(String amountCollected) {
        this.amountCollected = amountCollected;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }
}
