package com.example.awareness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.awareness.ui.CertificateActivity;
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

import static com.example.awareness.Constants.User;
import static com.example.awareness.ui.learningactivity.LearningActivity.modules;
import static com.example.awareness.ui.learningactivity.LearningActivity.quizBottomSheet;
import static com.example.awareness.ui.learningactivity.LearningActivity.quizBottomSheetDialog;

public class QuizUtils {

    private static int questionPosition;
    private static List<Question> questions = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static TextView questionNumber, question, guideline;
    @SuppressLint("StaticFieldLeak")
    private static RadioButton option1, option2, option3, option4;
    @SuppressLint("StaticFieldLeak")
    private static Button checkNext, previous;
    @SuppressLint("StaticFieldLeak")
    private static RadioGroup radioGroup;
    @SuppressLint("StaticFieldLeak")
    private static ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private static ScrollView mainContent;
    private static boolean done = false;
    private static boolean certificate = false;
    private static boolean showGuideline = User.accessModule == 1 && User.accessQuestion == 1;


    public static void createQuiz(final Context context, final int moduleNumber) {

        questionNumber = quizBottomSheet.findViewById(R.id.question_number);
        question = quizBottomSheet.findViewById(R.id.question);
        option1 = quizBottomSheet.findViewById(R.id.option1);
        option2 = quizBottomSheet.findViewById(R.id.option2);
        option3 = quizBottomSheet.findViewById(R.id.option3);
        option4 = quizBottomSheet.findViewById(R.id.option4);
        guideline = quizBottomSheet.findViewById(R.id.guideline);
        checkNext = quizBottomSheet.findViewById(R.id.check_next);
        previous = quizBottomSheet.findViewById(R.id.previous);
        radioGroup = quizBottomSheet.findViewById(R.id.radio_group);
        progressBar = quizBottomSheet.findViewById(R.id.progress_circle);
        mainContent = quizBottomSheet.findViewById(R.id.main_content);
        mainContent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        radioGroup.clearCheck();

        if (showGuideline) {

            question.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            previous.setVisibility(View.GONE);
            questionNumber.setVisibility(View.GONE);
            guideline.setVisibility(View.VISIBLE);
            guideline.setText(R.string.hindi_test_guideline);
            checkNext.setText("Start test");
            progressBar.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);

        } else {
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
                            questionPosition = User.accessQuestion - 1;
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
        }


        checkNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (done) {
                    checkNext.setText("Submit");
                    done = false;
                    quizBottomSheetDialog.dismiss();
                    return;
                }
                if (certificate) {
                    checkNext.setText("Submit");
                    certificate = false;
                    quizBottomSheetDialog.dismiss();
                    context.startActivity(new Intent(context, CertificateActivity.class));
                    return;
                }
                if (showGuideline) {
                    showGuideline = false;
                    createQuiz(context, moduleNumber);
                    checkNext.setText("Submit");
                    return;
                }
                Handler handler = new Handler();
                if (isCorrect(questions.get(questionPosition).getAnswer())) {

                    final RadioButton correctAnswer = quizBottomSheet.findViewById(radioGroup.getCheckedRadioButtonId());
                    correctAnswer.setBackgroundResource(R.drawable.correct_answer);

                    if (questionPosition == questions.size() - 1) {
                        done = true;
                        certificate = false;

                        checkNext.setEnabled(false);
                        previous.setEnabled(false);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkNext.setEnabled(true);
                                previous.setEnabled(true);
                                question.setVisibility(View.GONE);
                                radioGroup.setVisibility(View.GONE);
                                previous.setVisibility(View.GONE);
                                questionNumber.setVisibility(View.GONE);
                                guideline.setVisibility(View.VISIBLE);
                                guideline.setText(R.string.hindi_start_next);
                                checkNext.setText("Done");

                            }
                        }, 500);

                        if (moduleNumber == User.accessModule) {
                            if (moduleNumber == modules.get(modules.size() - 1).getModuleNumber()) {
                                User.progressLecture = true;
                                User.progressLink = true;
                                User.progressPdf = true;
                                done = false;
                                certificate = true;
                                checkNext.setText("View Certificate");
                            } else {
                                User.accessModule++;
                                User.progressLecture = false;
                                User.progressPdf = false;
                                // To be changed if link
                                User.progressLink = true;
                            }
                            User.accessQuestion = 1;
                            updateProgress(context);
                            LearningActivity.learningAdapter.notifyDataSetChanged();

                        }


                    } else {
                        if (moduleNumber == User.accessModule) {
                            User.accessQuestion++;
                            updateProgress(context);
                        }
                        checkNext.setEnabled(false);
                        previous.setEnabled(false);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkNext.setEnabled(true);
                                previous.setEnabled(true);
                                questionPosition++;
                                setQuestion(questionPosition);
                            }
                        }, 500);
                    }
                } else {
                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                        final RadioButton incorrectAnswer = quizBottomSheet.findViewById(radioGroup.getCheckedRadioButtonId());
                        incorrectAnswer.setBackgroundResource(R.drawable.wrong_answer);
                        checkNext.setEnabled(false);
                        previous.setEnabled(false);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkNext.setEnabled(true);
                                previous.setEnabled(true);
                                radioGroup.clearCheck();
                                incorrectAnswer.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }, 500);
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
        if (questionPosition == 0) {
            previous.setVisibility(View.INVISIBLE);
        } else {
            previous.setVisibility(View.VISIBLE);
        }
        question.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
        questionNumber.setVisibility(View.VISIBLE);
        guideline.setVisibility(View.GONE);

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

    private static void updateProgress(Context context) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        String userId = preferences.getString(User.USER_CONTACT_NUMBER, null);
        Map<String, Object> newProgress = new HashMap<>();
        newProgress.put(User.ACCESS_MODULE, User.accessModule);
        newProgress.put(User.PROGRESS_LINK, User.progressLink);
        newProgress.put(User.PROGRESS_PDF, User.progressPdf);
        newProgress.put(User.PROGRESS_LECTURE, User.progressLecture);
        newProgress.put(User.ACCESS_QUESTION, User.accessQuestion);

        if (userId != null) {
            firestore.collection("users").document(userId).update(newProgress);
        }
    }

    static class SortbyQuestionNumber implements Comparator<Question> {
        @Override
        public int compare(Question a, Question b) {
            return a.getQuestionNumber() - b.getQuestionNumber();
        }
    }
}
