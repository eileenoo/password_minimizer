package ath.password_minimizer.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Util.Constants;
import ath.password_minimizer.R;
import listAdapters.PasswordListAdapter;
import model.PasswordStrength;
import model.PicturePassword;

public class MainActivity extends BaseActivity {

    boolean userHasPasswords;
    ArrayList<PicturePassword> picturePasswords;
    ListView picturePasswordListView;
    private static PasswordListAdapter passwordListAdapter;
    ImageButton addPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userHasPasswords = checkIfUserHasPasswords();

        initBurgerMenu();

        picturePasswordListView = (ListView) findViewById(R.id.password_list);

        // TODO: save data somewhere else and remove dummy data here
        picturePasswords = new ArrayList<>();
        picturePasswords.add(new PicturePassword(PasswordStrength.SIMPLE, "Einfaches Passwort", "xxx/yy/abc.png", "3", 3, 4));
        picturePasswords.add(new PicturePassword(PasswordStrength.SIMPLE, "Einfaches Passwort", "xxx/yy/abc.png", "3", 3, 4));

        passwordListAdapter = new PasswordListAdapter(picturePasswords, getApplicationContext());

        picturePasswordListView.setAdapter(passwordListAdapter);

        picturePasswordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPasswordDetailWebsitesActivity();
            }
        });

        addPasswordButton = (ImageButton) findViewById(R.id.addPasswordButton);
        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePasswordActivity();
            }
        });

        if (picturePasswords.size() >= 3) {
            addPasswordButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Checks if the user has already created a picture password.
     *
     * @return true or false
     */
    private boolean checkIfUserHasPasswords() {
        SharedPreferences sharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFERENCES_PASSWORD_MINIMIZER, MODE_PRIVATE);
        String jSonListOfPWFromSharedPreferences = sharedPreferences.getString(Constants.LIST_PICTURE_PASSWORDS, "");
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
     * Redirects to activity, where the user can create a new picture password.
     */
    private void openPasswordDetailWebsitesActivity() {
        Intent intent = new Intent(this, PasswordDetailWebsitesActivity.class);
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
