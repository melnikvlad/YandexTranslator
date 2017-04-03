package com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Syn {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("pos")
    @Expose
    private String pos;

    //======================================== GETTERS & SETTERS ===================================================================
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}