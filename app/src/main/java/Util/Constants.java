package Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import model.PicturePassword;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VerenaSchlott on 05/06/18.
 */

public class Constants {

    public final static String PIN = "1234";
    public final static String RESTART_PIN = "1337";
    public final static String PIN_INCORRECT = "Der eingegebene Pin ist nicht korrekt.";
    public final static String PIN_HELP_DIALOG = "Wenn du den Pin vergessen hast, kann ich dir nicht helfen";
    public final static String PIN_HELP_DIALOG_BUTTON = "Alles klar.";


    public final static String SIMPLE = "password simple";
    public final static String MIDDLE = "password middle";
    public final static String STRONG = "password strong";

    public final static String CHOSEN_PW_STRENGTH = "passwordStrength";
    public final static String CHOSEN_NUM = "chosenNum";
    public final static String CHOSEN_IMAGE_URI = "chosenImageUri";
    public final static String CHOSEN_NAME = "chosenName";
    public final static String PASSWORD_NUM_POS_X = "passwordNumPosX";
    public final static String PASSWORD_NUM_POS_Y = "passwordNumPosY";

    private final static String SHARED_PREFERENCES_PASSWORD_MINIMIZER = "sharedPreferencesPWMinimizer";
    private final static String LIST_PICTURE_PASSWORDS = "listOfPicturePasswords";


    /**
     * Return the saved jsonObject containing a list of picture passwords from shared preferences
     *
     * @return "" (if there is no json object saved) or the saved json object
     */
    public static String getJsonPicturePWList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_PASSWORD_MINIMIZER, MODE_PRIVATE);
        return sharedPreferences.getString(Constants.LIST_PICTURE_PASSWORDS, "");
    }

    /**
     * Get a jSon object filled with the list of picture passwords.
     * Converts jSon (containing list of picture passwords) to object of list of picture passwords.
     * Adds the new picture password and returns jSon of new list of picture passwords.
     *
     * @param jSonPicturePasswordList current list of picture passwords as json.
     * @param newPicturePassword      new picture password, that needs to be saved
     */
    public static String getJsonPicturePWListWithNewlyAddedPW(String jSonPicturePasswordList, PicturePassword newPicturePassword) {
        Gson gson = new Gson();
        List<PicturePassword> picturePasswordList;
        if (!jSonPicturePasswordList.equals("")) {
            Type picturePasswordListType = new TypeToken<ArrayList<PicturePassword>>() {
            }.getType();
            picturePasswordList = gson.fromJson(jSonPicturePasswordList, picturePasswordListType);
        } else {
            picturePasswordList = new ArrayList<>();
        }
        picturePasswordList.add(newPicturePassword);
        return gson.toJson(picturePasswordList);
    }

    /**
     * Save new json with list of picture passwords to shared preferences.
     *
     * @param newPicturePasswordList json with new list of picture passwords.
     * @param context                context of calling activity.
     */
    public static void savePicturePWListToSharedPreferences(String newPicturePasswordList, Context context) {
        // TODO: existiert liste mit shared preferences und diesem namen/key schon?
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_PASSWORD_MINIMIZER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LIST_PICTURE_PASSWORDS, newPicturePasswordList);
        editor.apply();
    }

    /**
     * Returns the list of all saved picture passwords.
     * This will be needed to display the list of passwords in the main activity.
     *
     * @param jsonPicturePasswordList List of picture passwords from shared preferences.
     */
    public static List<PicturePassword> getPicturePasswordList(String jsonPicturePasswordList) {
        Gson gson = new Gson();
        Type picturePasswordListType = new TypeToken<ArrayList<PicturePassword>>() {
        }.getType();
        return gson.fromJson(jsonPicturePasswordList, picturePasswordListType);
    }

    /**
     * Removes list of saved picture passwords.
     *
     * @param context of the calling activity.
     */
    public static void removeAllPicturePasswords(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_PASSWORD_MINIMIZER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LIST_PICTURE_PASSWORDS, "");
        editor.apply();
    }

}
