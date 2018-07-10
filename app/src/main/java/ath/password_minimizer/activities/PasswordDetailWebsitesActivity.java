package ath.password_minimizer.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import ath.password_minimizer.R;
import listAdapters.PasswordListAdapter;
import listAdapters.WebsiteListAdapter;
import model.PicturePassword;
import model.WebsiteCredentials;

public class PasswordDetailWebsitesActivity extends BaseActivity {

    ArrayList<WebsiteCredentials> websites;
    ListView websitesListView;
    private static WebsiteListAdapter websiteListAdapter;
    ImageButton addWebsiteButton;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_pw_details_websites:
                    return true;
                case R.id.navigation_pw_detail_settings:
                    Intent intentPasswordSettings = new Intent(PasswordDetailWebsitesActivity.this, PasswordDetailSettingsActivity.class);
                    startActivity(intentPasswordSettings);
                    finish();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail_websites);

        initBurgerMenu();
        setNavigationBar();

        websitesListView = (ListView) findViewById(R.id.website_list);

        websites = new ArrayList<>();
        websites.add(new WebsiteCredentials("Facebook", "Sam Smith", "password123!"));
        websites.add(new WebsiteCredentials("Twitter", "Sam Smith", "password123!"));
        websites.add(new WebsiteCredentials("Amazon", "Sam Smith", "password123!"));
        websites.add(new WebsiteCredentials("Deutsche Bahn", "Sam Smith", "password123!"));

        websiteListAdapter = new WebsiteListAdapter (websites, getApplicationContext());
        websitesListView.setAdapter(websiteListAdapter);

        websitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: go to edit password
            }
        });

        addWebsiteButton = (ImageButton) findViewById(R.id.addWebsiteButton);
        addWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: go to create password
            }
        });
    }

    private void setNavigationBar() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_pw_details_websites);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
