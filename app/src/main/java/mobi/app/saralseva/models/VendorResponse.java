package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 1/16/2017.
 */

public class VendorResponse {

    @SerializedName("currentMonthBillAmount")
    private String currentPlan;

    private String dueDate;

    @SerializedName("totalBillAmount")
    private String totalDue;

    @SerializedName("stb")
    private String setupBoxNumber;

    @SerializedName("subscriptionDesc")
    private String subscriptionName;

    @SerializedName("customerDueAmount")
    private String prevDue;

    @SerializedName("taxAmount")
    private String taxAmount;

    @SerializedName("lastCollectedAmount")
    private String lastCollectionAmt;

    @SerializedName("lastCollectedTime")
    private String lastCollectedDate;



    public String getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(String totalDue) {
        this.totalDue = totalDue;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(String currentPlan) {
        this.currentPlan = currentPlan;
    }

    public String getSetupBoxNumber() {
        return setupBoxNumber;
    }

    public void setSetupBoxNumber(String setupBoxNumber) {
        this.setupBoxNumber = setupBoxNumber;
    }

    public String getPrevDue() {
        return prevDue;
    }

    public void setPrevDue(String prevDue) {
        this.prevDue = prevDue;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getLastCollectionAmt() {
        return lastCollectionAmt;
    }

    public void setLastCollectionAmt(String lastCollectionAmt) {
        this.lastCollectionAmt = lastCollectionAmt;
    }

    public String getLastCollectedDate() {
        return lastCollectedDate;
    }

    public void setLastCollectedDate(String lastCollectedDate) {
        this.lastCollectedDate = lastCollectedDate;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }
}
