package com.example.vlad.mytranslatorwithyandex_v101.Models.Translate;

import java.util.List;

public class FavouriteDetail {
    private String word;
    private String translate;
    private String pos;
    private List<String> top_row;
    private List<String> bot_row;
    private String direction;

    public FavouriteDetail(String word, String translate, String pos, List<String> top_row, List<String> bot_row, String direction) {
        this.word = word;
        this.translate = translate;
        this.pos = pos;
        this.top_row = top_row;
        this.bot_row = bot_row;
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

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public List<String> getTop_row() {
        return top_row;
    }

    public void setTop_row(List<String> top_row) {
        this.top_row = top_row;
    }

    public List<String> getBot_row() {
        return bot_row;
    }

    public void setBot_row(List<String> bot_row) {
        this.bot_row = bot_row;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
