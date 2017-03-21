package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.AllLanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Langs.AllLanguagesResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Langs.Languages;
import com.example.vlad.mytranslatorwithyandex_v101.R;

import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.getLangsAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    List<String> keys;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        rv = (RecyclerView)view.findViewById(R.id.recycler_view_setings);
        searchView = (SearchView)view.findViewById(R.id.serchview_settings);
        manager = new LinearLayoutManager(getActivity());

        getLanguages();

        return view;
    }

    private void getLanguages() {
       keys = new ArrayList<>();
        Retrofit retrofitLNG = new Retrofit.Builder().baseUrl(Constants.BASE_URL)  //  Translate
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllLanguagesService lang_service = retrofitLNG.create(AllLanguagesService.class); // Translate service

        final Call<AllLanguagesResponse> CallToLanguages = lang_service.makeAllLanguagesRequest(getLanguagesParams());

        CallToLanguages.enqueue(new Callback<AllLanguagesResponse>() {
            @Override
            public void onResponse(Call<AllLanguagesResponse> call, Response<AllLanguagesResponse> response) {
                AllLanguagesResponse languges_response = response.body();

               Languages languages = new Languages(getKeys(languges_response),getValues(languges_response));

                rv.setLayoutManager(manager); // View in Recycler View
                adapter = new getLangsAdapter(
                        getActivity().getApplication(),languages.getValues());

                rv.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<AllLanguagesResponse> call, Throwable t) {

            }
        });
    }

    private String getKeybyValueID(List<String> keys,int value_id){
        return keys.get(value_id).toString();
    }
        private List<String> getKeys(AllLanguagesResponse response){
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

        private List<String> getValues(AllLanguagesResponse response){
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

        private Map<String, String> getLanguagesParams(){ // Params for Translate retrofit request
            Map<String, String> params = new HashMap<>();
            params.put("key", Constants.API_KEY_TRANSLATE);
            params.put("ui", Constants.UI);
            return params;
        }
    }

