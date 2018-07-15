package model;

public class WebsiteCredentials {
    public String mName;
    public String mWebsite;
    public String mUserName;
    public String mPassword;

    public WebsiteCredentials(String name, String website, String userName, String password) {
        mName = name;
        mWebsite = website;
        mUserName = userName;
        mPassword = password;
    }

    public String getName() {
        return mName;
    }
}