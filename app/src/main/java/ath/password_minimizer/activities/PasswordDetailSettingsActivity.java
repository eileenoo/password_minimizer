package ath.password_minimizer.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import ath.password_minimizer.R;

public class PasswordDetailSettingsActivity extends BaseActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_pw_detail_settings:
                    return true;
                case R.id.navigation_pw_details_websites:
                    Intent intentPasswordWebsites = new Intent(PasswordDetailSettingsActivity.this, PasswordDetailWebsitesActivity.class);
                    startActivity(intentPasswordWebsites);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail_settings);
        setNavigationBar();

        initBurgerMenu();
    }

    private void setNavigationBar() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_pw_detail_settings);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
