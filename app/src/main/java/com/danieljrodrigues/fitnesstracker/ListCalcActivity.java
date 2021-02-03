package com.danieljrodrigues.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListCalcActivity extends AppCompatActivity {

    private RecyclerView rvRegisters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calc);

        rvRegisters = findViewById(R.id.rv_registers);
        rvRegisters.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String type = extras.getString("type");
            List<Register> registers = SqlHelper.getInstance(this).getItemsBy(type);

            CalcAdapter adapter = new CalcAdapter(registers);
            rvRegisters.setAdapter(adapter);
        }
    }

    private class CalcAdapter extends RecyclerView.Adapter<CalcAdapter.RegisterHolder> implements OnAdapterClickListener {
        private List<Register> registeredItems;

        public CalcAdapter(List<Register> registeredItems) {
            this.registeredItems = registeredItems;
        }

        @NonNull
        @Override
        public RegisterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RegisterHolder(
                LayoutInflater.from(ListCalcActivity.this)
                    .inflate(R.layout.register_item, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RegisterHolder holder, int position) {
            Register current = registeredItems.get(position);
            holder.bind(current, this);
        }

        @Override
        public int getItemCount() {
            return registeredItems.size();
        }

        @Override
        public void onLongPress(int position, String type, int id) {
            Log.i("TESTE", String.valueOf(id));
            AlertDialog alertDialog = new AlertDialog.Builder(ListCalcActivity.this)
                .setMessage(getString(R.string.delete_message))
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                    new Thread(() -> {
                        SqlHelper sqlHelper = SqlHelper.getInstance(ListCalcActivity.this);
                        long calcId = sqlHelper.deleteItem(type, id);

                        runOnUiThread(() -> {
                            if (calcId > 0) {
                                Toast.makeText(ListCalcActivity.this, R.string.calc_removed, Toast.LENGTH_LONG).show();
                                registeredItems.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }).start();
                }).create();
            alertDialog.show();
        }

        private class RegisterHolder extends RecyclerView.ViewHolder {
            public RegisterHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(Register registerIndex, final OnAdapterClickListener onClickListener) {
                TextView type = itemView.findViewById(R.id.rv_registerType);
                TextView res = itemView.findViewById(R.id.rv_registerValue);
                TextView date = itemView.findViewById(R.id.rv_registerDate);

                type.setText(registerIndex.type);
                res.setText(getString(R.string.response, registerIndex.res));
                date.setText(registerIndex.created_at);

                itemView.setOnLongClickListener(v -> {
                    Log.i("TESTE", String.valueOf(registerIndex.type));
                    onClickListener.onLongPress(getAdapterPosition(), registerIndex.type, registerIndex.getId());
                    return false;
                });

            }
        }
    }
}