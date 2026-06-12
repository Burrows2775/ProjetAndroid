package com.example.projetandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private static final String LANGUAGE_FR = "fr";
    private static final String LANGUAGE_EN = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, windowInsets) -> {
            int insetTypes = WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout();
            int topInset = Math.max(
                windowInsets.getInsets(insetTypes).top,
                windowInsets.getInsets(insetTypes).top
            );

            view.setPadding(
                view.getPaddingLeft(),
                topInset,
                view.getPaddingRight(),
                view.getPaddingBottom()
            );

            return windowInsets;
        });

        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);

        Button btnPlay = findViewById(R.id.bouton_jouer);
        Button btnHighscores = findViewById(R.id.bouton_highscores);
        Spinner spinnerLanguage = findViewById(R.id.spinner_language);

        btnPlay.setOnClickListener(v ->
                startActivity(new Intent(this, GameActivity.class)));

        btnHighscores.setOnClickListener(v ->
                startActivity(new Intent(this, HighscoreActivity.class)));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{getString(R.string.langue_fr), getString(R.string.langue_en)}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(adapter);

        String currentLanguage = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        int selectedIndex = currentLanguage.startsWith(LANGUAGE_EN) ? 1 : 0;
        spinnerLanguage.setSelection(selectedIndex, false);

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String desiredLanguage = position == 1 ? LANGUAGE_EN : LANGUAGE_FR;
                if (!AppCompatDelegate.getApplicationLocales().toLanguageTags().startsWith(desiredLanguage)) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(desiredLanguage));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No-op.
            }
        });
    }
}
