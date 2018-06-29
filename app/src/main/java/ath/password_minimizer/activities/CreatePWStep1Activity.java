package ath.password_minimizer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Util.Constants;
import ath.password_minimizer.R;

/**
 * User chooses pw name.
 */
public class CreatePWStep1Activity extends AppCompatActivity {

    private boolean isNextButtonEnabled;
    private int maxLengthOfPwName;
    private String pwName;
    private Button nextButton;
    private EditText pwNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pwstep1);

        setViewAndDataElements();
        setOnClickListener();
    }

    private void setViewAndDataElements() {
        pwName = "";
        isNextButtonEnabled = false;

        nextButton = findViewById(R.id.step1_btn_next);
        setNextButtonClickableStatus();

        pwNameField = findViewById(R.id.pw_name_field);
        maxLengthOfPwName = 30;
        pwNameField.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(maxLengthOfPwName)
        });
        pwNameField.requestFocus();
    }

    private void setOnClickListener() {
        pwNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pwName = pwNameField.getText().toString();
                if (!pwNameField.getText().toString().equals("")) {
                    isNextButtonEnabled = true;
                } else {
                    isNextButtonEnabled = false;
                }
                setNextButtonClickableStatus();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!pwName.equals("")) {
                    Bundle bundle = getIntent().getExtras();
                    bundle.putString(Constants.CHOSEN_NAME, pwName);
                    Intent intent = new Intent(CreatePWStep1Activity.this, CreatePWStep2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    // set Error message
                }
            }
        });
    }

    private void setNextButtonClickableStatus() {
        nextButton.setEnabled(isNextButtonEnabled);
        if (isNextButtonEnabled) {
            nextButton.setBackground(getResources().getDrawable(R.drawable.create_pw_button_background));
            nextButton.setTextColor(getResources().getColor(R.color.color_white));
        } else {
            nextButton.setBackground(getResources().getDrawable(R.drawable.create_pw_button_background_disabled));
            nextButton.setTextColor(getResources().getColor(R.color.color_button_pin_pressed));
        }
    }
}
