package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 1/17/2017.
 */

public class Vendor {

    @SerializedName("customerDetailsList")
    private VendorResponse[] vendorResponses;

    public VendorResponse[] getVendorResponses() {
        return vendorResponses;
    }

    public void setVendorResponses(VendorResponse[] vendorResponses) {
        this.vendorResponses = vendorResponses;
    }
}
