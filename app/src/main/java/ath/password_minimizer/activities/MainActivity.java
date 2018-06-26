package ath.password_minimizer.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ath.password_minimizer.R;
import listAdapters.DrawerListAdapter;
import model.NavItem;

public class MainActivity extends BaseActivity {

    boolean userHasPasswords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userHasPasswords = checkIfUserHasPasswords();

        initBurgerMenu();

        ImageButton addPasswordButton = (ImageButton) findViewById(R.id.addPasswordButton);
        addPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
