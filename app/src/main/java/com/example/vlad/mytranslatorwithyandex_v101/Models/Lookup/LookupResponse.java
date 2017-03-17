package com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LookupResponse {

    @SerializedName("head")
    @Expose
    private Head head;
    @SerializedName("def")
    @Expose
    private List<Def> def = null;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }

}
