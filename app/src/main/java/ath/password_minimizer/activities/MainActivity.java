package ath.password_minimizer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ath.password_minimizer.R;

public class MainActivity extends AppCompatActivity {

    boolean userHasPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userHasPasswords = checkIfUserHasPasswords();
    }

    /**
     * Checks if the user has already created a picture password.
     *
     * @return true or false
     */
    private boolean checkIfUserHasPasswords() {
        return false;
    }

    /**
     * Redirects to activity, where the user can create a new picture password.
     */
    private void openCreatePasswordActivity() {
        Intent intent = new Intent(this, CreatePasswordActivity.class);
        startActivity(intent);
    }
}