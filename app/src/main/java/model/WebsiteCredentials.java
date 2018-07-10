package model;

public class WebsiteCredentials {
    public String mName;
    public String mUserName;
    public String mPassword;

    public WebsiteCredentials(String name, String userName, String password) {
        mName = name;
        mUserName = userName;
        mPassword = password;
    }

    public String getName() {
        return mName;
    }
}