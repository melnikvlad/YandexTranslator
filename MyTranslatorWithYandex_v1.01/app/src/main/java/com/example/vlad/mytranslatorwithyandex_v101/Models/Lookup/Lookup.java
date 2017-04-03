package com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup;


import java.util.List;

public class Lookup {
    private String def;
    private String pos;
    private List<String> top_row;
    private List<String> bot_row;

    public Lookup(String def, String pos, List<String> top_row, List<String> bot_row) {
        this.def = def;
        this.pos = pos;
        this.top_row = top_row;
        this.bot_row = bot_row;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
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
}
