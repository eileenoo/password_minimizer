package ath.password_minimizer.activities;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Util.Constants;
import ath.password_minimizer.R;

public class CreatePWStep4Activity extends AppCompatActivity implements View.OnTouchListener
{
    private TextView centerNumberView;
    private TextView backgroundNumberMatrixView;
    private ViewGroup root;
    private float xDelta;
    private float yDelta;
    private float scale;

    private final int[] numberIds = new int[] {R.drawable.number1, R.drawable.number2, R.drawable.number3,
            R.drawable.number4, R.drawable.number5, R.drawable.number6, R.drawable.number7,
            R.drawable.number8, R.drawable.number9};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pwstep4);

        root = (ViewGroup) findViewById(R.id.root);
//        centerNumberView = findViewById(R.id.numberMatrix);
//        backgroundNumberMatrixView = findViewById(R.id.numberMatrixBackground);

        this.scale = getResources().getDisplayMetrics().density;

        findViewById(R.id.numberGrid).setOnTouchListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Bundle bundle = getIntent().getExtras();
        Uri imageUri = (Uri) bundle.get(Constants.CHOSEN_IMAGE);
        String chosenNumber = (String) bundle.get(Constants.CHOSEN_NUM);

        Bitmap passwordImage = getPasswordImage(imageUri);
        setDataAndViewElements(passwordImage, chosenNumber);
        createNumberMatrix();
    }

    private void setDataAndViewElements(Bitmap passwordImage, String chosenNumber)
    {
        ImageView passwordImageContainer;

        passwordImageContainer = findViewById(R.id.passwordImageContainer);
        passwordImageContainer.setImageBitmap(passwordImage);

    }

    private Bitmap getPasswordImage(Uri imageUri)
    {
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


    private void createNumberMatrix()
    {
        int numbersPerRow = 17;
        int numbersPerColumn = 25;

        int pixelDim = BitmapFactory.decodeResource(getResources(),
                R.drawable.number1).getWidth();

        int[] numbers = new int[]
                {   1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 1, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1};

        Bitmap numbersGrid = createNumberGridBitmap(numbersPerRow, numbersPerColumn, numbers, pixelDim);

        ImageView numberGridView = findViewById(R.id.numberGrid);

        numberGridView.setImageBitmap(numbersGrid);

        float[] screenSize = getScreenSizeInDp();

        ViewGroup.LayoutParams params2 = numberGridView.getLayoutParams();
        params2.width = (int) (numbersPerRow * pixelDim * 2.0f * scale + 0.5f);
        params2.height = (int) (numbersPerColumn * pixelDim * 2.0f * scale + 0.5f);

        numberGridView.setTranslationX(-((numbersPerRow * pixelDim * (scale + 0.5f)) / 2.0f));
        numberGridView.setTranslationY(-((numbersPerColumn * pixelDim * (scale + 0.5f)) / 2.0f));
    }

    private Bitmap createNumberGridBitmap(int numbersPerRow, int numbersPerColumn, int[] numbers, int numberImageDimensions)
    {
        Bitmap result = Bitmap.createBitmap(numberImageDimensions * numbersPerRow,
                numberImageDimensions * numbersPerColumn, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < numbers.length; i++)
        {
            Bitmap numberImg = BitmapFactory.decodeResource(getResources(),
                    numberIds[numbers[i] - 1]);

            canvas.drawBitmap(numberImg,
                    numberImageDimensions * (i % numbersPerRow),
                    numberImageDimensions * (i / numbersPerRow), paint);
        }

        return result;
    }

    private float[] getScreenSizeInDp()
    {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return new float[]{dpWidth, dpHeight};
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

//                backgroundNumberMatrixView.animate()
//                        .x(event.getRawX() + xDelta)
//                        .y(event.getRawY() + yDelta)
//                        .setDuration(0)
//                        .start();

                break;
            default:
                return false;
        }
        return true;
    }

}
