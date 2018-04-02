package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 1/28/2017.
 */

public class Complaints {

    @SerializedName("Complaints_list")
    private ComplaintResponse[] complaintResponses;

    public ComplaintResponse[] getComplaintResponses() {
        return complaintResponses;
    }

    public void setComplaintResponses(ComplaintResponse[] complaintResponses) {
        this.complaintResponses = complaintResponses;
    }
}
