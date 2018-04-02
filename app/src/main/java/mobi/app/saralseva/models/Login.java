package mobi.app.saralseva.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumardev on 12/27/2016.
 */

public class Login {

    @SerializedName("loginResponseList")
    private LoginResponse[] loginResponses;

    private String status;

    private String newCustomer;

    public LoginResponse[] getLoginResponses() {
        return loginResponses;
    }

    public void setLoginResponses(LoginResponse[] loginResponses) {
        this.loginResponses = loginResponses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(String newCustomer) {
        this.newCustomer = newCustomer;
    }
}
