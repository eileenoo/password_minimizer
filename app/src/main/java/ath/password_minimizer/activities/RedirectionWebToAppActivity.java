package ath.password_minimizer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import Util.Constants;
import ath.password_minimizer.R;
import model.PasswordStrength;
import model.PicturePassword;

public class RedirectionWebToAppActivity extends AppCompatActivity {

    private Uri uriWebsite;
    private PicturePassword currentPicturePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirection_web_to_app);

        uriWebsite = getIntent().getData();
        String currentPWStrength = uriWebsite.toString().substring(uriWebsite.toString().lastIndexOf("=") + 1);

        // Get current pw strength
        PasswordStrength correctPassWordStrength;
        switch (currentPWStrength) {
            case Constants.SIMPLE:
                correctPassWordStrength = PasswordStrength.SIMPLE;
                break;
            case Constants.MIDDLE:
                correctPassWordStrength = PasswordStrength.MIDDLE;
                break;
            case Constants.STRONG:
                correctPassWordStrength = PasswordStrength.STRONG;
                break;
            default:
                correctPassWordStrength = null;
                break;
        }
        if (correctPassWordStrength != null) {
            displayCorrectPicturePassword(getAccordingPicturePassword(correctPassWordStrength));

            //Show dialog and tell user to enter correct pw
            Constants.showNewDialogOkButton(RedirectionWebToAppActivity.this, Constants.REDIRECT_ENTER_PW_DIALOG, Constants.REDIRECT_BUTTON_OK, null);
        } else {
            noPasswordWithAccordingStrengthAction();
        }
    }

    /**
     * Returns the index of first picture password with the according strength.
     * In case there is no picture pw with the given strength, it returns 0.
     *
     * @param passwordStrength passed by website.
     * @return first picture pw with the according pw strength.
     */
    private int getAccordingPicturePassword(PasswordStrength passwordStrength) {
        int currentPicturePasswordIndex = 0;
        List<PicturePassword> picturePasswordList = Constants.getCurrentPicturePasswordList(this);
        for (int i = 0; i <= picturePasswordList.size(); i++) {
            if (picturePasswordList.get(i).getPasswordStrength() == passwordStrength) {
                currentPicturePasswordIndex = i;
                System.out.println(picturePasswordList.get(i).getPasswordName());
                break;
            }
        }
        return currentPicturePasswordIndex;
    }

    /**
     * Displays a picture password, which the user needs to unlock.
     */
    private void displayCorrectPicturePassword(int currentPWIndex) {
        Intent intent = new Intent(RedirectionWebToAppActivity.this, CheckPicturePasswordActivity.class);
        intent.putExtra(Constants.PICTURE_PASSWORD_INDEX, currentPWIndex);
        startActivity(intent);
        //TODO: show correct pw
//        pwCorrectAction();
    }


    private void pwCorrectAction() {
        backToBrowser(true);
    }

    /**
     * Shows dialog with to options: 1) back to browser or 2) try again.
     * 1) User gets redirected to browser with path /incorrect.
     * 2) User can enter pw again.
     */
    private void pwIncorrectAction() {
        //TODO: okListener: try again.
        Constants.showNewDialogOkCancelButton(RedirectionWebToAppActivity.this, Constants.REDIRECT_ERROR_DIALOG, Constants.REDIRECT_ERROR_OK_BTN, Constants.REDIRECT_ERROR_CANCEL_BTN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backToBrowser(false);
            }
        }, null);
    }

    /**
     * If user has no password with the given strength he can go back to the website or create new pw.
     */
    private void noPasswordWithAccordingStrengthAction() {
        Constants.showNewDialogOkCancelButton(RedirectionWebToAppActivity.this, Constants.REDIRECT_NO_PW_DIALOG, Constants.REDIRECT_CREATE_PW, Constants.REDIRECT_ERROR_CANCEL_BTN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backToBrowser(false);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(RedirectionWebToAppActivity.this, CreatePasswordActivity.class));
            }
        });
    }

    /**
     * Redirects to website.
     * If the entered pw was correct the username and password should be shown in website.
     * If the entered pw was incorrect and the user did not want to try it again, the user gets redirected to
     *
     * @param wasPasswordEnteredCorrect true if pw was correct / false if pw was incorrect
     */
    private void backToBrowser(boolean wasPasswordEnteredCorrect) {
        if (!uriWebsite.toString().startsWith("http://") && !uriWebsite.toString().startsWith("https://")) {
            uriWebsite = Uri.parse("http://" + uriWebsite.toString());
        }
        if (wasPasswordEnteredCorrect) {
            uriWebsite = Uri.parse(uriWebsite.toString() + "correct");
        } else {
            uriWebsite = Uri.parse(uriWebsite.toString() + "incorrect");
        }
        System.out.println("Uri-webseite: " + uriWebsite);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uriWebsite);
        startActivity(browserIntent);
    }

}
