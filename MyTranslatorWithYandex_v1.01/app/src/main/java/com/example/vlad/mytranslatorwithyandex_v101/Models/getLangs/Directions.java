package com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs;

import java.util.List;

public class Directions {
    private List<String> dirs;

    public Directions() {
    }

    public Directions(List<String> dirs) {
        this.dirs = dirs;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }
}

