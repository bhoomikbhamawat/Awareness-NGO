package com.bhoomik.Vardaan.ui.language;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bhoomik.Vardaan.R;
import com.bhoomik.Vardaan.model.Language;
import com.google.mlkit.nl.translate.TranslateLanguage;

import java.util.ArrayList;
import java.util.List;

public class QuizLanguage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_language);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView languageRecyclerView = findViewById(R.id.language_recyclerview);
        languageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        languageRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        final List<Language> languages = new ArrayList<Language>() {
            {
                add(new Language("বাংলা", TranslateLanguage.BENGALI));
                add(new Language("English", TranslateLanguage.ENGLISH));
                add(new Language("ગુજરતી", TranslateLanguage.GUJARATI));
                add(new Language("हिन्दी", TranslateLanguage.HINDI));
                add(new Language("मराठी", TranslateLanguage.MARATHI));
                add(new Language("தமிழ்", TranslateLanguage.TAMIL));
                add(new Language("తెలుగు", TranslateLanguage.TELUGU));
                add(new Language("اردو", TranslateLanguage.URDU));
            }
        };

        LanguageAdapter languageAdapter = new LanguageAdapter(this, languages);

        languageRecyclerView.setAdapter(languageAdapter);


    }


}




