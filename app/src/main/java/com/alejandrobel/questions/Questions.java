package com.alejandrobel.questions;

import java.util.Map;

public class Questions {
    private String text;
    private Map<String, String> options;
    private String correctOption;
    private String imageUrl; // Nuevo campo para la URL de la imagen

    public Questions() {
        // Constructor vacío requerido para Firebase
    }

    public Questions(String text, Map<String, String> options, String correctOption, String imageUrl) {
        this.text = text;
        this.options = options;
        this.correctOption = correctOption;
        this.imageUrl = imageUrl;
    }

    // Getters y Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
