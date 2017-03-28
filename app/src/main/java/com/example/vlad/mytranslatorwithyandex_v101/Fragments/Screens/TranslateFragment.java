package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.AllLanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LookupService;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.TranslateService;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.ServerResponse.getLangsResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Languages;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.LookupResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.TranslaterResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.LookupAdapter;


public class TranslateFragment extends Fragment implements View.OnClickListener {
    private TextView trans,def,pos;
    private EditText input_field;
    private Button translate,btn_translate_from,btn_switch_language,btn_translate_to;
    private String text ="" ;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private SharedPreferences sharedPreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = getPreferences();
        View view = inflater.inflate(R.layout.translate_fragment, container, false);
        btn_translate_from  = (Button)view.findViewById(R.id.translate_from);
        btn_switch_language = (Button)view.findViewById(R.id.switch_language);
        btn_translate_to    = (Button)view.findViewById(R.id.translate_to);
        translate           = (Button)view.findViewById(R.id.btn_tanslate);
        trans               = (TextView)view.findViewById(R.id.translate);
        def                 = (TextView)view.findViewById(R.id.def);
        pos                 = (TextView)view.findViewById(R.id.pos);
        input_field         = (EditText)view.findViewById(R.id.inputfield);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler_view);
        manager             = new LinearLayoutManager(getActivity());

            getLanguages();

        btn_translate_from.setOnClickListener(this);
        btn_switch_language.setOnClickListener(this);
        btn_translate_to.setOnClickListener(this);

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = input_field.getText().toString();
                Translate(text);  // DO ALL STUFF
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        DataBaseSQLite db = new DataBaseSQLite(getActivityContex());
        switch (view.getId()){
            case R.id.translate_from:
                goToLanguages(R.id.translate_from);
                break;
            case R.id.translate_to:
                goToLanguages(R.id.translate_to);
                break;
            case R.id.switch_language:
                swapLanguages();
                btn_translate_from.setText(db.getValueByKey(sharedPreferences.getString(Constants.TRANSLATE_FROM,"")));
                btn_translate_to.setText(db.getValueByKey(sharedPreferences.getString(Constants.TRANSLATE_TO,"")));
                break;

        }
    }
