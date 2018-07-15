package ath.password_minimizer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import Util.Constants;
import ath.password_minimizer.R;
import model.PasswordStrength;
import model.PicturePassword;

public class RedirectionWebToAppActivity extends AppCompatActivity {

    private Uri uriWebsite;
    private PicturePassword currentPicturePassword;

    private Uri exmplUri = Uri.parse("http://garten-pioniere.de.w017833c.kasserver.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirection_web_to_app);

        findViewById(R.id.btn_example).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToBrowser(true);
            }
        });

//        List<String> path = getIntent().getData().getPathSegments();
//        uriWebsite = getIntent().getData();
//        Uri uri = Uri.parse(path.get(2));
//        PasswordStrength correctPassWordStrength;
//
//        switch (uri.toString()) {
//            case Constants.SIMPLE:
//                correctPassWordStrength = PasswordStrength.SIMPLE;
//                break;
//            case Constants.MIDDLE:
//                correctPassWordStrength = PasswordStrength.MIDDLE;
//                break;
//            case Constants.STRONG:
//                correctPassWordStrength = PasswordStrength.STRONG;
//                break;
//            default:
//                correctPassWordStrength = PasswordStrength.SIMPLE;
//                break;
//        }
//        //Show dialog and tell user to enter correct pw
//        Constants.showNewDialogOkButton(RedirectionWebToAppActivity.this, Constants.REDIRECT_ENTER_PW_DIALOG, Constants.REDIRECT_BUTTON_OK, null);
//
//        getAccordingPicturePassword(correctPassWordStrength);
//        displayCorrectPicturePassword();
    }

    /**
     * Returns the first picture password with the according strength.
     *
     * @param passwordStrength passed by website.
     * @return first picture pw with the according pw strength.
     */
    private void getAccordingPicturePassword(PasswordStrength passwordStrength) {
        PicturePassword picturePasswordToShow = null;
        for (PicturePassword picturePassword : Constants.getCurrentPicturePasswordList(this)) {
            if (picturePassword.getPasswordStrength() == passwordStrength) {
                picturePasswordToShow = picturePassword;
            }
        }
    }

    /**
     * Displays a picture password, which the user needs to unlock.
     */
    private void displayCorrectPicturePassword() {
        //TODO: show correct pw
        pwCorrectAction();
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
     * Redirects to website.
     * If the entered pw was correct the username and password should be shown in website.
     * If the entered pw was incorrect and the user did not want to try it again, the user gets redirected to
     *
     * @param wasPasswordEnteredCorrect true if pw was correct / false if pw was incorrect
     */
    private void backToBrowser(boolean wasPasswordEnteredCorrect) {
        uriWebsite = exmplUri;
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
