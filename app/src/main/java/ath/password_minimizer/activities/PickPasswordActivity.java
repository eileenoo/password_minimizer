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
import android.widget.ImageView;

import ath.password_minimizer.R;
import model.PasswordStrength;

public class PickPasswordActivity extends AppCompatActivity
{
    private final static int SELECT_PHOTO = 12345;
    private final static int READ_EXTERNAL_STORAGE_PERMISSION = 1000;

    ImageView pickedImageView;
    Bitmap passwordImage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_password);

        pickedImageView = findViewById(R.id.pickedImage);
        checkPermissions();

        findViewById(R.id.pickImageButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle extras = getIntent().getExtras();
                PasswordStrength passwordStrength = PasswordStrength.SIMPLE;

                if (extras.containsKey("PasswordStrength"))
                {
                    passwordStrength = (PasswordStrength)extras.get("PasswordStrength");
                }

                // TODO:
                Intent intent = new Intent(PickPasswordActivity.this, CreatePWStep3Activity.class);
                intent.putExtra("PasswordStrength", passwordStrength);

                startActivity(intent);
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

            // Do something with the bitmap
            passwordImage = bitmap;

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }
}
