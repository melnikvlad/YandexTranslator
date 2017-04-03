package com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ex {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("tr")
    @Expose
    private List<Tr_> tr = null;

    //======================================== GETTERS & SETTERS ===================================================================

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tr_> getTr() {
        return tr;
    }

    public void setTr(List<Tr_> tr) {
        this.tr = tr;
    }

}
