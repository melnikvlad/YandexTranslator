package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Third;
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
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.DirectionsService;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Directions;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Translator_getLangsResponse.getLangsResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Languages;
import com.example.vlad.mytranslatorwithyandex_v101.R;

import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.getLangsAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
We use yhis fragment in two ways:
1 - When our app loads first time and we should select default interface language
2 - When we want to change this language in Settings (3rd tab in viewpager)
*/
public class LanguagesFragment extends Fragment{
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
        db                  = new DataBaseSQLite(getActivityContex());

        if(db.getLanguagesCount() == 0){ // Check for SQLite data, if it not exist,so there is no way to go back on another screen
            back_to_settings.setVisibility(View.GONE);// and we should make back button invisiable
        }

        setupSearchView(); //Search View setup
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // send to adapter new, filtered data
                return true;
            }
        });

        back_to_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettingsFragment();
            }
        });

        getLanguages(); // Load all languages, supported by Translator.API with Retrofit
        getDirections();// Load all directions, supported by Dictionary.API with Retrofit

        return view;
    }
/*
    Get languages and directions supported by Translator.API
    it will response with lists of keys, values, directions.For ex.: en English and en-ru
    insert keys and values in "Languages" table
    insert directions in "Directions" table
 */
    public void getLanguages() {

       if((db.getLanguagesCount()== 0)){ // if we hadn't load anything, when do it by Retrofit call, else load data in rv from SQLite DB

           Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                   .addConverterFactory(GsonConverterFactory.create())
                   .build();

           LanguagesService lang_service = retrofit.create(LanguagesService.class);
           final Call<getLangsResponse> CallToLanguages = lang_service.getLangs(getLanguagesParams());

           CallToLanguages.enqueue(new Callback<getLangsResponse>() {
               @Override
               public void onResponse(Call<getLangsResponse> call, Response<getLangsResponse> response) {

                   getLangsResponse serverResponse = response.body();
                   Languages languages = new Languages(
                           serverResponse.getResponseKeys(serverResponse),
                           serverResponse.getResponseValues(serverResponse)
                   );
                   Directions directions = new Directions(serverResponse.getResponseDirs(serverResponse));

                   db.insertLanguages(languages);
                   db.insertDirections(directions);

                   rv.setLayoutManager(manager);
                   adapter = new getLangsAdapter(getActivity(),db.getKeysAndValuesFromLanguagesTable().getValues());
                   rv.setAdapter(adapter);
               }
               @Override
               public void onFailure(Call<getLangsResponse> call, Throwable t) {
               }
           });
       }
       else {
           rv.setLayoutManager(manager);
           adapter = new getLangsAdapter(getActivity(),db.getKeysAndValuesFromLanguagesTable().getValues());
           rv.setAdapter(adapter);
       }
    }
    /*
        Get directions supported by Dictionary.API
        Directions from Translator.API are different, so we want to save another dirs in new table "DirectionsDictionary"
     */
    public void getDirections() {// if we hadn't load directions, when do it by Retrofit call, else load data in rv from SQLite DB

        if ((db.getDictoinaryDirectionsCount() == 0)) {

            Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DirectionsService service = retrofit.create(DirectionsService.class);
            final Call<List<String>> CallForDirs = service.getDirs(getLanguagesParams());

            CallForDirs.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    List<String> serverResponse = response.body();

                    Directions directions = new Directions(
                            ArrayToList(serverResponse) // Transform server response array to List
                    );

                    db.insertDictionaryDirections(directions);
                }
                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                }
            });
        }
    }

    public void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Поиск");
    }

    public List<String> ArrayToList(List<String> response){
        List<String> list = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(response);

        try {
            JSONArray jsonArr = new JSONArray(json);
            for(int i = 0;i< jsonArr.length();i++){
                list.add(jsonArr.get(i).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Map<String, String> getLanguagesParams(){ // Params for Translate Retrofit request
            sharedPreferences = getPreferences();
            String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_UI,"");
            Map<String, String> params = new HashMap<>();
            params.put("key", Constants.API_KEY_TRANSLATE);
            params.put("ui", ui);
            return params;
    }
    private Map<String, String> getParams(){ // Params for Directions Retrofit request
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_UI,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_LOOKUP);
        params.put("ui", ui);
        return params;
    }

    private void goToSettingsFragment(){ // Replace fragment with Settings (3rd tab)
        SettingsFragment fragment = new SettingsFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.languages_frame,fragment);
        ft.commit();
    }

    private static Context getActivityContex(){
        Context applicationContext = MainActivity.getContextOfApplication();
        return applicationContext;
    }

    private static SharedPreferences getPreferences(){ // setup SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivityContex());
        return prefs;
    }
}

