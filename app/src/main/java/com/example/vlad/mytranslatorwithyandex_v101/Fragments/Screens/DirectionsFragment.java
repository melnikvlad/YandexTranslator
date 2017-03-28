package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.LanguagesSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.AllLanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Langs.AllLanguagesResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Langs.Languages;
import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.getDirsAdapter;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectionsFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView rv;
    private getDirsAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private LanguagesSQLite db;
    private SharedPreferences sharedPreferences;
    private Button back_to_settings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.directions_fragment,container,false);
        back_to_settings    = (Button)view.findViewById(R.id.back_to_settings_from_directions);
        searchView          = (SearchView)view.findViewById(R.id.serchview_settings);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler_view_setings);
        manager             = new LinearLayoutManager(getActivity());
        db                  = new LanguagesSQLite(getActivity().getApplicationContext());

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
                goToSettingsFragment();
            }
        });
        return view;
    }

    private void getLanguages() {
        sharedPreferences = getPreferences();
        if((db.getLanguagesCount()== 0)){ // if LanguageSQLite DB is empty --> load data from server --> insert it in SQLite -->view DB in RV

            Retrofit retrofitLNG = new Retrofit.Builder().baseUrl(Constants.BASE_URL)  //  Translate
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllLanguagesService lang_service = retrofitLNG.create(AllLanguagesService.class); // Translate service
            final Call<AllLanguagesResponse> CallToLanguages = lang_service.makeAllLanguagesRequest(getLanguagesParams());

            CallToLanguages.enqueue(new Callback<AllLanguagesResponse>() {
                @Override
                public void onResponse(Call<AllLanguagesResponse> call, Response<AllLanguagesResponse> response) {
                    AllLanguagesResponse languges_response = response.body();
                    Languages languages = new Languages(
                            languges_response.getResponseDirs(languges_response),
                            languges_response.getResponseKeys(languges_response),
                            languges_response.getResponseValues(languges_response)
                    );
                    db.insertData(languages);

                    rv.setLayoutManager(manager); // View in Recycler View
                    adapter = new getDirsAdapter(getActivity(),db.RewriteDirsInValues(db.getAllData().getDirs()));
                    rv.setAdapter(adapter);
                }
                @Override
                public void onFailure(Call<AllLanguagesResponse> call, Throwable t) {}
            });
        }
        else { // if LanguagesSQLite DB is already exists and got all info --> view it in RV immediately
            rv.setLayoutManager(manager); // View in Recycler View
            adapter = new getDirsAdapter(getActivity(),db.RewriteDirsInValues(db.getAllData().getDirs()));
            rv.setAdapter(adapter);
        }
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");
    }

    private Map<String, String> getLanguagesParams(){ // Params for Translate retrofit request
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("ui", ui);
        return params;
    }

    private void goToSettingsFragment(){
        SettingsFragment fragment = new SettingsFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.directions_frame,fragment);
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
