package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;
import java.util.Random;

import ath.password_minimizer.R;

public class PasswordManager
{
    private Context context;
    private List<PicturePassword> picturePasswords;

    private final int[] numberIds = new int[]{R.drawable.number1, R.drawable.number2, R.drawable.number3,
            R.drawable.number4, R.drawable.number5, R.drawable.number6, R.drawable.number7,
            R.drawable.number8, R.drawable.number9};

    private final int numbersPerRow = 17;
    private final int numbersPerColumn = 25;

    private float scale;

    public PasswordManager(Context context)
    {
        this.context = context;
        this.scale = context.getResources().getDisplayMetrics().density;
    }

    public void AddPicturePassword()
    {

    }

    private Bitmap createNumberGridBitmap(int numbersPerRow, int numbersPerColumn, int[] numbers, int numberImageDimensions, boolean forSelection)
    {
        Bitmap result = Bitmap.createBitmap(numberImageDimensions * numbersPerRow,
                numberImageDimensions * numbersPerColumn, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        Paint paintTransparent = new Paint();
        paintTransparent.setAlpha(120);

        for (int i = 0; i < numbers.length; i++)
        {
            Bitmap numberImg = BitmapFactory.decodeResource(context.getResources(),
                    numberIds[numbers[i] - 1]);

            if (forSelection)
            {
                // if not center / selection number
                if (i != ((numbers.length - 1) / 2))
                {
                    canvas.drawBitmap(numberImg,
                            numberImageDimensions * (i % numbersPerRow),
                            numberImageDimensions * (i / numbersPerRow), paintTransparent);
                }
                else
                {
                    canvas.drawBitmap(numberImg,
                            numberImageDimensions * (i % numbersPerRow),
                            numberImageDimensions * (i / numbersPerRow), paint);
                }
            }
            else
            {
                canvas.drawBitmap(numberImg,
                        numberImageDimensions * (i % numbersPerRow),
                        numberImageDimensions * (i / numbersPerRow), paint);
            }
        }

        return result;
    }

    public void generateSelectionNumberMatrix(int selectedNumber, ImageView numberGridImageView)
    {
        int pixelDim = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.number1).getWidth();

        int[] numbers = getRandomNumbers(numbersPerRow * numbersPerColumn, selectedNumber);

        numbers[((numbersPerRow * numbersPerColumn) - 1) / 2] = selectedNumber;

        Bitmap numbersGrid = createNumberGridBitmap(numbersPerRow, numbersPerColumn, numbers, pixelDim, true);

        numberGridImageView.setImageBitmap(numbersGrid);

        ViewGroup.LayoutParams params2 = numberGridImageView.getLayoutParams();
        params2.width = (int) (numbersPerRow * pixelDim * 2.0f * scale + 0.5f);
        params2.height = (int) (numbersPerColumn * pixelDim * 2.0f * scale + 0.5f);

        numberGridImageView.setTranslationX(-((numbersPerRow * pixelDim * (scale + 0.5f)) / 2.0f));
        numberGridImageView.setTranslationY(-((numbersPerColumn * pixelDim * (scale + 0.5f)) / 2.0f));
    }

    private int[] getRandomNumbers(int count, int exception)
    {
        int[] numbers = new int[count];
        Random random = new Random();

        for (int i = 0; i < numbers.length; i++)
        {
            int number = exception;

            while (number == exception)
            {
                number = random.nextInt(10 - 1) + 1;
            }

            numbers[i] = number;
        }

        return numbers;
    }

    public Bitmap makeTransparent(Bitmap src, int value)
    {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap transBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transBitmap);
        canvas.drawARGB(0, 0, 0, 0);
        // config paint
        final Paint paint = new Paint();
        paint.setAlpha(value);
        canvas.drawBitmap(src, 0, 0, paint);
        return transBitmap;
    }

    private float[] getScreenSizeInDp()
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return new float[]{dpWidth, dpHeight};
    }
}
