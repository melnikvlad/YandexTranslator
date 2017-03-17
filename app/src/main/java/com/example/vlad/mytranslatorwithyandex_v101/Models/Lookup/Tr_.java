package com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tr_ {

    @SerializedName("text")
    @Expose
    private String text;

    //======================================== GETTERS & SETTERS ===================================================================
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

