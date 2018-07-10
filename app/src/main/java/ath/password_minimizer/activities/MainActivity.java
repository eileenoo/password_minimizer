package ath.password_minimizer.activities;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import ath.password_minimizer.R;
import model.PicturePassword;

public class MainActivity extends AppCompatActivity {

    private List<PicturePassword> picturePasswordsList;
    private boolean userHasPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picturePasswordsList = new ArrayList<>();
        userHasPasswords = checkIfUserHasPasswords();
        Button btn = findViewById(R.id.add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreatePasswordActivity();
            }
        });
    }

    /**
     * Checks if the user has already created a picture password.
     *
     * @return true or false
     */
    private boolean checkIfUserHasPasswords() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES_PASSWORD_MINIMIZER, MODE_PRIVATE);
        String jSonListOfPWFromSharedPreferences = sharedPreferences.getString(Constants.LIST_PICTURE_PASSWORDS,"");
        System.out.println(jSonListOfPWFromSharedPreferences);
        return false;
    }

    /**
     * Redirects to activity, where the user can create a new picture password.
     */
    private void openCreatePasswordActivity() {
        Intent intent = new Intent(this, CreatePasswordActivity.class);
        startActivity(intent);
    }

    /**
     * Does not close the app if this is the last activity.
     */
    @Override
    public void onBackPressed() {
        if (!this.isTaskRoot()) {
            super.onBackPressed();
        }
    }
}
