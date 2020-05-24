package com.example.awareness;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.awareness.ui.learningactivity.LearningActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private static TextView questionNumber, question;
    @SuppressLint("StaticFieldLeak")
    private static RadioButton option1, option2, option3, option4;
    @SuppressLint("StaticFieldLeak")
    private static Button checkNext,previous;
    @SuppressLint("StaticFieldLeak")
    private static RadioGroup radioGroup;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private static ScrollView mainContent;
    private static boolean done = false;

    public static void createQuiz(final int moduleNumber) {

        questionNumber = quizBottomSheet.findViewById(R.id.question_number);
        question = quizBottomSheet.findViewById(R.id.question);
        option1 = quizBottomSheet.findViewById(R.id.option1);
        option2 = quizBottomSheet.findViewById(R.id.option2);
        option3 = quizBottomSheet.findViewById(R.id.option3);
        option4 = quizBottomSheet.findViewById(R.id.option4);
        checkNext = quizBottomSheet.findViewById(R.id.check_next);
        previous = quizBottomSheet.findViewById(R.id.previous);
        radioGroup = quizBottomSheet.findViewById(R.id.radio_group);
        progressBar = quizBottomSheet.findViewById(R.id.progress_circle);
        mainContent = quizBottomSheet.findViewById(R.id.main_content);
        mainContent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
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

                    if (moduleNumber == Constants.User.accessModule) {
                        questionPosition = accessQuestion - 1;
                    } else {
                        questionPosition = 0;
                    }

                    if (questionPosition < questions.size()) {
                        setQuestion(questionPosition);
                        progressBar.setVisibility(View.GONE);
                        mainContent.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        checkNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (done) {
                    checkNext.setText("Submit");
                    done = false;
                    quizBottomSheetDialog.dismiss();
                } else {
                    Handler handler = new Handler();

                    if (isCorrect(questions.get(questionPosition).getAnswer())) {

                        final RadioButton correctAnswer = quizBottomSheet.findViewById(radioGroup.getCheckedRadioButtonId());
                        correctAnswer.setBackgroundResource(R.drawable.correct_answer);

                        if (questionPosition == questions.size() - 1) {

                            if (moduleNumber == accessModule) {
                                accessModule++;
                                progressLink = false;
                                progressPdf = false;
                                accessQuestion = 1;
                                updateProgress();
                                LearningActivity.learningAdapter.notifyDataSetChanged();
                            }

                            done = true;
                            checkNext.setText("Done");
                        } else {
                            if (moduleNumber == accessModule) {
                                accessQuestion++;
                                updateProgress();
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    questionPosition++;
                                    setQuestion(questionPosition);
                                }
                            }, 500);
                        }
                    } else {
                        if (radioGroup.getCheckedRadioButtonId() != -1) {
                            final RadioButton incorrectAnswer = quizBottomSheet.findViewById(radioGroup.getCheckedRadioButtonId());
                            incorrectAnswer.setBackgroundResource(R.drawable.wrong_answer);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    radioGroup.clearCheck();
                                    incorrectAnswer.setBackgroundColor(Color.TRANSPARENT);
                                }
                            }, 500);
                        }
                    }
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionPosition--;
                setQuestion(questionPosition);
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
        if(questionPosition == 0){
            previous.setVisibility(View.INVISIBLE);
        }else {
            previous.setVisibility(View.VISIBLE);
        }
        questionNumber.setText(questions.get(questionPosition).getQuestionNumber() + "/" + questions.size());
        radioGroup.clearCheck();

        question.setText(questions.get(questionPosition).getQuestion());
        option1.setText(questions.get(questionPosition).getOption1());
        option1.setBackgroundColor(Color.TRANSPARENT);
        option2.setText(questions.get(questionPosition).getOption2());
        option2.setBackgroundColor(Color.TRANSPARENT);
        option3.setText(questions.get(questionPosition).getOption3());
        option3.setBackgroundColor(Color.TRANSPARENT);
        option4.setText(questions.get(questionPosition).getOption4());
        option4.setBackgroundColor(Color.TRANSPARENT);
    }

    private static void updateProgress() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Todo: userID?
        String userID = "0123456789";
        Map<String, Object> newProgress = new HashMap<>();
        newProgress.put(ACCESS_MODULE, accessModule);
        newProgress.put(PROGRESS_LINK, progressLink);
        newProgress.put(PROGRESS_PDF, progressPdf);
        newProgress.put(ACCESS_QUESTION, accessQuestion);

        firestore.collection("users").document(userID).update(newProgress);
    }

    static class SortbyQuestionNumber implements Comparator<Question> {
        @Override
        public int compare(Question a, Question b) {
            return a.getQuestionNumber() - b.getQuestionNumber();
        }
    }
}
