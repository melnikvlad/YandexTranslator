package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Third;

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
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Directions;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Translator_getLangsResponse.getLangsResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Languages;
import com.example.vlad.mytranslatorwithyandex_v101.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SettingsFragment extends Fragment {
    TextView selected_lang;
    Button select_lang,view_list_of_dirs,getView_list_of_dirs2;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPreferences = getPreferences();
        View view = inflater.inflate(R.layout.settings_fragment,container,false);
        select_lang = (Button) view.findViewById(R.id.select_language);
        view_list_of_dirs = (Button) view.findViewById(R.id.view_list_of_dirs);
        getView_list_of_dirs2 = (Button)view.findViewById(R.id.view_list_of_dirs2);
        selected_lang = (TextView)view.findViewById(R.id.selected_lang);

        getLanguages();

        select_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLanguagesFragment();
            }
        });
        view_list_of_dirs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDirectionsFragment();
            }
        });
        getView_list_of_dirs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDirectionsFragment2();
            }
        });
        return view;
    }

    private void getLanguages() {
        final DataBaseSQLite db = new DataBaseSQLite(getActivityContex());
        sharedPreferences = getPreferences();

        if(db.getLanguagesCount() == 0){
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
                    Log.d(Constants.TAG,"SETTINGS LANG COUNT : "+ languages.getKeys().size());
                    Directions directions = new Directions(serverResponse.getResponseDirs(serverResponse));
                    Log.d(Constants.TAG,"DIRECTIONS DIRS COUNT : "+ directions.getDirs().size());
                    db.insertDirections(directions);

                    selected_lang.setText(db.getValueByKey(sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_INTERFACE,"")));
                }
                @Override
                public void onFailure(Call<getLangsResponse> call, Throwable t) {}
            });
        }
        else {
            selected_lang.setText(db.getValueByKey(sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_UI,"")));
        }
    }

    private Map<String, String> getLanguagesParams(){ // Params for Translate retrofit request
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_UI,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("ui", ui);
        return params;
    }

    private void goToLanguagesFragment(){

        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.BTN_CLICKED,4);
        editor.apply();
        LanguagesFragment fragment = new LanguagesFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.defaultlang_fragment_frame,fragment);
        ft.commit();
    }

    private void goToDirectionsFragment(){

        DirectionsFragment_Translator fragment = new DirectionsFragment_Translator();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.defaultlang_fragment_frame,fragment);
        ft.commit();
    }
    private void goToDirectionsFragment2(){

        DirectionsFragment_Dictionary fragment = new DirectionsFragment_Dictionary();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.defaultlang_fragment_frame,fragment);
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
