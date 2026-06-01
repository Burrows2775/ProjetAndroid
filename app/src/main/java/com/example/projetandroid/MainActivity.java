package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPlay = findViewById(R.id.bouton_jouer);
        Button btnHighscores = findViewById(R.id.bouton_highscores);

        btnPlay.setOnClickListener(v ->
                startActivity(new Intent(this, GameActivity.class)));

        btnHighscores.setOnClickListener(v ->
                startActivity(new Intent(this, HighscoreActivity.class)));
    }
}
