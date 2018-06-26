package model;

/**
 * Created by VerenaSchlott on 05/06/18.
 */

public class PicturePassword {

    // TODO: Feel free to modifiy
    public String passwordName;
    public PasswordStrength passwordStrength;
    public String imagePath;


    public PicturePassword(String passwordName, PasswordStrength passwordStrength, String imagePath) {
        this.passwordName = passwordName;
        this.passwordStrength = passwordStrength;
        this.imagePath = imagePath;
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
}
