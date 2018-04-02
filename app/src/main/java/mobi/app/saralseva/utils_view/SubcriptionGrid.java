package mobi.app.saralseva.utils_view;

public class SubcriptionGrid {
    private String customerId;
    private String vendorName;
    private String vendorType;
    private String vendorId;
    private int thumbnail;
    private String merchantId;
    private String customerName;
    private String primaryPhone;
    private String merchantKey;


    public SubcriptionGrid() {
    }


    public SubcriptionGrid(String customerId, String vendorName, String vendorType, String vendorId, int thumbnail, String merchantId,String customerName,String primaryPhone, String merchantKey) {
        this.customerId = customerId;
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorId = vendorId;
        this.thumbnail = thumbnail;
        this.merchantId = merchantId;
        this.customerName = customerName;
        this.primaryPhone = primaryPhone;
        this.merchantKey = merchantKey;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getMerchantKey() {
        return merchantKey;
    }

    public void setMerchantKey(String merchantKey) {
        this.merchantKey = merchantKey;
    }
}