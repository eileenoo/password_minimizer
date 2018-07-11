package ath.password_minimizer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import Util.Constants;
import ath.password_minimizer.R;
import listAdapters.PasswordListAdapter;
import model.PicturePassword;

public class MainActivity extends BaseActivity {

    ArrayList<PicturePassword> picturePasswords;
    ListView picturePasswordListView;
    private static PasswordListAdapter passwordListAdapter;
    ImageButton addPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(userHasPasswords()) {
            picturePasswords = (ArrayList<PicturePassword>) Constants.getPicturePasswordList(Constants.getJsonPicturePWList(this));
        } else {
            picturePasswords = new ArrayList<>();
        }

        initBurgerMenu();

        picturePasswordListView = findViewById(R.id.password_list);

        passwordListAdapter = new PasswordListAdapter(picturePasswords, getApplicationContext());

        picturePasswordListView.setAdapter(passwordListAdapter);

        picturePasswordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openPasswordDetailWebsitesActivity();
            }
        });

        addPasswordButton = findViewById(R.id.addPasswordButton);
        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePasswordActivity();
            }
        });

//        if (picturePasswords.size() >= 3) {
//            addPasswordButton.setVisibility(View.INVISIBLE);
//        }
    }

    /**
     * Checks if the user has already created a picture password.
     *
     * @return true or false
     */
    private boolean userHasPasswords() {
        return !Constants.getJsonPicturePWList(this).equals("");
    }

    /**
     * Redirects to activity, where the user can create a new picture password.
     */
    private void openCreatePasswordActivity() {
//        Intent intent = new Intent(this, CreatePasswordActivity.class);
        Intent intent = new Intent(this, RedirectionWebToAppActivity.class);
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
