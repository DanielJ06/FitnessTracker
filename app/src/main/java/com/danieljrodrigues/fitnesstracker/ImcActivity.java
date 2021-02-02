package com.danieljrodrigues.fitnesstracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ImcActivity extends AppCompatActivity {

    private EditText editWeight;
    private EditText editHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc);

        editHeight = findViewById(R.id.edit_height);
        editWeight = findViewById(R.id.edit_weight);

        Button submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(v -> {
            if (!validateInputs()) {
                Toast.makeText(ImcActivity.this, R.string.fields_message, Toast.LENGTH_LONG).show();
                return;
            }

            String sHeight = editHeight.getText().toString();
            String sWeight = editWeight.getText().toString();

            int height = Integer.parseInt(sHeight);
            int weight = Integer.parseInt(sWeight);
            double result = calculateImc(height, weight);

            int response = imcResponse(result);

            AlertDialog dialog = new AlertDialog.Builder(ImcActivity.this)
                .setTitle(getString(R.string.imc_response, result))
                .setMessage(response)
                .setPositiveButton(android.R.string.ok, (dialog1, which) -> {
                    Intent intent = new Intent(ImcActivity.this, ListCalcActivity.class);
                    intent.putExtra("type", "IMC");
                    startActivity(intent);
                })
                .setNegativeButton(R.string.save, ((dialog1, which) -> {
                    new Thread(() -> {
                        long calcId = SqlHelper.getInstance(ImcActivity.this).addItem("IMC", result);
                        runOnUiThread(() -> {
                            if (calcId > 0) {
                                Toast.makeText(ImcActivity.this, R.string.calc_saved, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ImcActivity.this, ListCalcActivity.class);
                                intent.putExtra("type", "IMC");
                                startActivity(intent);
                            }
                        });
                    }).start();
                })).create();
            dialog.show();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editHeight.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(editWeight.getWindowToken(), 0);
        });
    }

    private int imcResponse(double imc) {
        if (imc < 15) {
            return R.string.imc_severely_low_weight;
        } else if (imc < 16) {
            return R.string.imc_very_low_weight;
        } else if (imc < 18.5) {
            return R.string.imc_low_weight;
        } else if (imc < 25) {
            return R.string.normal;
        } else if (imc < 30) {
            return R.string.imc_high_weight;
        } else if (imc < 35) {
            return R.string.imc_so_high_weight;
        } else if (imc < 40) {
            return R.string.imc_severely_high_weight;
        } else {
            return R.string.imc_extreme_weight;
        }
    }

    private double calculateImc(int height, int weight) {
        return weight / (((double) height / 100) * ((double) height / 100));
    }

    private boolean validateInputs() {
        return (!editHeight.getText().toString().startsWith("0") &&
            !editHeight.getText().toString().isEmpty() &&
            !editWeight.getText().toString().startsWith("0") &&
            !editWeight.getText().toString().isEmpty()
        );
    }
}