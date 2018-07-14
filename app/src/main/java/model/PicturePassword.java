package model;

import java.util.ArrayList;

/**
 * Created by VerenaSchlott on 05/06/18.
 */

public class PicturePassword {

    private PasswordStrength passwordStrength;
    private String passwordName;
    private String imageUri;
    private String passwordNumber;
    private Vector2 numberPosition;
    public ArrayList<WebsiteCredentials> websites;


    public PicturePassword(PasswordStrength passwordStrength, String passwordName, String imageUri, String passwordNumber, Vector2 numberPosition) {

        this.passwordStrength = passwordStrength;
        this.passwordName = passwordName;
        this.imageUri = imageUri;
        this.passwordNumber = passwordNumber;
        this.numberPosition = numberPosition;
        this.websites = new ArrayList<>();
    }

    public PasswordStrength getPasswordStrength() {
        return passwordStrength;
    }

    public void setPasswordStrength(PasswordStrength passwordStrength) {
        this.passwordStrength = passwordStrength;
    }

    public String getPasswordName() {
        return passwordName;
    }

    public void setPasswordName(String passwordName) {
        this.passwordName = passwordName;
    }

    public ArrayList<WebsiteCredentials> getWebsites() {
        return websites;
    }

    public void setWebsites(ArrayList<WebsiteCredentials> websites) {
        this.websites = websites;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getPasswordNumber() {
        return passwordNumber;
    }

    public void setPasswordNumber(String passwordNumber) {
        this.passwordNumber = passwordNumber;
    }

    public Vector2 getNumberPosition() {
        return numberPosition;
    }

    public void setNumberPosition(Vector2 numberPosition) {
        this.numberPosition = numberPosition;
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
