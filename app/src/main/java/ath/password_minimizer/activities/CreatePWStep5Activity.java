package ath.password_minimizer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import Util.Constants;
import Util.PixelConverter;
import ath.password_minimizer.R;
import model.NumberGridGenerator;
import model.PasswordStrength;
import model.PicturePassword;
import model.Vector2;

public class CreatePWStep5Activity extends AppCompatActivity implements View.OnTouchListener {
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
        setContentView(R.layout.activity_create_pwstep5);
        numberGridGenerator = new NumberGridGenerator(this, getStatusBarHeight());

        this.scale = getResources().getDisplayMetrics().density;

        ImageView numberGridView = findViewById(R.id.numberGrid);
        numberGridView.setOnTouchListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        Uri imageUri = Uri.parse(bundle.getString(Constants.CHOSEN_IMAGE_URI));
        String chosenNumber = (String) bundle.get(Constants.CHOSEN_NUM);

        Bitmap passwordImage = getPasswordImage(imageUri);
        setDataAndViewElements(passwordImage);
        numberGrid = numberGridGenerator.generateNumberMatrix(Integer.parseInt(chosenNumber),
                (ImageView) findViewById(R.id.numberGrid), false);

        ImageView numberGridView = findViewById(R.id.numberGrid);

        startPosition.x = numberGridView.getX();
        startPosition.y = numberGridView.getY();
    }

    private void setDataAndViewElements(Bitmap passwordImage) {
        ImageView passwordImageContainer;

        passwordImageContainer = findViewById(R.id.passwordImageContainer);
        passwordImageContainer.setImageBitmap(passwordImage);
    }

    private Bitmap getPasswordImage(Uri imageUri) {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);

        // At the end remember to close the cursor or you will end with the RuntimeException!
        cursor.close();

        return bitmap;
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

                System.out.println("Action Up");
                checkIfPasswordIsCorrect(positionDiffDp);

                findViewById(R.id.numberGrid).setOnTouchListener(null);
                break;
            default:
                return false;
        }
        return true;
    }

    private void checkIfPasswordIsCorrect(Vector2 positionDifferenceDp) {
        Bundle bundle = getIntent().getExtras();
        String chosenNumber = (String) bundle.get(Constants.CHOSEN_NUM);

        Vector2 positionDp = new Vector2();
        positionDp.x = (float) bundle.get(Constants.PASSWORD_NUM_POS_X);
        positionDp.y = (float) bundle.get(Constants.PASSWORD_NUM_POS_Y);

        boolean isCorrect = numberGridGenerator.isAnyNumberInGridOnPosition(Integer.parseInt(chosenNumber),
                numberGrid, positionDifferenceDp, positionDp);

        System.out.println("is Correct?: "+ isCorrect);
        Log.d("Is Correct: ", String.valueOf(isCorrect));

        if (isCorrect) {
            savePasswordToHardwareDevice();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
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

    public void savePasswordToHardwareDevice() {
        Bundle bundle = getIntent().getExtras();
        PasswordStrength pwStrength = (PasswordStrength) bundle.get(Constants.CHOSEN_PW_STRENGTH);
        String pwName = bundle.getString(Constants.CHOSEN_NAME);
        String imageUri = bundle.getString(Constants.CHOSEN_IMAGE_URI);
        String pwNum = bundle.getString(Constants.CHOSEN_NUM);
        float pwPosX = bundle.getFloat(Constants.PASSWORD_NUM_POS_X);
        float pwPosY = bundle.getFloat(Constants.PASSWORD_NUM_POS_Y);

        PicturePassword newPicturePassword = new PicturePassword(pwStrength, pwName, imageUri, pwNum, pwPosX, pwPosY);

        String currentJsonPicturePasswordList = Constants.getPicturePWListFromSharedPreferences(this);
        String newPicturePasswordList = newPicturePassword.getJsonPicturePasswordList(currentJsonPicturePasswordList, newPicturePassword);
        Constants.savePicturePWListToSharedPreferences(newPicturePasswordList, this);
    }
}
