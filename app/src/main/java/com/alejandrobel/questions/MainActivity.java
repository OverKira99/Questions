package com.alejandrobel.questions;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvQuestion;
    private RadioGroup rgOptions;
    private RadioButton rbOption1, rbOption2, rbOption3;
    private Button btnSubmit;
    private ImageView imgQuestion;

    private List<Questions> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        btnSubmit = findViewById(R.id.btnSubmit);
        imgQuestion = findViewById(R.id.imgQuestion);

        loadQuestionsFromFirebase();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
                currentQuestionIndex++;
                if (currentQuestionIndex < questions.size()) {
                    displayQuestion(currentQuestionIndex);
                } else {
                    // Mostrar el resultado final
                    saveResultsToFirebaseAndShowResult();
                    Toast.makeText(MainActivity.this, "Evaluación finalizada. Correctas: " + correctAnswers, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadQuestionsFromFirebase() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("questions");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
                    Questions question = questionSnapshot.getValue(Questions.class);
                    questions.add(question);
                }
                if (!questions.isEmpty()) {
                    displayQuestion(currentQuestionIndex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al cargar las preguntas", databaseError.toException());
            }
        });
    }

    private void displayQuestion(int index) {
        Questions currentQuestion = questions.get(index);
        tvQuestion.setText(currentQuestion.getText());

        rbOption1.setText(currentQuestion.getOptions().get("option1"));
        rbOption2.setText(currentQuestion.getOptions().get("option2"));
        rbOption3.setText(currentQuestion.getOptions().get("option3"));

        // Cargar la imagen usando Glide
        if (currentQuestion.getImageUrl() != null && !currentQuestion.getImageUrl().isEmpty()) {
            imgQuestion.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(currentQuestion.getImageUrl())
                    .into(imgQuestion);
        } else {
            imgQuestion.setVisibility(View.GONE);  // Ocultar la imagen si no hay URL
        }

        // Limpiar las selecciones previas
        rgOptions.clearCheck();
    }

    private void checkAnswer() {
        Questions currentQuestion = questions.get(currentQuestionIndex);
        String selectedOption = "";

        // Obtener la opción seleccionada
        switch (rgOptions.getCheckedRadioButtonId()) {
            case R.id.rbOption1:
                selectedOption = "option1";
                break;
            case R.id.rbOption2:
                selectedOption = "option2";
                break;
            case R.id.rbOption3:
                selectedOption = "option3";
                break;
        }

        // Verificar si la opción seleccionada es correcta
        if (selectedOption.equals(currentQuestion.getCorrectOption())) {
            correctAnswers++;
        }
    }

    private void saveResultsToFirebaseAndShowResult() {
        DatabaseReference resultsRef = FirebaseDatabase.getInstance().getReference("results");

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("correctAnswers", correctAnswers);
        resultData.put("totalQuestions", questions.size());

        // Guardar los resultados en Firebase
        resultsRef.push().setValue(resultData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MainActivity.this, "Resultados guardados correctamente", Toast.LENGTH_LONG).show();

                // Pasar a la actividad de Resultados
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra("correctAnswers", correctAnswers);
                intent.putExtra("totalQuestions", questions.size());
                startActivity(intent);
                finish(); // Opcional: si no quieres que el usuario regrese a la evaluación
            } else {
                Toast.makeText(MainActivity.this, "Error al guardar los resultados", Toast.LENGTH_LONG).show();
            }
        });
    }

}
