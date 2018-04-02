package mobi.app.saralseva.utils_view;

/**
 * Created by admin on 1/27/2017.
 */

public class DataRegComplaint {
    String ticketNo;
    String issue;
    String detail;
    String status;

    public DataRegComplaint(String ticketNo, String issue, String detail, String status) {
        this.ticketNo = ticketNo;
        this.issue = issue;
        this.status = status;
        this.detail = detail;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
