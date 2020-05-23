package com.example.awareness;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.awareness.Question;
import com.example.awareness.R;
import com.example.awareness.ui.learningactivity.LearningActivity;
import com.example.awareness.ui.learningactivity.LearningAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.awareness.Constants.User.ACCESS_MODULE;
import static com.example.awareness.Constants.User.ACCESS_QUESTION;
import static com.example.awareness.Constants.User.PROGRESS_LINK;
import static com.example.awareness.Constants.User.PROGRESS_PDF;
import static com.example.awareness.Constants.User.accessModule;
import static com.example.awareness.Constants.User.accessQuestion;
import static com.example.awareness.Constants.User.progressLink;
import static com.example.awareness.Constants.User.progressPdf;
import static com.example.awareness.ui.learningactivity.LearningActivity.quizBottomSheet;
import static com.example.awareness.ui.learningactivity.LearningActivity.quizBottomSheetDialog;

public class QuizUtils {

    private static int questionPosition;
    private static List<Question> questions = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static TextView questionNumber,question;
    @SuppressLint("StaticFieldLeak")
    private static RadioButton option1,option2,option3,option4;
    @SuppressLint("StaticFieldLeak")
    private static Button checkNext;
    @SuppressLint("StaticFieldLeak")
    private static RadioGroup radioGroup;
    private static boolean nextQuestion = false;

    public static void createQuiz(final int moduleNumber) {

        questionNumber = quizBottomSheet.findViewById(R.id.question_number);
        question = quizBottomSheet.findViewById(R.id.question);
        option1 = quizBottomSheet.findViewById(R.id.option1);
        option2 = quizBottomSheet.findViewById(R.id.option2);
        option3 = quizBottomSheet.findViewById(R.id.option3);
        option4 = quizBottomSheet.findViewById(R.id.option4);
        checkNext = quizBottomSheet.findViewById(R.id.check_next);
        radioGroup = quizBottomSheet.findViewById(R.id.radio_group);
        radioGroup.clearCheck();

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String path = "modules/" + moduleNumber + "/quiz";
        firestore.collection(path).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    questions.clear();
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

                    if(moduleNumber == Constants.User.accessModule ) {
                        questionPosition = Constants.User.accessQuestion - 1;
                    }else{
                        questionPosition = 0;
                    }

                    if (questionPosition < questions.size()) {
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
                } else if (checkNext.getText().toString().equals("Next Module")) {
                    quizBottomSheetDialog.cancel();
                } else {
                    if (isCorrect(questions.get(questionPosition).getAnswer())) {

                        // Todo: UI after true
                        if (questionPosition + 1 == questions.size()) {
                            nextQuestion = false;
                            accessModule ++;
                            progressLink = false;
                            progressPdf = false;
                            accessQuestion = 1;
                            updateProgress();
                            LearningActivity.learningAdapter.notifyDataSetChanged();
                            checkNext.setText("Next Module");
                        } else {
                            nextQuestion = true;
                            accessQuestion++;
                            updateProgress();
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

    private static boolean isCorrect(int answer) {
        return (radioGroup.getCheckedRadioButtonId() == R.id.option1 && answer == 1) ||
                (radioGroup.getCheckedRadioButtonId() == R.id.option2 && answer == 2) ||
                (radioGroup.getCheckedRadioButtonId() == R.id.option3 && answer == 3) ||
                (radioGroup.getCheckedRadioButtonId() == R.id.option4 && answer == 4);
    }

    private static void setQuestion(int questionPosition) {
        questionNumber.setText(questions.get(questionPosition).getQuestionNumber() + "/" + questions.size());

        question.setText(questions.get(questionPosition).getQuestion());
        option1.setText(questions.get(questionPosition).getOption1());
        option2.setText(questions.get(questionPosition).getOption2());
        option3.setText(questions.get(questionPosition).getOption3());
        option4.setText(questions.get(questionPosition).getOption4());
    }

    private static void updateProgress(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Todo: userID?
        String userID = "0123456789";
        Map<String,Object> newProgress = new HashMap<>();
        newProgress.put(ACCESS_MODULE,accessModule);
        newProgress.put(PROGRESS_LINK,progressLink);
        newProgress.put(PROGRESS_PDF,progressPdf);
        newProgress.put(ACCESS_QUESTION,accessQuestion);

        firestore.collection("users").document(userID).update(newProgress);
    }

    static class SortbyQuestionNumber implements Comparator<Question> {
        @Override
        public int compare(Question a, Question b) {
            return a.getQuestionNumber() - b.getQuestionNumber();
        }
    }

}
