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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMain = findViewById(R.id.rv_main);

        List<MainItem> mainItems = new ArrayList<>();
        mainItems.add(new MainItem(1, R.drawable.imc_icon, R.string.imc, R.color.purple_700));
        mainItems.add(new MainItem(2, R.drawable.tmb_ic, R.string.tmb, R.color.purple_700));

        rvMain.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter(mainItems);
        adapter.setListener(id -> {
            switch (id) {
                case 1:
                    startActivity(new Intent(MainActivity.this, ImcActivity.class));
                    return;
                case 2:
                    startActivity(new Intent(MainActivity.this, TmbActivity.class));
                    return;
            }
        });
        rvMain.setAdapter(adapter);
    }

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

        private List<MainItem> mainItems;
        private onItemClickListener listener;

        public MainAdapter(List<MainItem> mainItems) {
            this.mainItems = mainItems;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.main_item, parent, false)
            );
        }

        public void setListener(onItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            MainItem current = mainItems.get(position);
            holder.bind(current);
        }

        @Override
        public int getItemCount() {
            return mainItems.size();
        }

        private class MainViewHolder extends RecyclerView.ViewHolder {
            public MainViewHolder(@NonNull View itemView) {
                super(itemView);
            }

            public void bind(MainItem mainItemIndex) {
                TextView title = itemView.findViewById(R.id.rv_title);
                ImageView icon = itemView.findViewById(R.id.rv_drawable);
                LinearLayout container = (LinearLayout) itemView;

                container.setOnClickListener(v -> {
                    listener.onClick(mainItemIndex.getId());
                });

                title.setText(mainItemIndex.getTextStringId());
                icon.setImageResource(mainItemIndex.getDrawableId());
                container.setBackgroundColor(mainItemIndex.getColor());
            }
        }
    }
}