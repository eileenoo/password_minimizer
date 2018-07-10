package model;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by VerenaSchlott on 05/06/18.
 */

public class PicturePassword {

    private PasswordStrength passwordStrength;
    private String passwordName;
    private String imageUri;
    private String passwordNumber;
    private float numPosX;
    private float numPosY;
    public ArrayList<WebsiteCredentials> websites;


    public PicturePassword(PasswordStrength passwordStrength, String passwordName, String imageUri, String passwordNumber, float numPosX, float numPosY) {

        this.passwordStrength = passwordStrength;
        this.passwordName = passwordName;
        this.imageUri = imageUri;
        this.passwordNumber = passwordNumber;
        this.numPosX = numPosX;
        this.numPosY = numPosY;
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

    public float getNumPosX() {
        return numPosX;
    }

    public void setNumPosX(float numPosX) {
        this.numPosX = numPosX;
    }

    public float getNumPosY() {
        return numPosY;
    }

    public void setNumPosY(float numPosY) {
        this.numPosY = numPosY;
    }

    /**
     * Get a jSon object filled with the list of picture passwords.
     * Converts jSon (containing list of picture passwords) to object of list of picture passwords.
     * Adds the new picture password and returns jSon of new list of picture passwords.
     *
     * @param jSonPicturePasswordList current list of picture passwords as json.
     * @param newPicturePassword      new picture password, that needs to be saved
     */
    public String getJsonPicturePasswordList(String jSonPicturePasswordList, PicturePassword newPicturePassword) {
        Gson gson = new Gson();
        List<PicturePassword> picturePasswordList;
        if (!jSonPicturePasswordList.equals("")) {
            Type picturePasswordListType = new TypeToken<ArrayList<PicturePassword>>() {
            }.getType();
            picturePasswordList = gson.fromJson(jSonPicturePasswordList, picturePasswordListType);
        } else  {
            picturePasswordList = new ArrayList<>();
        }
        picturePasswordList.add(newPicturePassword);
        return gson.toJson(picturePasswordList);
    }

    /**
     * Returns the list of all saved picture passwords.
     * This will be needed to display the list of passwords in the main activity.
     *
     * @param jsonPicturePasswordList List of picture passwords from shared preferences.
     */
    public List<PicturePassword> getPicturePasswordList(String jsonPicturePasswordList) {
        Gson gson = new Gson();
        Type picturePasswordListType = new TypeToken<ArrayList<PicturePassword>>() {
        }.getType();
        List<PicturePassword> picturePasswordList = gson.fromJson(jsonPicturePasswordList, picturePasswordListType);
        return picturePasswordList;
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
