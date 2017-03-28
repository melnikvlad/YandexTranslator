package com.example.vlad.mytranslatorwithyandex_v101.Models.Translate;



public class History {
    private String word;
    private String translate;
    private String direction;

    public History() {
    }

    public History(String word, String translate, String direction) {
        this.word = word;
        this.translate = translate;
        this.direction = direction;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) {
        this.translate = translate;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
