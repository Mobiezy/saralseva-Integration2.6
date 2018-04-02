package mobi.app.saralseva.utils_view;

import java.util.List;

/**
 * Created by kumardev on 3/12/2017.
 */

public class ComplaintOptions {

    List<String> complaints;

    public ComplaintOptions(List<String> complaints) {
        this.complaints = complaints;
    }

    public List<String> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<String> complaints) {
        this.complaints = complaints;
    }
}
