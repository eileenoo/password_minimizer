package ath.password_minimizer.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import ath.password_minimizer.R;

public class PasswordDetailSettingsActivity extends BaseActivity {

    Button changeImageButton;
    Button changeNumberButton;
    Button changeSecurityButtton;
    Button deletePasswordButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_pw_detail_settings:
                    return true;
                case R.id.navigation_pw_details_websites:
                    String picturePasswordName = (String) getIntent().getExtras().get("picturePasswordName");
                    Intent intentPasswordWebsites = new Intent(PasswordDetailSettingsActivity.this, PasswordDetailWebsitesActivity.class);
                    intentPasswordWebsites.putExtra("picturePasswordName", picturePasswordName);
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

        changeImageButton = (Button) findViewById(R.id.changeImageButton);
        changeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: change image for password here
            }
        });

        changeNumberButton = (Button) findViewById(R.id.changeNumberButton);
        changeNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: change number for password here
            }
        });

        changeSecurityButtton = (Button) findViewById(R.id.changeSecurityButton);
        changeSecurityButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: change security for password here
            }
        });

        deletePasswordButton = (Button) findViewById(R.id.deletePasswordButton);
        deletePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: deleted password here
            }
        });
    }

    private void setNavigationBar() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_pw_detail_settings);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
