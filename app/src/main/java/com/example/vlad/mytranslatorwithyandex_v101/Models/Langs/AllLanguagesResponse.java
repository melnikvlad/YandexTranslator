package com.example.vlad.mytranslatorwithyandex_v101.Models.Langs;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllLanguagesResponse {

    @SerializedName("dirs")
    @Expose
    private List<String> dirs = null;
    @SerializedName("langs")
    @Expose
    private Langs langs;

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public Langs getLangs() {
        return langs;
    }

    public void setLangs(Langs langs) {
        this.langs = langs;
    }

}
