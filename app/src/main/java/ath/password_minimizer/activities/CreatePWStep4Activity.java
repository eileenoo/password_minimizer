package ath.password_minimizer.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import Util.Constants;
import Util.PixelConverter;
import ath.password_minimizer.R;
import model.NumberGridGenerator;
import model.PasswordStrength;
import model.Vector2;

public class CreatePWStep4Activity extends AppCompatActivity implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;
    private float scale;

    private Vector2 startPosition = new Vector2();
    private Vector2 positionDifference = new Vector2();

    private String chosenNumber;
    private Uri chosenImageUri;
    private PasswordStrength passwordStrength;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pwstep4);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        getDataFromBundle();
        setDataAndViewElementss();
        //Constants.showNewDialogOkButton(CreatePWStep4Activity.this, Constants.DIALOG_PUT_NUM_TO_AREA, Constants.REDIRECT_BUTTON_OK, null);
    }

    private void getDataFromBundle() {
        Bundle bundle = getIntent().getExtras();
        chosenImageUri = Uri.parse(bundle.getString(Constants.CHOSEN_IMAGE_URI));
        chosenNumber = bundle.getString(Constants.CHOSEN_NUM);
        passwordStrength = (PasswordStrength)bundle.get(Constants.CHOSEN_PW_STRENGTH);
    }

    private void setDataAndViewElementss() {
        ImageView passwordImageContainer = findViewById(R.id.passwordImageContainer);
        passwordImageContainer.setImageBitmap(getChosenImage());

        scale = getResources().getDisplayMetrics().density;

        ImageView numberGridView = findViewById(R.id.numberGrid);
        numberGridView.setOnTouchListener(this);

        NumberGridGenerator numberGridGenerator = new NumberGridGenerator(this, getStatusBarHeight());
        numberGridGenerator.generateNumberMatrix(Integer.parseInt(chosenNumber),
                (ImageView) findViewById(R.id.numberGrid),  passwordStrength,true);

        startPosition.x = numberGridView.getX();
        startPosition.y = numberGridView.getY();
    }

    private Bitmap getChosenImage() {
        Bitmap bitmap = null;

        try
        {
            InputStream inputStream = getBaseContext().getContentResolver().openInputStream(chosenImageUri);
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return bitmap;


//        String[] filePath = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(chosenImageUri, filePath, null, null, null);
//        cursor.moveToFirst();
//        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        options.inSampleSize = 4;
//        Bitmap unscaledBitmap = BitmapFactory.decodeFile(imagePath, options);
//        Bitmap scaledBitmap = null;
//
//        float calculatedWidthRatio = 600.0f / (float)unscaledBitmap.getHeight();
//        scaledBitmap = Bitmap.createScaledBitmap(unscaledBitmap, (int)((float)unscaledBitmap.getWidth() * calculatedWidthRatio),
//                600, false);
//        unscaledBitmap.recycle();
//        unscaledBitmap = null;
//
//        cursor.close();
//
//        return unscaledBitmap;
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

                findViewById(R.id.numberGrid).setOnTouchListener(null);
                initiateConfirmActivity(positionDiffDp);
                break;
            default:
                return false;
        }
        return true;
    }

    private float[] getAvailableScreenSizeInDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = PixelConverter.convertPixelsToDp(displayMetrics.heightPixels - getStatusBarHeight(), this);
        float dpWidth = PixelConverter.convertPixelsToDp(displayMetrics.widthPixels, this);

        return new float[]{dpWidth, dpHeight};
    }

    private void initiateConfirmActivity(Vector2 positionDifferenceDp) {
        Bundle bundle = getIntent().getExtras();

        float[] screenSizeDp = getAvailableScreenSizeInDp();

        Vector2 positionDp = new Vector2();
        positionDp.x = (screenSizeDp[0] / 2.0f) + positionDifferenceDp.x;
        positionDp.y = (screenSizeDp[1] / 2.0f) + positionDifferenceDp.y;

        bundle.putFloat(Constants.PASSWORD_NUM_POS_X, positionDp.x);
        bundle.putFloat(Constants.PASSWORD_NUM_POS_Y, positionDp.y);

        Intent intent = new Intent(CreatePWStep4Activity.this, CreatePWStep5Activity.class);
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
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
