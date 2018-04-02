package mobi.app.saralseva.models;

/**
 * Created by kumardev on 3/11/2017.
 */

public class UserProfile {
    private String gender;
    private String firstname;
    private String lastname;
    private String dob;
    private String address;
    private String phoneNum;
    private String aadharNum;

    public UserProfile() {
    }

    public UserProfile(String gender, String firstname, String lastname, String dob, String address,  String phoneNum, String aadharNum) {
        this.gender = gender;
        this.firstname = firstname;
        this.lastname = lastname;
        this.dob = dob;
        this.address = address;
        this.phoneNum = phoneNum;
        this.aadharNum = aadharNum;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAadharNum() {
        return aadharNum;
    }

    public void setAadharNum(String aadharNum) {
        this.aadharNum = aadharNum;
    }
}
