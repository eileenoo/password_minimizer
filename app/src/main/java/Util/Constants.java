package Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    public final static String PIN_RESET_DIALOG = "Alle Passwörter wurden gelöscht.";

    public final static String ERROR_EMPTY_PIN_NAME = "Bitte gib einen Namen für das Passwort ein.";
    public final static String TOAST_ERROR_CHOOSE_IMAGE = "Bitte wähle ein Bild für dein Passwort aus.";

    public final static String SIMPLE = "password simple";
    public final static String MIDDLE = "password middle";
    public final static String STRONG = "password strong";

    public final static String CHOSEN_PW_STRENGTH = "passwordStrength";
    public final static String CHOSEN_NUM = "chosenNum";
    public final static String CHOSEN_IMAGE_URI = "chosenImageUri";
    public final static String CHOSEN_NAME = "chosenName";
    public final static String PASSWORD_NUM_POS_X = "passwordNumPosX";
    public final static String PASSWORD_NUM_POS_Y = "passwordNumPosY";

    public final static String REDIRECT_ENTER_PW_DIALOG = "Please enter the correct password";
    public final static String REDIRECT_BUTTON_OK = "Ok";

    public final static String REDIRECT_ERROR_DIALOG = "The entered password is incorrect.";
    public final static String REDIRECT_ERROR_OK_BTN = "Try again";
    public final static String REDIRECT_ERROR_CANCEL_BTN = "Back to website";

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

    /**
     * Get current list of picture passwords.
     *
     * @param context of the calling activity.
     * @return current List of picture passwords.
     */
    public static List<PicturePassword> getCurrentPicturePasswordList(Context context) {
        return getPicturePasswordList(getJsonPicturePWList(context));
    }


    /**
     * Shows specific dialog with specific ok and cancel actions.
     *
     * @param context            of the calling activity.
     * @param dialog             message.
     * @param okButtonString     text of ok button.
     * @param cancelButtonString text of cancel button.
     * @param cancelListener     cancel action.
     * @param okListener         confirm action.
     */
    public static void showNewDialogOkCancelButton(Context context, String dialog, String okButtonString, String cancelButtonString, DialogInterface.OnClickListener cancelListener, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(dialog)
                .setPositiveButton(okButtonString, okListener)
                .setNegativeButton(cancelButtonString, cancelListener)
                .create()
                .show();
    }

    /**
     * Shows specific dialog with specific ok and cancel actions.
     *
     * @param context            of the calling activity.
     * @param dialog             message.
     * @param okButtonString     text of ok button.
     * @param okListener         confirm action.
     */
    public static void showNewDialogOkButton(Context context, String dialog, String okButtonString, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setMessage(dialog)
                .setPositiveButton(okButtonString, okListener)
                .create()
                .show();
    }

}
