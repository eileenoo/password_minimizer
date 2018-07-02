package model;

import java.util.ArrayList;

/**
 * Created by VerenaSchlott on 05/06/18.
 */

public class PicturePassword {

    // TODO: Feel free to modifiy
    public String passwordName;
    public PasswordStrength passwordStrength;
    public String imagePath;
    public ArrayList<WebsiteCredentials> websites;

    public PicturePassword(String passwordName, PasswordStrength passwordStrength, String imagePath) {
        this.passwordName = passwordName;
        this.passwordStrength = passwordStrength;
        this.imagePath = imagePath;
        this.websites = new ArrayList<>();
    }

    public String getPasswordName() {
        return passwordName;
    }

    public void setPasswordName(String passwordName) {
        this.passwordName = passwordName;
    }

    public PasswordStrength getPasswordStrength() {
        return passwordStrength;
    }

    public void setPasswordStrength(PasswordStrength passwordStrength) {
        this.passwordStrength = passwordStrength;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void addWebsite(WebsiteCredentials websiteCredentials) {
        if (getWebsiteByName(websiteCredentials.getName()) == -1) {
            websites.add(websiteCredentials);
        }
    }

    public void removeWebsite(WebsiteCredentials websiteCredentials) {
        int websiteIndex = getWebsiteByName(websiteCredentials.getName());
        if (websiteIndex != -1) {
            websites.remove(websiteIndex);
        }
    }

    public int getWebsiteByName(String name) {
        for (int i = 0; i < websites.size(); i++) {
            if (websites.get(i).getName() == name) {
                return i;
            }
        }

        return -1;
    }
}
