package com.example.translator.model;

public class TranslationResponse {
    private String originalText;
    private String translatedText;
    private String targetLang;
    private String error;

    public TranslationResponse() {}

    public TranslationResponse(String translatedText) {
        this.translatedText = translatedText;
    }

    public TranslationResponse(String originalText, String translatedText, String targetLang) {
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.targetLang = targetLang;
    }

    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }

    public String getTranslatedText() { return translatedText; }
    public void setTranslatedText(String translatedText) { this.translatedText = translatedText; }

    public String getTargetLang() { return targetLang; }
    public void setTargetLang(String targetLang) { this.targetLang = targetLang; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}