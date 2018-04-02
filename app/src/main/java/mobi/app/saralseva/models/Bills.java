package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 1/28/2017.
 */

public class Bills {

    @SerializedName("cust_tran_details")
    private BillResponse[] billResponses;

    public BillResponse[] getBillResponses() {
        return billResponses;
    }

    public void setBillResponses(BillResponse[] billResponses) {
        this.billResponses = billResponses;
    }
}
