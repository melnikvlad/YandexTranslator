package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.LanguagesSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.AllLanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Langs.AllLanguagesResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Langs.Languages;
import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DefaultLanguageFragment extends Fragment {
    TextView selected_lang;
    Button select_lang;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPreferences = getPreferences();
        View view = inflater.inflate(R.layout.default_lang_fragment,container,false);
        select_lang = (Button) view.findViewById(R.id.select_language);
        selected_lang = (TextView)view.findViewById(R.id.selected_lang);

        getLanguages();

        select_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettingsFragment();
            }
        });
        return view;
    }

    private void getLanguages() {
        final LanguagesSQLite db = new LanguagesSQLite(getActivityContex());
        sharedPreferences = getPreferences();
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        Retrofit retrofitLNG = new Retrofit.Builder().baseUrl(Constants.BASE_URL)  //  Translate
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllLanguagesService lang_service = retrofitLNG.create(AllLanguagesService.class); // Translate service

        final Call<AllLanguagesResponse> CallToLanguages = lang_service.makeAllLanguagesRequest(getLanguagesParams());

        CallToLanguages.enqueue(new Callback<AllLanguagesResponse>() {
            @Override
            public void onResponse(Call<AllLanguagesResponse> call, Response<AllLanguagesResponse> response) {
                AllLanguagesResponse languges_response = response.body();

                Languages languages = new Languages(getResponseKeys(languges_response),getResponseValues(languges_response));
                if(db.getLanguagesCount() == 0) {
                    db.insertData(languages);
                }

                selected_lang.setText(db.getValueByKey(sharedPreferences.getString(Constants.DEFAULT_LANGUAGE,"")));

                editor.putInt(Constants.REFRESH,1);
                editor.apply();
            }
            @Override
            public void onFailure(Call<AllLanguagesResponse> call, Throwable t) {}
        });
    }

    private Map<String, String> getLanguagesParams(){ // Params for Translate retrofit request
        String ui;
        LanguagesSQLite db = new LanguagesSQLite(getActivity().getApplicationContext());
        ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE,"");

        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("ui", ui);
        return params;
    }
    private List<String> getResponseKeys(AllLanguagesResponse response){
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

    private List<String> getResponseValues(AllLanguagesResponse response){
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


    private void goToSettingsFragment(){

        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.BTN_CLICKED,4);
        editor.apply();
        SettingsFragment fragment = new SettingsFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }
    private Context getActivityContex(){
        Context applicationContext = MainActivity.getContextOfApplication();
        return applicationContext;
    }
    private SharedPreferences getPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivityContex());
        return prefs;
    }

}
