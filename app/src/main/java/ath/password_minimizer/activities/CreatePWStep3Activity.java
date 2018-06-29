package ath.password_minimizer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

import Util.Constants;
import ath.password_minimizer.R;

public class CreatePWStep3Activity extends AppCompatActivity {

    private String chosenNumber;
    private boolean isNextButtonEnabled;
    private Button btnNum0, btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9, nextButton;
    private ArrayList<Button> numButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pwstep3);

        setViewAndDataElements();
        setOnClickListener();
    }

    @Override
    protected void onResume() {
        chosenNumber = null;
        isNextButtonEnabled = false;
        setNextButtonClickableStatus();
        for (Button numbers : numButtons) {
            numbers.setSelected(false);
        }
        super.onResume();
    }

    private void setViewAndDataElements() {
        isNextButtonEnabled = false;
        nextButton = findViewById(R.id.step3_btn_next);
        setNextButtonClickableStatus();
        btnNum1 = findViewById(R.id.num_1);
        btnNum2 = findViewById(R.id.num_2);
        btnNum3 = findViewById(R.id.num_3);
        btnNum4 = findViewById(R.id.num_4);
        btnNum5 = findViewById(R.id.num_5);
        btnNum6 = findViewById(R.id.num_6);
        btnNum7 = findViewById(R.id.num_7);
        btnNum8 = findViewById(R.id.num_8);
        btnNum9 = findViewById(R.id.num_9);
        btnNum0 = findViewById(R.id.num_0);
        numButtons = new ArrayList<>();
        Collections.addAll(numButtons, btnNum0, btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9);
    }

    private void setOnClickListener() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chosenNumber == null) {
                    // Set error message
                } else {
                    Bundle bundle = getIntent().getExtras();
                    bundle.putString(Constants.CHOSEN_NUM, chosenNumber);
                    Intent intent = new Intent(CreatePWStep3Activity.this, null);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        for (final Button numButton : numButtons) {
            numButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (numButton.isSelected()) {
                        numButton.setSelected(false);
                        chosenNumber = null;
                        isNextButtonEnabled = false;
                        setNextButtonClickableStatus();
                    } else {
                        for (Button button: numButtons) {
                            button.setSelected(false);
                        }
                        chosenNumber = view.getTag().toString();
                        numButton.setSelected(true);
                        isNextButtonEnabled = true;
                        setNextButtonClickableStatus();
                    }
                }
            });
        }
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
