package com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup;

import java.util.ArrayList;
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

    private int getDefSize(){ // How many Def[] arrays ?
        return this.getDef().size();
    }

    public List<String> RV_top_items_row(){// Inflate cardview top row for Recycler View adapter
        List<Tr> tr_i;
        List<String> rv_rowTop_i = new ArrayList<>();
        List<String> pos_i = new ArrayList<>();
        List<String> gen_i = new ArrayList<>();
        List<String> syn_i = new ArrayList<>();
        String rowTop="";

        tr_i = this.getTranslates(); // All translates

        for (int i=0;i<tr_i.size();i++) { // Loop through all translates
            if(tr_i.get(i).getPos() != null) { // if pos field exists --> add it to  pos list
                pos_i.add(tr_i.get(i).getPos());
            }
            else {
                pos_i.add(""); // else make it empty

            }
            if(tr_i.get(i).getGen() != null) {  // if gen field exists --> add it to gen list
                gen_i.add(tr_i.get(i).getGen());
            }
            else {
                gen_i.add("");  // else make it empty
            }
            if(tr_i.get(i).getSyn()!= null) { // if syn array exists --> add it to syn list
                for (int j=0;j<tr_i.get(i).getSyn().size();j++){
                    syn_i.add(tr_i.get(i).getSyn().get(j).getText());
                }
            }
            else {
                syn_i.add("");  // else make it empty
            }

            String listtoString = listToString(syn_i);
            if(!listtoString.isEmpty()){        // Format string
                rowTop = tr_i.get(i).getText()+", " + listtoString;
            }
            else {
                rowTop = tr_i.get(i).getText();
            }
            rv_rowTop_i.add(rowTop);// top row would be like : Translate + Synonyms
            syn_i.clear();
        }
        return rv_rowTop_i;
    }

    public List<String> RV_bot_items_row(){ // Inflate cardview bottom row for Recycler View adapter
        String rowBot = "";
        List<Tr> tr_i;
        List<String> rv_rowBot_i = new ArrayList<>();
        List<String> mean_i= new ArrayList<>();

        tr_i = this.getTranslates(); // All translates

        for (int i=0;i<tr_i.size();i++) { // if mean array exists --> add it to mean list
            if(tr_i.get(i).getMean()!= null) {
                for (int j=0;j<tr_i.get(i).getMean().size();j++){
                    mean_i.add(tr_i.get(i).getMean().get(j).getText());
                }
            }
            else {
                mean_i.add(""); // else make it empty
            }
            String listtoString = listToString(mean_i);
            if(!listtoString.isEmpty()){ // Format string
                rowBot = "("+listToString(mean_i)+")";
            }
            else{
                rowBot="";
            }
            rv_rowBot_i.add(rowBot); // bottom row would be like : Means
            mean_i.clear();
        }
        return rv_rowBot_i;
    }

    public List<Tr> getTranslates(){  // Get list of all translates
        List<List<Tr>> trList = new ArrayList<>();
        List<Tr> tr_i = new ArrayList<>();

        trList  = this.getTrLists();
        tr_i    = this.getTrListsObjects(trList);
        return tr_i;
    }

    private List<List<Tr>> getTrLists(){ // Get all Tr[] from every Def[] array
        List<List<Tr>> list = new ArrayList<>();
        for(int i=0;i<this.getDefSize();i++){
            list.add(this.getDef().get(i).getTr());
        }
        return list;
    }

    private List<Tr> getTrListsObjects(List<List<Tr>> lists){ // Get all Tr{} objects from every Tr[] array
        List<Tr> objs = new ArrayList<>();
        for(int i=0;i<lists.size();i++) {
            for (int j=0;j<lists.get(i).size();j++) {
                objs.add(lists.get(i).get(j));
            }
        }
        return objs;
    }

    private String listToString(List<String> list){  // Turn list items to single textline
        String text="";
        for(int i=0;i<list.size();i++){
            if(i == list.size()-1){
                text+=list.get(i).toString();
            }
            else {
                text+=list.get(i).toString()+", ";
            }
        }
        return text;
    }
}
