package com.example.awareness.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.awareness.Question;
import com.example.awareness.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {

    int questionPosition;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    List<Question> questions = new ArrayList<>();
    TextView questionNumber, question;
    RadioButton option1, option2, option3, option4;
    Button checkNext;
    RadioGroup radioGroup;
    boolean nextQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        int mModuleNumber = intent.getIntExtra("ModuleNumber", 0);

        questionNumber = findViewById(R.id.question_number);
        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        checkNext = findViewById(R.id.check_next);
        radioGroup = findViewById(R.id.radio_group);


        String path = "modules/" + mModuleNumber + "/quiz";
        firestore.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        questions.add(new Question(
                                Integer.parseInt(document.getId()),
                                document.getString("Question"),
                                Objects.requireNonNull(document.getLong("Answer")).intValue(),
                                document.getString("1"),
                                document.getString("2"),
                                document.getString("3"),
                                document.getString("4")));
                    }
                    Collections.sort(questions, new SortbyQuestionNumber());

                    // Todo: get candidate progress from firebase
                    questionPosition = 0;
                    if(questionPosition < questions.size()) {
                        setQuestion(questionPosition);
                    }
                }
            }
        });


        checkNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextQuestion) {
                    radioGroup.clearCheck();
                    questionPosition++;
                    setQuestion(questionPosition);
                    nextQuestion = false;
                    checkNext.setText("Submit");
                }else if(checkNext.getText().toString().equals("Next Module")){
                    finish();
                }
                else {
                    if (isCorrect(questions.get(questionPosition).getAnswer())) {

                        // Todo: UI after true
                        if (questionPosition + 1 == questions.size()) {
                            nextQuestion = false;
                            checkNext.setText("Next Module");
                        } else {
                            nextQuestion = true;
                            checkNext.setText("Next");
                        }
                    } else {
                        nextQuestion = false;
                        // Todo: UI after false
                        radioGroup.clearCheck();
                    }

                }
            }
        });

    }

    private boolean isCorrect(int answer) {
        return (radioGroup.getCheckedRadioButtonId() == R.id.option1 && answer == 1) ||
                (radioGroup.getCheckedRadioButtonId() == R.id.option2 && answer == 2) ||
                (radioGroup.getCheckedRadioButtonId() == R.id.option3 && answer == 3) ||
                (radioGroup.getCheckedRadioButtonId() == R.id.option4 && answer == 4);
    }

    private void setQuestion(int questionPosition) {
        questionNumber.setText(questions.get(questionPosition).getQuestionNumber() + "/" + questions.size());

        question.setText(questions.get(questionPosition).getQuestion());
        option1.setText(questions.get(questionPosition).getOption1());
        option2.setText(questions.get(questionPosition).getOption2());
        option3.setText(questions.get(questionPosition).getOption3());
        option4.setText(questions.get(questionPosition).getOption4());
    }

    static class SortbyQuestionNumber implements Comparator<Question> {
        @Override
        public int compare(Question a, Question b) {
            return a.getQuestionNumber() - b.getQuestionNumber();
        }
    }

}
