package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 12/27/2016.
 */

public class LoginResponse {

    private String customerFirstName;

    private String customerLastName;

    private String customerPrimaryPhone;


    @SerializedName("customerBusinessID")
    private String customerID;

    private String vendorID;

    private String businessType;



    private String vendorCompanyName;

    @SerializedName("MerchantID")

    private String merchantId;

    @SerializedName("MerchantKey")
    private String merchantkey;




    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerPrimaryPhone() {
        return customerPrimaryPhone;
    }

    public void setCustomerPrimaryPhone(String customerPrimaryPhone) {
        this.customerPrimaryPhone = customerPrimaryPhone;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getVendorCompanyName() {
        return vendorCompanyName;
    }

    public void setVendorCompanyName(String vendorCompanyName) {
        this.vendorCompanyName = vendorCompanyName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantkey() {
        return merchantkey;
    }

    public void setMerchantkey(String merchantkey) {
        this.merchantkey = merchantkey;
    }
}
