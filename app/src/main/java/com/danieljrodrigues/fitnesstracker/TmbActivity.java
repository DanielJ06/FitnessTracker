package com.danieljrodrigues.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static androidx.core.os.LocaleListCompat.create;

public class TmbActivity extends AppCompatActivity {

    private EditText editWeight;
    private EditText editHeight;
    private EditText editAge;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tmb);

        editHeight = findViewById(R.id.edit_tmb_height);
        editWeight = findViewById(R.id.edit_tmb_weight);
        editAge = findViewById(R.id.edit_tmb_age);
        spinner = findViewById(R.id.edit_tmb_lifeStyle);

        Button submitBtn = findViewById(R.id.submitTmbBtn);

        submitBtn.setOnClickListener(v -> {
            if (!validateInputs()) {
                Toast.makeText(TmbActivity.this, R.string.fields_message, Toast.LENGTH_LONG).show();
                return;
            }

            int height = Integer.parseInt(editHeight.getText().toString());
            int weight = Integer.parseInt(editWeight.getText().toString());
            int age = Integer.parseInt(editAge.getText().toString());
            double result = calculateTmb(height, weight, age);
            double tmb = tbmResponse(result);

            AlertDialog alertDialog = new AlertDialog.Builder(TmbActivity.this)
                .setTitle(getString(R.string.tmb_response, tmb))
                .setNegativeButton(android.R.string.ok, (diag, which) -> {
                })
                .setPositiveButton(R.string.save, (diag, which) -> {
                    SqlHelper sqlHelper = SqlHelper.getInstance(TmbActivity.this);
                    new Thread(() -> {
                        long calcId = sqlHelper.addItem("TMB", result);

                        runOnUiThread(() -> {
                            if (calcId > 0) {
                                Toast.makeText(TmbActivity.this, R.string.calc_saved, Toast.LENGTH_LONG).show();
                                startNewActivity();
                            }
                        });
                    }).start();
            }).create();
            alertDialog.show();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
        });
    }

    private double tbmResponse(double tmb) {
        switch (spinner.getSelectedItemPosition()) {
            case 0:
                return tmb * 1.2;
            case 1:
                return tmb * 1.375;
            case 2:
                return tmb * 1.55;
            case 3:
                return tmb * 1.725;
            case 4:
                return tmb * 1.9;
            default:
                return 0;
        }
    }

    public double calculateTmb(int height, int weight, int age) {
        return 66 + (13.8 * weight) + (5 * height) - (6.8 * age);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_listResults:
                startNewActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startNewActivity() {
        Intent intent = new Intent(TmbActivity.this, ListCalcActivity.class);
        intent.putExtra("type", "TMB");
        startActivity(intent);
    }

    private boolean validateInputs() {
        return (!editHeight.getText().toString().startsWith("0") &&
            !editHeight.getText().toString().isEmpty() &&
            !editWeight.getText().toString().startsWith("0") &&
            !editWeight.getText().toString().isEmpty() &&
            !editAge.getText().toString().startsWith("0") &&
            !editAge.getText().toString().isEmpty()
        );
    }
}