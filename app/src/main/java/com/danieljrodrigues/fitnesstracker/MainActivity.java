package com.danieljrodrigues.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private View cardBtn;
    private RecyclerView rvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        cardBtn = findViewById(R.id.Card);
//
//        cardBtn.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, ImcActivity.class);
//            startActivity(intent);
//        });
        rvMain = findViewById(R.id.rv_main);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter();
        rvMain.setAdapter(adapter);
    }

    private class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false)
            );
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return 5;
        }
    }

    private class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(int index) {
            TextView title = itemView.findViewById(R.id.rv_title);
            title.setText("IMC" + index);
        }
    }
}