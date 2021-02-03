package com.danieljrodrigues.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    private class CalcAdapter extends RecyclerView.Adapter<CalcAdapter.RegisterHolder> {
        private List<Register> registeredItems;

        public CalcAdapter(List<Register> registeredItems) { this.registeredItems = registeredItems; }

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
            holder.bind(current);
        }

        @Override
        public int getItemCount() {
            return registeredItems.size();
        }

        private class RegisterHolder extends RecyclerView.ViewHolder {
            public RegisterHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(Register registerIndex) {
                TextView type = itemView.findViewById(R.id.rv_registerType);
                TextView res = itemView.findViewById(R.id.rv_registerValue);
                TextView date = itemView.findViewById(R.id.rv_registerDate);

                type.setText(registerIndex.getType());
                res.setText(getString(R.string.response, registerIndex.getRes()));
                date.setText(registerIndex.getCreated_at());
            }
        }
    }
}