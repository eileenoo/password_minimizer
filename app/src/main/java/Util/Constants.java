package Util;

import android.content.Context;
import android.content.SharedPreferences;

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

    public final static String SHARED_PREFERENCES_PASSWORD_MINIMIZER = "sharedPreferencesPWMinimizer";
    public final static String LIST_PICTURE_PASSWORDS = "listOfPicturePasswords";

    public final static void savePicturePWListToSharedPreferences(String newPicturePasswordList, Context context) {
        // TODO: existiert liste mit shared preferences und diesem namen/key schon?
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_PASSWORD_MINIMIZER, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.LIST_PICTURE_PASSWORDS, newPicturePasswordList);
        editor.apply();
    }

    /**
     * Return the saved jsonObject containing a list of picture passwords from shared preferences
     *
     * @return "" (if there is no json object saved) or the saved json object
     */
    public final static  String getPicturePWListFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES_PASSWORD_MINIMIZER, MODE_PRIVATE);
        return sharedPreferences.getString(Constants.LIST_PICTURE_PASSWORDS, "");
    }
}
