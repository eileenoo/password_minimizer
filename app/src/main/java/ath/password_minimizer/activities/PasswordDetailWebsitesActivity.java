package ath.password_minimizer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import Util.Constants;
import ath.password_minimizer.R;
import listAdapters.PasswordListAdapter;
import listAdapters.WebsiteListAdapter;
import model.PasswordManager;
import model.PicturePassword;
import model.WebsiteCredentials;

public class PasswordDetailWebsitesActivity extends BaseActivity {

    ArrayList<WebsiteCredentials> websites;
    ListView websitesListView;
    private static WebsiteListAdapter websiteListAdapter;
    ImageButton addWebsiteButton;

    EditText nameBox;
    EditText websiteBox;
    EditText userNameBox;
    EditText passwordBox;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_pw_details_websites:
                    return true;
                case R.id.navigation_pw_detail_settings:
                    String picturePasswordName = (String) getIntent().getExtras().get("picturePasswordName");
                    Intent intentPasswordSettings = new Intent(PasswordDetailWebsitesActivity.this, PasswordDetailSettingsActivity.class);
                    intentPasswordSettings.putExtra("picturePasswordName", picturePasswordName);
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

        //TODO: passwords need to be able to be removed
        //TODO: function to delete password

        String picturePasswordName = (String) getIntent().getExtras().get("picturePasswordName");
        final PicturePassword picturePassword = Constants.getPicturePasswordByName(Constants.getJsonPicturePWList(this), picturePasswordName);
        websites = picturePassword.getWebsites();

        websiteListAdapter = new WebsiteListAdapter (websites, getApplicationContext());
        websitesListView.setAdapter(websiteListAdapter);

        websitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: go to edit password
            }
        });

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.add_website_dialog, (ViewGroup) findViewById(R.id.layout_root));
        nameBox = (EditText) layout.findViewById(R.id.name);
        websiteBox = (EditText) layout.findViewById(R.id.website);
        userNameBox = (EditText) layout.findViewById(R.id.username);
        passwordBox = (EditText) layout.findViewById(R.id.password);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameBox.getText().toString();
                String website = websiteBox.getText().toString();
                String userName = userNameBox.getText().toString();
                String password = passwordBox.getText().toString();
                WebsiteCredentials addedWebsite = new WebsiteCredentials(name, website, userName, password);

                websites.add(addedWebsite);
                PasswordManager passwordManager = PasswordManager.getInstance();
                passwordManager.updateWebsitesOfPicturePassword(picturePassword);

                nameBox.setText("");
                nameBox.requestFocus();
                websiteBox.setText("");
                userNameBox.setText("");
                passwordBox.setText("");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();

        addWebsiteButton = (ImageButton) findViewById(R.id.addWebsiteButton);
        addWebsiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void setNavigationBar() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_pw_details_websites);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
