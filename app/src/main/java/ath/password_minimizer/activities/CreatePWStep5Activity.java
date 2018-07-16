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
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import Util.Constants;
import Util.PixelConverter;
import ath.password_minimizer.R;
import model.NumberGridGenerator;
import model.PasswordManager;
import model.PasswordStrength;
import model.PicturePassword;
import model.Vector2;

public class CreatePWStep5Activity extends AppCompatActivity implements View.OnTouchListener
{
    private float xDelta;
    private float yDelta;
    private float scale;
    private NumberGridGenerator numberGridGenerator;

    private Vector2 startPosition = new Vector2();
    private Vector2 positionDifference = new Vector2();

    private int[] numberGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pwstep5);
        numberGridGenerator = new NumberGridGenerator(this, getStatusBarHeight());

        this.scale = getResources().getDisplayMetrics().density;

        ImageView numberGridView = findViewById(R.id.numberGrid);
        numberGridView.setOnTouchListener(this);
        setupNumberMatrix();
        //Constants.showNewDialogOkButton(CreatePWStep5Activity.this, Constants.DIALOG_PUT_NUM_TO_AREA_REPEAT, Constants.REDIRECT_BUTTON_OK, null);
    }

    private void setDataAndViewElements(Bitmap passwordImage)
    {
        ImageView passwordImageContainer;

        passwordImageContainer = findViewById(R.id.passwordImageContainer);
        passwordImageContainer.setImageBitmap(passwordImage);
    }

    private Bitmap getPasswordImage(Uri imageUri)
    {
//        String[] filePath = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(imageUri, filePath, null, null, null);
//        cursor.moveToFirst();
//        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        options.inSampleSize = 4;
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
//
//        cursor.close();

        Bitmap bitmap = null;

        try
        {
            InputStream inputStream = getBaseContext().getContentResolver().openInputStream(imageUri);
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

    }

    public boolean onTouch(View view, MotionEvent event)
    {
        switch (event.getAction())
        {
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

    private void setupNumberMatrix()
    {
        Bundle bundle = getIntent().getExtras();
        Uri imageUri = Uri.parse(bundle.getString(Constants.CHOSEN_IMAGE_URI));
        String chosenNumber = (String) bundle.get(Constants.CHOSEN_NUM);
        PasswordStrength strength = (PasswordStrength) bundle.get(Constants.CHOSEN_PW_STRENGTH);

        Bitmap passwordImage = getPasswordImage(imageUri);
        setDataAndViewElements(passwordImage);
        numberGrid = numberGridGenerator.generateNumberMatrix(Integer.parseInt(chosenNumber),
                (ImageView) findViewById(R.id.numberGrid), strength, false);

        ImageView numberGridView = findViewById(R.id.numberGrid);

        startPosition.x = numberGridView.getX();
        startPosition.y = numberGridView.getY();
    }

    private void checkIfPasswordIsCorrect(Vector2 positionDifferenceDp)
    {
        Bundle bundle = getIntent().getExtras();
        String chosenNumber = (String) bundle.get(Constants.CHOSEN_NUM);

        Vector2 positionDp = new Vector2();
        positionDp.x = (float) bundle.get(Constants.PASSWORD_NUM_POS_X);
        positionDp.y = (float) bundle.get(Constants.PASSWORD_NUM_POS_Y);

        PasswordManager passwordManager = PasswordManager.getInstance();
        PicturePassword password = passwordManager.createPicturePassword(
                (PasswordStrength) bundle.get(Constants.CHOSEN_PW_STRENGTH),
                bundle.getString(Constants.CHOSEN_NAME), bundle.getString(Constants.CHOSEN_IMAGE_URI),
                chosenNumber, positionDp);

        boolean isCorrect = numberGridGenerator.checkIfPasswordIsCorrect(password, positionDifferenceDp, numberGrid);

        if (isCorrect)
        {
            findViewById(R.id.numberGrid).setOnTouchListener(null);
            passwordManager.addNewPicturePassword(password);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(getApplicationContext(), R.string.string_wrong_password, Toast.LENGTH_SHORT).show();
            setupNumberMatrix();
        }
    }

    public int getStatusBarHeight()
    {
        int actionBarHeight = 0;
        int statusBarHeight;

        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            statusBarHeight = (int) PixelConverter.convertDpToPixel(24, this);
        }
        else
        {
            statusBarHeight = (int) PixelConverter.convertDpToPixel(25, this);
        }

        return actionBarHeight + statusBarHeight;
    }
}
