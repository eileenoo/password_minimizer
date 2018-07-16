package ath.password_minimizer.activities;

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

import Util.Constants;
import Util.PixelConverter;
import ath.password_minimizer.R;
import model.NumberGridGenerator;
import model.PasswordManager;
import model.PicturePassword;
import model.Vector2;

public class CheckPicturePasswordActivity extends AppCompatActivity implements View.OnTouchListener {
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
        setContentView(R.layout.activity_check_picture_password);

        String picturePasswordName = (String) getIntent().getExtras().get(Constants.CHOSEN_NAME);
        currentPicturePassword = Constants.getPicturePasswordByName(Constants.getJsonPicturePWList(this), picturePasswordName);
        getSupportActionBar().setTitle("Passwort: " + currentPicturePassword.getPasswordName().toUpperCase());


        // Show grid
        numberGridGenerator = new NumberGridGenerator(this, getStatusBarHeight());
        this.scale = getResources().getDisplayMetrics().density;
        ImageView numberGridView = findViewById(R.id.numberGrid);
        numberGridView.setOnTouchListener(this);

        if (currentPicturePassword != null) {
            setupNumberMatrix();
        }
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
        ImageView passwordImageContainer = findViewById(R.id.passwordImageContainer);
        passwordImageContainer.setImageBitmap(bitmap);

        // Setup matrix
        numberGrid = numberGridGenerator.generateNumberMatrix(Integer.parseInt(currentPicturePassword.getPasswordNumber()),
                (ImageView) findViewById(R.id.numberGrid), currentPicturePassword.getPasswordStrength(), false);

        ImageView numberGridView = findViewById(R.id.numberGrid);

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
            Toast.makeText(this, "Password is correct", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Password is incorrect", Toast.LENGTH_LONG).show();

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
