package com.bhoomik.Vardaan.model;

public class Language {

    String language;
    String translateLanguageId;

    public Language(String language, String translateLanguageId) {
        this.language = language;
        this.translateLanguageId = translateLanguageId;
    }

    public String getLanguage() {
        return language;
    }

    public String getTranslateLanguageId() {
        return translateLanguageId;
    }
}
