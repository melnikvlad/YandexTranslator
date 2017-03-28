package com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs;

import java.util.List;

public class Languages {
    private List<String> keys;
    private List<String> values;

    public Languages() {
    }

    public Languages(List<String> keys, List<String> values) {
        this.keys = keys;
        this.values = values;
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
