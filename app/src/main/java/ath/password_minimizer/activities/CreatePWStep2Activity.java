package ath.password_minimizer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import Util.Constants;
import ath.password_minimizer.R;

/**
 * User chooses image.
 */
public class CreatePWStep2Activity extends AppCompatActivity {

    private final static int SELECT_PHOTO = 12345;
    private final static int READ_EXTERNAL_STORAGE_PERMISSION = 1000;

    Bitmap passwordImage;

    private boolean isNextButtonEnabled;
    private Button nextButton;
    private ImageButton pickImageButton;
    private ImageView pickedImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pwstep2);

        setViewAndDataElements();
        setOnClickListener();

        checkPermissions();
    }

    private void setViewAndDataElements() {
        isNextButtonEnabled = false;
        nextButton = findViewById(R.id.step2_btn_next);
        setNextButtonClickableStatus();
        pickImageButton = findViewById(R.id.pickImageButton);
        pickedImageView = findViewById(R.id.pickedImage);

    }

    private void setOnClickListener() {

        pickImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordImage != null) {
                    Bundle bundle = getIntent().getExtras();
                    bundle.putString(Constants.CHOSEN_IMAGE, String.valueOf(passwordImage));
                    Intent intent = new Intent(CreatePWStep2Activity.this, CreatePWStep3Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // set Error message
                }
            }
        });


    }

    private void checkPermissions()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    private void setNextButtonClickableStatus() {
        nextButton.setEnabled(isNextButtonEnabled);
        if (isNextButtonEnabled) {
            nextButton.setBackground(getResources().getDrawable(R.drawable.create_pw_button_background));
            nextButton.setTextColor(getResources().getColor(R.color.color_white));
        } else {
            nextButton.setBackground(getResources().getDrawable(R.drawable.create_pw_button_background_disabled));
            nextButton.setTextColor(getResources().getColor(R.color.color_button_pin_pressed));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null)
        {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            pickedImageView.setImageBitmap(bitmap);
            pickedImageView.setVisibility(View.VISIBLE);
            pickImageButton.setVisibility(View.GONE);

            // Do something with the bitmap
            passwordImage = bitmap;

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
            isNextButtonEnabled = true;
            setNextButtonClickableStatus();
        }
    }
}
