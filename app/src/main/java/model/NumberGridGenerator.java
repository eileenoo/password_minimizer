package model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.Random;

import Util.PixelConverter;
import ath.password_minimizer.R;

public class NumberGridGenerator
{
    private Context context;

    private final int[] numberIds = new int[]{R.drawable.number1, R.drawable.number2, R.drawable.number3,
            R.drawable.number4, R.drawable.number5, R.drawable.number6, R.drawable.number7,
            R.drawable.number8, R.drawable.number9};

    private final int numbersPerRow = 17;
    private final int numbersPerColumn = 25;

    private final float errorThresholdDp = 20.0f;
    private final int bitmapScaleFactor = 4;
    private int topBarHeightPx;

    private float scale;
    private int pixelDim;


    public NumberGridGenerator(Context context, int topBarHeightPx)
    {
        this.context = context;
        this.scale = context.getResources().getDisplayMetrics().density;
        this.pixelDim = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.number1).getWidth();
        this.topBarHeightPx = topBarHeightPx;
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

    public int[] generateHighlightedNumberMatrix(int passwordNumber, ImageView numberGridImageView, ImageView backgroundImageView)
    {
        int[] numbers = getRandomNumbers(numbersPerRow * numbersPerColumn, passwordNumber);

        numbers[((numbersPerRow * numbersPerColumn) - 1) / 2] = passwordNumber;

        Bitmap numbersGrid = createNumberGridBitmap(numbersPerRow, numbersPerColumn, numbers, pixelDim, true);

        numberGridImageView.setImageBitmap(numbersGrid);

        // set scale and position of imageview
        ViewGroup.LayoutParams params2 = numberGridImageView.getLayoutParams();
        params2.width = numbersPerRow * pixelDim * bitmapScaleFactor;
        params2.height = numbersPerColumn * pixelDim * bitmapScaleFactor;

        float[] screenSize = getScreenSize(backgroundImageView);
        screenSize[1] = screenSize[1] - topBarHeightPx;

        numberGridImageView.setTranslationX(-((screenSize[0] / 2) + ((params2.width / 2) - screenSize[0])));
        numberGridImageView.setTranslationY(-((screenSize[1] / 2) + ((params2.height / 2) - screenSize[1])));


        return numbers;
    }

    public int[] generateNumberMatrix(int passwordNumber, ImageView numberGridImageView, ImageView backgroundImageView)
    {
        int pixelDim = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.number1).getWidth();

        int[] numbers = getRandomNumbers(numbersPerRow * numbersPerColumn);

        numbers[((numbersPerRow * numbersPerColumn) - 1) / 2] = passwordNumber;

        Bitmap numbersGrid = createNumberGridBitmap(numbersPerRow, numbersPerColumn, numbers, pixelDim, false);

        numberGridImageView.setImageBitmap(numbersGrid);

        ViewGroup.LayoutParams params2 = numberGridImageView.getLayoutParams();
        params2.width = (int) (numbersPerRow * pixelDim * 2.0f * scale + 0.5f);
        params2.height = (int) (numbersPerColumn * pixelDim * 2.0f * scale + 0.5f);

        numberGridImageView.setTranslationX(-((numbersPerRow * pixelDim * (scale + 0.5f)) / 2.0f));
        numberGridImageView.setTranslationY(-((numbersPerColumn * pixelDim * (scale + 0.5f)) / 2.0f));

        return numbers;
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

    private int[] getRandomNumbers(int count)
    {
        int[] numbers = new int[count];
        Random random = new Random();

        for (int i = 0; i < numbers.length; i++)
        {
            numbers[i] = random.nextInt(10 - 1) + 1;
        }

        return numbers;
    }

    private float[] getScreenSize(ImageView backgroundImageView)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float pxHeight = displayMetrics.heightPixels;
        float pxWidth = displayMetrics.widthPixels;

        return new float[]{pxWidth, pxHeight};
    }

    public boolean isAnyNumberInGridOnPosition(int passwordNumber, int[] numberGrid,
                                               Vector2 numberGridTranslationDp, Vector2 positionDp)
    {
        float leftPosOffsetDp = -((numbersPerRow * pixelDim * (scale + 0.5f)) / 2.0f);
        float topPosOffsetDp = -((numbersPerColumn * pixelDim * (scale + 0.5f)) / 2.0f);

        for (int i = 0; i < numberGrid.length; i++)
        {
            if (numberGrid[i] == passwordNumber) // determine pixel position in dp
            {
                int columnIndex = (i % numbersPerRow);
                int rowIndex = (i / numbersPerRow);

                Vector2 centeredPositionDp = new Vector2();
                centeredPositionDp.x = ((columnIndex * pixelDim) + ((pixelDim) / 2.0f));
                centeredPositionDp.y = ((rowIndex * pixelDim) + ((pixelDim) / 2.0f));

                centeredPositionDp.x += leftPosOffsetDp;
                centeredPositionDp.y += topPosOffsetDp;

                // check if translated centered position is in threshold of positionDp
                Vector2 translatedPositionDp = new Vector2();
                translatedPositionDp.x = centeredPositionDp.x + numberGridTranslationDp.x;
                translatedPositionDp.y = centeredPositionDp.y + numberGridTranslationDp.y;

                float differenceDp = getVector2Difference(translatedPositionDp, positionDp);
                Log.d("TEST", "difference " + columnIndex + ", " + rowIndex + " : " + String.valueOf(differenceDp));

                if (differenceDp < errorThresholdDp)
                {
                    return true;
                }
            }
        }

        return false;
    }

    private float getVector2Difference(Vector2 v1, Vector2 v2)
    {
        float x = Math.abs(v1.x - v2.x);
        float y = Math.abs(v2.y - v2.y);

        return x + y;
    }

}
