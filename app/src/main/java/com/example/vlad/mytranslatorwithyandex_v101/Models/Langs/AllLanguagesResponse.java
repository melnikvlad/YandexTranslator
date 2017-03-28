package com.example.vlad.mytranslatorwithyandex_v101.Models.Langs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

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

    public List<String> getResponseDirs(AllLanguagesResponse response){
        List<String> dirs_list = new ArrayList<>();
        dirs_list = response.getDirs();
        return dirs_list;
    }

    public List<String> getResponseKeys(AllLanguagesResponse response){
        List<String> keys_list = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(response);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject langsObj = jsonObject.getJSONObject("langs");
            Iterator<String> iterator = langsObj.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                keys_list.add(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return keys_list;
    }

    public List<String> getResponseValues(AllLanguagesResponse response){
        List<String> values_list = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(response);
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject langsObj = jsonObject.getJSONObject("langs");
            Iterator<String> iterator = langsObj.keys();
            while (iterator.hasNext()){
                String key = iterator.next();
                String value = langsObj.get(key).toString();
                values_list.add(value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return values_list;
    }
}
