package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 1/28/2017.
 */

public class ComplaintResponse {

//    @SerializedName("COMP_ID")
    @SerializedName("compID")
    private String ticketNum;

//    @SerializedName("CMP_TYPE")
    @SerializedName("compType")
    private String issue;

//    @SerializedName("CMP_DETAIL")
    @SerializedName("compDetails")
    private String detail;

//    @SerializedName("COMP_STATUS")
    @SerializedName("compStatus")
    private String status;


    private String rating;

    public String getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(String ticketNum) {
        this.ticketNum = ticketNum;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
