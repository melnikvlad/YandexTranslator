package com.example.vlad.mytranslatorwithyandex_v101.Models.Langs;

import java.util.List;

public class Languages {
    private List<String> keys;
    private List<String> values;
    private List<String> dirs;

    public Languages() {
    }

    public Languages(List<String>dirs,List<String> keys, List<String> values) {
        this.keys = keys;
        this.values = values;
        this.dirs = dirs;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
