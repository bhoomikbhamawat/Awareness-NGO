package com.example.awareness;

public class Question {

    int mQuestionNumber;
    String mQuestion;
    String mOption1;
    String mOption2;
    String mOption3;
    String mOption4;
    int mAnswer;

    public Question(int questionNumber, String question, int answer, String option1, String option2, String option3, String option4) {
        this.mQuestionNumber = questionNumber;
        this.mQuestion = question;
        this.mOption1 = option1;
        this.mOption2 = option2;
        this.mOption3 = option3;
        this.mOption4 = option4;
        this.mAnswer = answer;
    }

    public int getQuestionNumber() {
        return mQuestionNumber;
    }

    public int getAnswer() {
        return mAnswer;
    }

    public String getOption1() {
        return mOption1;
    }

    public String getOption2() {
        return mOption2;
    }

    public String getOption3() {
        return mOption3;
    }

    public String getOption4() {
        return mOption4;
    }

    public String getQuestion() {
        return mQuestion;
    }
}

