package ath.password_minimizer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import Util.Constants;
import Util.PixelConverter;
import ath.password_minimizer.R;
import model.NumberGridGenerator;
import model.PasswordManager;
import model.PasswordStrength;
import model.PicturePassword;
import model.Vector2;

public class RedirectionWebToAppActivity extends AppCompatActivity implements View.OnTouchListener {

    private Uri uriWebsite;
    private PicturePassword currentPicturePassword;

    private float xDelta;
    private float yDelta;
    private float scale;
    private NumberGridGenerator numberGridGenerator;

    private Vector2 startPosition = new Vector2();
    private Vector2 positionDifference = new Vector2();

    private int[] numberGrid;

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
            currentPicturePassword = getAccordingPicturePassword(correctPassWordStrength);
            //Show dialog and tell user to enter correct pw
            Constants.showNewDialogOkButton(RedirectionWebToAppActivity.this, Constants.REDIRECT_ENTER_PW_DIALOG, Constants.REDIRECT_BUTTON_OK, null);
        } else {
            noPasswordWithAccordingStrengthAction();
        }

        // Show grid
        numberGridGenerator = new NumberGridGenerator(this, getStatusBarHeight());
        this.scale = getResources().getDisplayMetrics().density;
        ImageView numberGridView = findViewById(R.id.numberGridRedirect);
        numberGridView.setOnTouchListener(this);

        setupNumberMatrix();
    }

    /**
     * Returns the index of first picture password with the according strength.
     * In case there is no picture pw with the given strength, it returns 0.
     *
     * @param passwordStrength passed by website.
     * @return first picture pw with the according pw strength.
     */
    private PicturePassword getAccordingPicturePassword(PasswordStrength passwordStrength) {
        PicturePassword currentPicturePassword = null;
        List<PicturePassword> picturePasswordList = Constants.getCurrentPicturePasswordList(this);
        for (PicturePassword picturePassword : picturePasswordList) {
            if (picturePassword.getPasswordStrength() == passwordStrength) {
                currentPicturePassword = picturePassword;
            }
        }
        return currentPicturePassword;
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
        Constants.showNewDialogOkCancelButton(RedirectionWebToAppActivity.this, Constants.REDIRECT_ERROR_DIALOG, Constants.REDIRECT_ERROR_OK_BTN, Constants.REDIRECT_ERROR_CANCEL_BTN, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                backToBrowser(false);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setupNumberMatrix();
            }
        });
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
//        if (!uriWebsite.toString().startsWith("http://") && !uriWebsite.toString().startsWith("https://")) {
//            uriWebsite = Uri.parse("http://" + uriWebsite.toString());
//        }
//        if (wasPasswordEnteredCorrect) {
//            uriWebsite = Uri.parse(uriWebsite.toString() + "correct");
//        } else {
//            uriWebsite = Uri.parse(uriWebsite.toString() + "incorrect");
//        }

        if (currentPicturePassword.getPasswordStrength() == PasswordStrength.SIMPLE) {
            uriWebsite = Uri.parse("http://www.garten-pioniere.de.w017833c.kasserver.com/");
        } else {
            uriWebsite = Uri.parse("http://trust-bank.de.w017833c.kasserver.com/");
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uriWebsite);
        startActivity(browserIntent);
    }

    private void setupNumberMatrix() {
        // Setup image
        Bitmap bitmap = null;
        try {
            InputStream inputStream = getBaseContext().getContentResolver().openInputStream(Uri.parse(currentPicturePassword.getImageUri()));
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView passwordImageContainer = findViewById(R.id.passwordImageContainerRedirect);
        passwordImageContainer.setImageBitmap(bitmap);

        // Setup matrix
        numberGrid = numberGridGenerator.generateNumberMatrix(Integer.parseInt(currentPicturePassword.getPasswordNumber()),
                (ImageView) findViewById(R.id.numberGridRedirect), false);

        ImageView numberGridView = findViewById(R.id.numberGridRedirect);

        startPosition.x = numberGridView.getX();
        startPosition.y = numberGridView.getY();
    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDelta = view.getX() - event.getRawX();
                yDelta = view.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                view.animate()
                        .x(event.getRawX() + xDelta)
                        .y(event.getRawY() + yDelta)
                        .setDuration(0)
                        .start();
                break;
            case MotionEvent.ACTION_UP:
                positionDifference.x = view.getX() - startPosition.x;
                positionDifference.y = view.getY() - startPosition.y;

                Vector2 positionDiffDp = new Vector2();
                positionDiffDp.x = PixelConverter.convertPixelsToDp(positionDifference.x, this);
                positionDiffDp.y = PixelConverter.convertPixelsToDp(positionDifference.y, this);

                checkIfPasswordIsCorrect(positionDiffDp);
                break;
            default:
                return false;
        }
        return true;
    }

    private void checkIfPasswordIsCorrect(Vector2 positionDifferenceDp) {
        String chosenNumber = currentPicturePassword.getPasswordNumber();

        Vector2 positionDp = new Vector2();
        positionDp.x = currentPicturePassword.getNumberPosition().x;
        positionDp.y = currentPicturePassword.getNumberPosition().y;

        PasswordManager passwordManager = PasswordManager.getInstance();
        PicturePassword password = passwordManager.createPicturePassword(currentPicturePassword.getPasswordStrength(), currentPicturePassword.getPasswordName(), currentPicturePassword.getImageUri(), chosenNumber, positionDp);

        boolean isCorrect = numberGridGenerator.checkIfPasswordIsCorrect(password, positionDifferenceDp, numberGrid);

        if (isCorrect) {
            pwCorrectAction();

        } else {
            pwIncorrectAction();
        }
    }


    public int getStatusBarHeight() {
        int actionBarHeight = 0;
        int statusBarHeight;
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            statusBarHeight = (int) PixelConverter.convertDpToPixel(24, this);
        } else {
            statusBarHeight = (int) PixelConverter.convertDpToPixel(25, this);
        }
        return actionBarHeight + statusBarHeight;
    }

}
