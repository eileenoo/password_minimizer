package ath.password_minimizer.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import Util.Constants;
import ath.password_minimizer.R;

public class StartPinActivity extends AppCompatActivity {

    // UI elements
    private TextView pinFieldOne, pinFieldTwo, pinFieldThree, pinFieldFour;
    private TextView[] pinFields;
    private Button btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9, btnNum0;
    private ImageButton btnDelete, btnHelp;
    private ArrayList<Button> buttonsNumPad;

    // Data elements
    private String correctPin, restartPin;
    private int counter;
    private boolean isPinInputDisabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_pin);

        setViewElements();
        setDataElements();
        setButtonsClickListener();
    }

    private void setViewElements() {
        pinFieldOne = findViewById(R.id.pin_entry_first);
        pinFieldTwo = findViewById(R.id.pin_entry_second);
        pinFieldThree = findViewById(R.id.pin_entry_third);
        pinFieldFour = findViewById(R.id.pin_entry_fourth);
        pinFields = new TextView[]{pinFieldOne, pinFieldTwo, pinFieldThree, pinFieldFour};
        // Set focus on first pin field
        requestOutlineFocus(pinFieldOne);

        btnNum0 = findViewById(R.id.button_0);
        btnNum1 = findViewById(R.id.button_1);
        btnNum2 = findViewById(R.id.button_2);
        btnNum3 = findViewById(R.id.button_3);
        btnNum4 = findViewById(R.id.button_4);
        btnNum5 = findViewById(R.id.button_5);
        btnNum6 = findViewById(R.id.button_6);
        btnNum7 = findViewById(R.id.button_7);
        btnNum8 = findViewById(R.id.button_8);
        btnNum9 = findViewById(R.id.button_9);
        btnNum0 = findViewById(R.id.button_0);
        btnDelete = findViewById(R.id.button_delete);
        btnHelp = findViewById(R.id.button_help);
        buttonsNumPad = new ArrayList<>();
        Collections.addAll(buttonsNumPad, btnNum0, btnNum1, btnNum2, btnNum3, btnNum4, btnNum5, btnNum6, btnNum7, btnNum8, btnNum9, btnNum0);
    }

    private void requestOutlineFocus(TextView pinField) {
        for (TextView pinEntryField: pinFields) {
            if (pinEntryField.equals(pinField)) {
                pinEntryField.setBackground(getResources().getDrawable(R.drawable.pin_entry_background_focused));
            } else {
                pinEntryField.setBackground(getResources().getDrawable(R.drawable.pin_entry_background));
            }
        }
    }

    private void setDataElements() {
        counter = 0;
        correctPin = Constants.PIN;
        restartPin = Constants.RESTART_PIN;
    }

    private void setButtonsClickListener() {
        for (Button button : buttonsNumPad) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isPinInputDisabled) {
                        enterNextPin(view.getTag().toString());
                    }
                }
            });
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPinInputDisabled) {
                    deleteLastEnteredPin();
                }
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPinInputDisabled) {
                    showNewDialog(Constants.PIN_HELP_DIALOG, null);
                }
            }
        });
    }

    private void enterNextPin(String enteredPinNum) {
        switch (counter) {
            case 0:
                pinFields[counter].setText(enteredPinNum);
                requestOutlineFocus(pinFields[counter + 1]);
                counter++;
                break;
            case 1:
                pinFields[counter].setText(enteredPinNum);
                requestOutlineFocus(pinFields[counter + 1]);
                counter++;
                break;
            case 2:
                pinFields[counter].setText(enteredPinNum);
                requestOutlineFocus(pinFields[counter + 1]);
                counter++;
                break;
            case 3:
                pinFields[counter].setText(enteredPinNum);
                if (pinFieldOne.getText().toString().length() > 0 && pinFieldTwo.getText().toString().length() > 0 && pinFieldThree.getText().toString().length() > 0 && pinFieldFour.getText().toString().length() > 0) {
                    onPinEntered();
                }
                isPinInputDisabled = true;
                break;
            default:
                break;
        }
    }

    /**
     * Deletes last entered pin and resets focus.
     */
    private void deleteLastEnteredPin() {
        if (counter <= 3 && counter > 0) {
            pinFields[counter - 1].setText("");
            requestOutlineFocus(pinFields[counter - 1]);
            counter--;
        } else if (counter == 0) {
            pinFields[counter].setText("");
            requestOutlineFocus(pinFields[counter]);
            counter = 0;
        }
    }

    /**
     * Compares the pin entered by the user with the correct pin.
     */
    private void onPinEntered() {
        String enteredPin = pinFieldOne.getText().toString() + pinFieldTwo.getText().toString() + pinFieldThree.getText().toString() + pinFieldFour.getText().toString();
        if (enteredPin.equals(correctPin)) {
            pinCorrectAction();
        } else if (enteredPin.equals(restartPin)) {
            restartApplication();
        } else {
            pinIncorrectAction();
        }
    }

    /**
     * This happens when the entered pin is correct.
     */
    private void pinCorrectAction() {
        resetPinFieldsAndEnableButtons();
        Intent intentMainActivity = new Intent(StartPinActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
//        finish();
    }

    /**
     * This happens when the entered pin is incorrect.
     */
    private void pinIncorrectAction() {
        System.out.println("pin incorrect action");
        showNewDialog(Constants.PIN_INCORRECT, new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resetPinFieldsAndEnableButtons();
                requestOutlineFocus(pinFields[0]);
                System.out.println("dismiss");
            }
        });
    }

    /**
     * Resets whole application.
     * Deletes created passwords ...
     */
    private void restartApplication() {
        resetPinFieldsAndEnableButtons();
        requestOutlineFocus(pinFields[0]);
    }

    /**
     * Resets all pin fields.
     */
    private void resetPinFieldsAndEnableButtons() {
        System.out.println("reset pinfields");
        counter = 0;
        for (TextView pinField : pinFields) {
            pinField.setText("");
        }
        isPinInputDisabled = false;
    }

    private void showNewDialog(String dialog, DialogInterface.OnDismissListener dismissAction) {
        System.out.println("___________________________");
        new AlertDialog.Builder(StartPinActivity.this)
                .setMessage(dialog)
                .setOnDismissListener(dismissAction)
                .setPositiveButton(Constants.PIN_HELP_DIALOG_BUTTON, null)
                .create()
                .show();
        isPinInputDisabled = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        resetPinFieldsAndEnableButtons();
        requestOutlineFocus(pinFields[0]);
    }

    @Override
    public void onBackPressed() {

    }
}
