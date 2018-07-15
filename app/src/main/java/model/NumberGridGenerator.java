package model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import Util.Constants;
import Util.PixelConverter;
import ath.password_minimizer.R;
import ath.password_minimizer.activities.MainActivity;

public class NumberGridGenerator
{
    private Context context;

    private final int[] numberIds = new int[]{R.drawable.number1, R.drawable.number2, R.drawable.number3,
            R.drawable.number4, R.drawable.number5, R.drawable.number6, R.drawable.number7,
            R.drawable.number8, R.drawable.number9};

    private final int numbersPerRow = 17;
    private final int numbersPerColumn = 25;

    private final float errorThresholdDp = 20.0f;
    private final int bitmapScaleFactor = 2;
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

    public int[] generateNumberMatrix(int passwordNumber, ImageView numberGridImageView, PasswordStrength strength, boolean forSelection)
    {
        int pixelDim = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.number1).getWidth();

        int[] numbers;

        if (forSelection)
        {
            numbers = getRandomNumbers(numbersPerRow * numbersPerColumn, passwordNumber);
            numbers[((numbersPerRow * numbersPerColumn) - 1) / 2] = passwordNumber;
        }
        else
        {
            numbers = getEvenlyDistributedRandomNumbers(numbersPerRow * numbersPerColumn);
        }

        Bitmap numbersGrid = createNumberGridBitmap(numbersPerRow, numbersPerColumn, numbers, pixelDim, forSelection);

        numberGridImageView.setImageBitmap(numbersGrid);

        // set scale and position of imageview
        ViewGroup.LayoutParams params2 = numberGridImageView.getLayoutParams();
        params2.width = numbersPerRow * pixelDim * bitmapScaleFactor;
        params2.height = numbersPerColumn * pixelDim * bitmapScaleFactor;

        float[] screenSize = getScreenSize();
        screenSize[1] = screenSize[1] - topBarHeightPx;

        numberGridImageView.setTranslationX(-((screenSize[0] / 2) + ((params2.width / 2) - screenSize[0])));
        numberGridImageView.setTranslationY(-((screenSize[1] / 2) + ((params2.height / 2) - screenSize[1])));

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

    private int[] getEvenlyDistributedRandomNumbers(int quantity, int differentNumberCount, int passwordNumber)
    {
        int[] numbers = new int[quantity];

        int rest = quantity % differentNumberCount;
        int countNoRest = quantity - rest;
        int chunkCount = countNoRest / differentNumberCount;

        List<Integer> numberChunkSelection = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        numberChunkSelection.remove(passwordNumber - 1);
        int[] numberChunk = new int[differentNumberCount];

        // TODO: finish
        //for ()


        numberChunk[0] = passwordNumber;


        for (int i = 0; i < chunkCount; i++)
        {

            shuffleArray(numberChunk);

            for (int j = i * differentNumberCount; j < (i * differentNumberCount) + differentNumberCount; j++)
            {
                numbers[j] = numberChunk[j - (i * differentNumberCount)];
            }
        }

        int[] randomRestNumbers = getRandomNumbers(rest);

        for (int i = quantity - rest; i < quantity; i++)
        {
            numbers[i] = randomRestNumbers[i - (quantity - rest)];
        }

        return numbers;
    }

    private float[] getScreenSize()
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float pxHeight = displayMetrics.heightPixels;
        float pxWidth = displayMetrics.widthPixels;

        return new float[]{pxWidth, pxHeight};
    }

    public boolean isAnyNumberInGridOnPosition(int passwordNumber, int[] numberGrid,
                                               Vector2 numberGridTranslationDp, Vector2 positionDp)
    {
        float[] screenSize = getScreenSize();
        screenSize[1] = screenSize[1] - topBarHeightPx;

        float leftPosOffsetDp = -((screenSize[0] / 2) + (((numbersPerRow * pixelDim * bitmapScaleFactor) / 2) - screenSize[0]));
        float topPosOffsetDp = -((screenSize[1] / 2) + (((numbersPerColumn * pixelDim * bitmapScaleFactor) / 2) - screenSize[1]));

        leftPosOffsetDp = PixelConverter.convertPixelsToDp(leftPosOffsetDp, context);
        topPosOffsetDp = PixelConverter.convertPixelsToDp(topPosOffsetDp, context);

        for (int i = 0; i < numberGrid.length; i++)
        {
            if (numberGrid[i] == passwordNumber) // determine pixel position in dp
            {
                int columnIndex = (i % numbersPerRow);
                int rowIndex = (i / numbersPerRow);

                Vector2 centeredPositionDp = new Vector2();
                centeredPositionDp.x = (columnIndex * pixelDim * bitmapScaleFactor) + ((pixelDim * bitmapScaleFactor) / 2.0f);
                centeredPositionDp.y = (rowIndex * pixelDim * bitmapScaleFactor) + ((pixelDim * bitmapScaleFactor) / 2.0f);

                centeredPositionDp.x = PixelConverter.convertPixelsToDp(centeredPositionDp.x, context);
                centeredPositionDp.y = PixelConverter.convertPixelsToDp(centeredPositionDp.y, context);

                centeredPositionDp.x += leftPosOffsetDp;
                centeredPositionDp.y += topPosOffsetDp;

                // check if translated centered position is in threshold of positionDp
                Vector2 translatedPositionDp = new Vector2();
                translatedPositionDp.x = centeredPositionDp.x + numberGridTranslationDp.x;
                translatedPositionDp.y = centeredPositionDp.y + numberGridTranslationDp.y;

                float differenceDp = getVector2Difference(translatedPositionDp, positionDp);
                //Log.d("TEST", "difference " + columnIndex + ", " + rowIndex + " : " + String.valueOf(differenceDp));

                if (differenceDp < errorThresholdDp)
                {
                    Log.d("CORRECT", columnIndex + ", " + rowIndex);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean checkIfPasswordIsCorrect(PicturePassword picturePassword, Vector2 positionDifferenceDp, int[] numberGrid)
    {
        boolean isCorrect = isAnyNumberInGridOnPosition(Integer.parseInt(picturePassword.getPasswordNumber()),
                numberGrid, positionDifferenceDp, picturePassword.getNumberPosition());

        return isCorrect;
    }

    private float getVector2Difference(Vector2 v1, Vector2 v2)
    {
        float x = Math.abs(v1.x - v2.x);
        float y = Math.abs(v1.y - v2.y);

        return x + y;
    }

    // Implementing Fisher–Yates shuffle
    private void shuffleArray(int[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
}
