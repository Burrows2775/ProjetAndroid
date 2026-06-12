package com.example.projetandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (view, windowInsets) -> {
            int topInsetTypes = WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout();
            int topInset = windowInsets.getInsets(topInsetTypes).top;

            view.setPadding(
                    view.getPaddingLeft(),
                    topInset,
                    view.getPaddingRight(),
                    view.getPaddingBottom()
            );

            return windowInsets;
        });

        Button btnBack = findViewById(R.id.btn_back);
        ListView lvScores = findViewById(R.id.lv_scores);
        TextView tvEmpty = findViewById(R.id.tv_empty);

        btnBack.setOnClickListener(v -> finish());

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<DatabaseHelper.ScoreEntry> scores = dbHelper.getTop10();
        dbHelper.close();

        if (scores.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvScores.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            lvScores.setVisibility(View.VISIBLE);

            List<String> items = new ArrayList<>();
            for (int i = 0; i < scores.size(); i++) {
                DatabaseHelper.ScoreEntry e = scores.get(i);
                items.add((i + 1) + ".  " + e.name + "   —   " + e.score + " pts");
            }
            lvScores.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, items));
        }
    }
}
