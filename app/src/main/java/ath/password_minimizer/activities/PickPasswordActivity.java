package ath.password_minimizer.activities;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import Util.Constants;
import ath.password_minimizer.R;
import model.PicturePassword;

public class PickPasswordActivity extends AppCompatActivity {

    private Uri pickedImageUri;
    private Bitmap passwordImage;
    private String chosenNumber;

    private ImageView pwImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_password);

        setDataAndViewElements();
        createNumberMatrix();
    }

    private void setDataAndViewElements() {
        Bundle bundle = getIntent().getExtras();
        pickedImageUri = (Uri) bundle.get(Constants.CHOSEN_IMAGE);
        chosenNumber = (String) bundle.get(Constants.CHOSEN_NUM);

        // TODO: Bild wird noch nicht angezeigt
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(pickedImageUri, filePath, null, null, null);
        cursor.moveToFirst();
        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        cursor.close();

        pwImage = findViewById(R.id.confirm_pickedImage);
        pwImage.setImageBitmap(bitmap);

        TextView description = findViewById(R.id.confirm_pw_description);
        description.setText(getResources().getString(R.string.string_confirm_pw_1) + chosenNumber + getResources().getString(R.string.string_confirm_pw_2));
    }


    private void createNumberMatrix() {
    }

}
