package model;

import android.app.Application;
import android.content.Context;

import java.util.List;

import Util.Constants;
import application.PicturePasswordApplication;


public class PasswordManager
{
    private static PasswordManager instance;

    private PasswordManager()
    {
    }

    public static PasswordManager getInstance()
    {
        if (instance == null)
        {
            instance = new PasswordManager();
        }

        return instance;
    }

    public void addNewPicturePassword(PicturePassword picturePassword)
    {
        String currentJsonPicturePasswordList = Constants.getJsonPicturePWList(PicturePasswordApplication.getAppContext());
        String newPicturePasswordList = Constants.getJsonPicturePWListWithNewlyAddedPW(currentJsonPicturePasswordList, picturePassword);
        Constants.savePicturePWListToSharedPreferences(newPicturePasswordList, PicturePasswordApplication.getAppContext());
    }

    public PicturePassword createPicturePassword(PasswordStrength strength, String name, String imageUri, String number, Vector2 position)
    {
        return new PicturePassword(strength, name, imageUri, number, position);
    }

    public void updateWebsitesOfPicturePassword(PicturePassword updatedPicturePassword)
    {
        String currentJsonPicturePasswordList = Constants.getJsonPicturePWList(PicturePasswordApplication.getAppContext());
        String updatedPicturePasswordList  = Constants.getJsonPicturePWListWithUpdatedPW(currentJsonPicturePasswordList, updatedPicturePassword);
        Constants.savePicturePWListToSharedPreferences(updatedPicturePasswordList, PicturePasswordApplication.getAppContext());
    }
}
