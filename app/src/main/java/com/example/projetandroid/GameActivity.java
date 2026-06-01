package com.example.projetandroid;

import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private static final int MAX_LIVES = 3;

    private TextView tvScore, tvLives, tvQuestion;
    private EditText etAnswer;
    private Button btnValidate;

    private int score = 0;
    private int lives = MAX_LIVES;
    private int currentAnswer;
    private final Random random = new Random();
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dbHelper = new DatabaseHelper(this);

        tvScore = findViewById(R.id.tv_score);
        tvLives = findViewById(R.id.tv_lives);
        tvQuestion = findViewById(R.id.tv_question);
        etAnswer = findViewById(R.id.et_answer);
        btnValidate = findViewById(R.id.btn_validate);

        btnValidate.setOnClickListener(v -> checkAnswer());
        etAnswer.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAnswer();
                return true;
            }
            return false;
        });

        updateHUD();
        generateQuestion();
    }

    private void generateQuestion() {
        int op = random.nextInt(4);
        int a, b;
        String question;

        switch (op) {
            case 0: // addition
                a = random.nextInt(50) + 1;
                b = random.nextInt(50) + 1;
                currentAnswer = a + b;
                question = a + " + " + b + " = ?";
                break;
            case 1: // soustraction
                a = random.nextInt(50) + 1;
                b = random.nextInt(50) + 1;
                currentAnswer = a - b;
                question = a + " - " + b + " = ?";
                break;
            case 2: // multiplication
                a = random.nextInt(12) + 1;
                b = random.nextInt(12) + 1;
                currentAnswer = a * b;
                question = a + " × " + b + " = ?";
                break;
            default: // division entière garantie
                b = random.nextInt(9) + 2;            // diviseur 2..10
                currentAnswer = random.nextInt(10) + 1; // résultat 1..10
                a = b * currentAnswer;
                question = a + " ÷ " + b + " = ?";
                break;
        }

        tvQuestion.setText(question);
        etAnswer.setText("");
        etAnswer.requestFocus();
    }

    private void checkAnswer() {
        String input = etAnswer.getText().toString().trim();
        if (input.isEmpty() || input.equals("-")) return;

        int userAnswer;
        try {
            userAnswer = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.saisie_invalide), Toast.LENGTH_SHORT).show();
            return;
        }

        if (userAnswer == currentAnswer) {
            score++;
            Toast.makeText(this, getString(R.string.bonne_reponse), Toast.LENGTH_SHORT).show();
        } else {
            lives--;
            Toast.makeText(this,
                    getString(R.string.mauvaise_reponse, currentAnswer),
                    Toast.LENGTH_SHORT).show();
        }

        updateHUD();

        if (lives <= 0) {
            showGameOverDialog();
        } else {
            generateQuestion();
        }
    }

    private void updateHUD() {
        tvScore.setText(getString(R.string.score, score));
        tvLives.setText(getString(R.string.vies, lives));
    }

    private void showGameOverDialog() {
        EditText etName = new EditText(this);
        etName.setHint(getString(R.string.nom_hint));
        etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.game_over))
                .setMessage(getString(R.string.entrez_nom))
                .setView(etName)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.enregistrer), (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    if (name.isEmpty()) name = "???";
                    dbHelper.insertScore(name, score);
                    finish();
                })
                .setNegativeButton(getString(R.string.ignorer), (dialog, which) -> finish())
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
