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

        List<MainItem> mainItems = new ArrayList<>();
        mainItems.add(new MainItem(1, R.drawable.imc_icon, R.string.imc, R.color.purple_700));

        rvMain.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter adapter = new MainAdapter(mainItems);
        rvMain.setAdapter(adapter);
    }

    private class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

        private List<MainItem> mainItems;

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

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            MainItem current = mainItems.get(position);
            holder.bind(current);
        }

        @Override
        public int getItemCount() {
            return mainItems.size();
        }
    }

    private class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bind(MainItem mainItemIndex) {
            TextView title = itemView.findViewById(R.id.rv_title);
            ImageView icon = itemView.findViewById(R.id.rv_drawable);
            LinearLayout container = (LinearLayout) itemView;

            title.setText(mainItemIndex.getTextStringId());
            icon.setImageResource(mainItemIndex.getDrawableId());
            container.setBackgroundColor(mainItemIndex.getColor());
        }
    }
}