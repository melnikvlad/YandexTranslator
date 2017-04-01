package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.First;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Languages;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Translator_getLangsResponse.getLangsResponse;
import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.getLangsAdapter;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectTranslateLanguageFragment extends Fragment{
    private SearchView searchView;
    private RecyclerView rv;
    private getLangsAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private DataBaseSQLite db;
    private SharedPreferences sharedPreferences;
    private Button back_to_settings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.languages_fragment, container, false);
        back_to_settings    = (Button)view.findViewById(R.id.back_to_settings_from_directions);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler_view_setings);
        searchView          = (SearchView)view.findViewById(R.id.serchview_settings);
        manager             = new LinearLayoutManager(getActivity());
        db                  = new DataBaseSQLite(getActivity().getApplicationContext());

        if(db.getLanguagesCount() == 0){
            back_to_settings.setVisibility(View.GONE);
        }

        getLanguages();

        setupSearchView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        back_to_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToTranslateFragment();
            }
        });
        return view;
    }

    public void getLanguages() {
        sharedPreferences = getPreferences();
        if((db.getLanguagesCount()== 0)){ // if LanguageSQLite DB is empty --> load data from server --> insert it in SQLite -->view DB in RV

            Retrofit retrofitLNG = new Retrofit.Builder().baseUrl(Constants.BASE_URL)  //  Translate
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LanguagesService lang_service = retrofitLNG.create(LanguagesService.class); // Translate service
            final Call<getLangsResponse> CallToLanguages = lang_service.getLangs(getLanguagesParams());

            CallToLanguages.enqueue(new Callback<getLangsResponse>() {
                @Override
                public void onResponse(Call<getLangsResponse> call, Response<getLangsResponse> response) {

                    getLangsResponse serverResponse = response.body();
                    Languages languages = new Languages(
                            serverResponse.getResponseKeys(serverResponse),
                            serverResponse.getResponseValues(serverResponse)
                    );
                    db.insertLanguages(languages);

                    rv.setLayoutManager(manager); // View in Recycler View
                    adapter = new getLangsAdapter(getActivity(),db.getKeysAndValuesFromLanguagesTable().getValues());
                    rv.setAdapter(adapter);
                }
                @Override
                public void onFailure(Call<getLangsResponse> call, Throwable t) {
                }
            });
        }
        else { // if DataBaseSQLite DB is already exists and got all info --> view it in RV immediately
            rv.setLayoutManager(manager); // View in Recycler View
            adapter = new getLangsAdapter(getActivity(),db.getKeysAndValuesFromLanguagesTable().getValues());
            rv.setAdapter(adapter);
        }
    }

    public void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Поиск");
    }

    private Map<String, String> getLanguagesParams(){ // Params for Translate retrofit request
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_UI,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("ui", ui);
        return params;
    }

    private void goToTranslateFragment(){
        TranslateFragment fragment = new TranslateFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.languages_frame,fragment);
        ft.commit();
    }

    private static Context getActivityContex(){
        Context applicationContext = MainActivity.getContextOfApplication();
        return applicationContext;
    }

    private static SharedPreferences getPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivityContex());
        return prefs;
    }
}