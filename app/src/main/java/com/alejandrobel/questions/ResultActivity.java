package com.alejandrobel.questions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tvFinalScore, tvCorrectAnswers, tvTotalQuestions, tvPercentage;
    Button btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Inicializar vistas
        tvFinalScore = findViewById(R.id.tvFinalScore);
        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);
        tvTotalQuestions = findViewById(R.id.tvTotalQuestions);
        tvPercentage = findViewById(R.id.tvPercentage);
        btnFinish = findViewById(R.id.btnFinish);

        // Obtener los datos pasados desde MainActivity
        Intent intent = getIntent();
        int correctAnswers = intent.getIntExtra("correctAnswers", 0);
        int totalQuestions = intent.getIntExtra("totalQuestions", 0);

        // Calcular el porcentaje
        double percentage = ((double) correctAnswers / totalQuestions) * 100;

        // Mostrar los datos en las vistas
        tvCorrectAnswers.setText("Respuestas Correctas: " + correctAnswers);
        tvTotalQuestions.setText("Total de Preguntas: " + totalQuestions);
        tvPercentage.setText("Porcentaje de aciertos: " + String.format("%.2f", percentage) + "%");

        // Botón para finalizar
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finaliza la actividad y regresa al inicio
                finish();
            }
        });
    }
}