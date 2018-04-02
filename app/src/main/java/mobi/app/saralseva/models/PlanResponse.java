package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 1/28/2017.
 */

public class PlanResponse {
    @SerializedName("Plan_Name")
    private String planName;

    @SerializedName("Base_Price")
    private String basePrice;

    @SerializedName("Tax_Amount")
    private String taxAmount;

    @SerializedName("Total_Amount")
    private String totalAmopunt;

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTotalAmopunt() {
        return totalAmopunt;
    }

    public void setTotalAmopunt(String totalAmopunt) {
        this.totalAmopunt = totalAmopunt;
    }

}