//====================================================== MAIN TRANSLATE AND LOOKUP METHOD ==================================================================================
    private void Translate(String text) {
        final DataBaseSQLite db = new DataBaseSQLite(getActivityContex());

        Retrofit retrofitTR = new Retrofit.Builder().baseUrl(Constants.BASE_URL)  //  Translate
               .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitLK = new Retrofit.Builder().baseUrl(Constants.BASE_URL2)   // Lookup
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TranslateService tr_service = retrofitTR.create(TranslateService.class); // Translate sevice
        LookupService lookup_service = retrofitLK.create(LookupService.class); //   Lookup sevice

        final Call<TranslaterResponse> CallToTranslate = tr_service.makeTranslateRequest(getTranslateParams());
        final Call<LookupResponse> CallToLookup = lookup_service.makeLookupRequest(getLookupParams());

        CallToTranslate.enqueue(new Callback<TranslaterResponse>() {   // Translate call
            @Override
            public void onResponse(Call<TranslaterResponse> call, Response<TranslaterResponse> response) {
                if (db.checkForTranslateDirectionsExists(LanguageQuery())) {
                    TranslaterResponse translate_response = response.body(); // Translate response
                    // Lets cut square braces off and view normal text
                    trans.setText(translate_response.getText().toString().substring(1, translate_response.getText().toString().length() - 1));
                }
                else {
                    Toast.makeText(getActivity(), "Wrong Translate Direction in translate", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<TranslaterResponse> call, Throwable t) {
            }
        });

        CallToLookup.enqueue(new Callback<LookupResponse>() { // Lookup call
            @Override
            public void onResponse(Call<LookupResponse> call, Response<LookupResponse> response) {

                LookupResponse lookup_response = response.body();  // Lookup response

                if(db.checkForTranslateDirectionsExists(LanguageQuery())){ //if Lookup method cant find any sugestions to request text,then need to hide fields to  error messages
                    rv.setVisibility(View.VISIBLE);
                    def.setVisibility(View.VISIBLE);
                    pos.setVisibility(View.VISIBLE);
                    def.setText(lookup_response.getDef().get(0).getText());
                    pos.setText(lookup_response.getDef().get(0).getPos());
                    rv.setLayoutManager(manager); // View in Recycler View
                    adapter = new LookupAdapter(
                            getActivity().getApplication(),
                            lookup_response.RV_top_items_row(),
                            lookup_response.RV_bot_items_row());
                    rv.setAdapter(adapter);
                    //________________________
                    // Definition + Synonyms  |
                    // Meaning                | <-- Single Recycler View Item
                    //________________________|
                }
                else {
                    rv.setVisibility(View.GONE);
                    def.setVisibility(View.GONE);
                    pos.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Wrong Translate Direction in Lookup", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<LookupResponse> call, Throwable t) {
            }
        });
    }

    private void getLanguages() {
        final DataBaseSQLite db = new DataBaseSQLite(getActivityContex());
        sharedPreferences = getPreferences();
        if (db.getLanguagesCount() == 0){
            Retrofit retrofitLNG = new Retrofit.Builder().baseUrl(Constants.BASE_URL)  //  Translate
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AllLanguagesService lang_service = retrofitLNG.create(AllLanguagesService.class); // Translate service
            final Call<getLangsResponse> CallToLanguages = lang_service.makeAllLanguagesRequest(getLanguagesParams());

            CallToLanguages.enqueue(new Callback<getLangsResponse>() {
                @Override
                public void onResponse(Call<getLangsResponse> call, Response<getLangsResponse> response) {
                    getLangsResponse languges_response = response.body();
                    Languages languages = new Languages(
                            languges_response.getResponseKeys(languges_response),
                            languges_response.getResponseValues(languges_response)
                    );
                    db.insertLanguages(languages);

                    btn_translate_from.setText(db.getValueByKey(sharedPreferences.getString(Constants.TRANSLATE_FROM,"")));
                    btn_translate_to.setText(db.getValueByKey(sharedPreferences.getString(Constants.TRANSLATE_TO,"")));
                }
                @Override
                public void onFailure(Call<getLangsResponse> call, Throwable t) {}
            });
        }
        else {
            btn_translate_from.setText(db.getValueByKey(sharedPreferences.getString(Constants.TRANSLATE_FROM,"")));
            btn_translate_to.setText(db.getValueByKey(sharedPreferences.getString(Constants.TRANSLATE_TO,"")));
        }
    }
//=========================================================================================================================================================

    private String LanguageQuery(){
        String from =sharedPreferences.getString(Constants.TRANSLATE_FROM,"");
        String to = sharedPreferences.getString(Constants.TRANSLATE_TO,"");
        return from+"-"+to;
    }

    private Map<String,String> getTranslateParams() { // Params for Translate retrofit request
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("lang", LanguageQuery());
        params.put("text", text);
        return params;
    }

    private Map<String,String> getLookupParams() {  // Params for Lookup retrofit request
        SharedPreferences sharedPreferences = getPreferences();
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_LOOKUP);
        params.put("lang",LanguageQuery());
        params.put("ui", ui);
        params.put("text", text);
        return params;
    }

    private Map<String, String> getLanguagesParams(){ // Params for Translate retrofit request
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("ui", ui);
        return params;
    }

    private void goToLanguages(int id){
        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        if(id == R.id.translate_from){
            editor.putInt(Constants.BTN_CLICKED,1);
            editor.apply();
        }
        if(id == R.id.translate_to) {
            editor.putInt(Constants.BTN_CLICKED,2);
            editor.apply();
        }
        LanguagesFragment fragment = new LanguagesFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.translate_fragment_frame,fragment);
        ft.commit();
    }

    private void  swapLanguages(){
        SharedPreferences prefs = getPreferences();
        SharedPreferences.Editor editor = prefs.edit();
        String temp ="";
        String to ="";

        temp = prefs.getString(Constants.TRANSLATE_FROM,"");
        to = prefs.getString(Constants.TRANSLATE_TO,"");
        editor.putString(Constants.TRANSLATE_FROM,to);
        editor.putString(Constants.TRANSLATE_TO,temp);
        editor.apply();
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