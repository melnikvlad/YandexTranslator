package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.First;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.DirectionsService;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LookupService;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.TranslateService;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.Lookup;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.Favourite;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.FavouriteDetail;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.History;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Directions;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Translator_getLangsResponse.getLangsResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Languages;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Lookup.LookupResponse;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.TranslaterResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.LookupAdapter;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.getDirsAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;


public class TranslateFragment extends Fragment implements View.OnClickListener {
    private TextView trans,def,pos;
    private EditText input_field;
    private Button translate,btn_translate_from,btn_switch_language,btn_translate_to,btn_add_to_favourite;
    private String text ="" ;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private SharedPreferences sharedPreferences;
    private DataBaseSQLite db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = new DataBaseSQLite(getActivityContex());
        sharedPreferences = getPreferences();
        View view = inflater.inflate(R.layout.translate_fragment, container, false);
        btn_translate_from  = (Button)view.findViewById(R.id.translate_from);
        btn_switch_language = (Button)view.findViewById(R.id.switch_language);
        btn_translate_to    = (Button)view.findViewById(R.id.translate_to);
        translate           = (Button)view.findViewById(R.id.btn_tanslate);
        btn_add_to_favourite= (Button)view.findViewById(R.id.add_to_favourite);
        trans               = (TextView)view.findViewById(R.id.translate);
        def                 = (TextView)view.findViewById(R.id.def);
        pos                 = (TextView)view.findViewById(R.id.pos);
        input_field         = (EditText)view.findViewById(R.id.inputfield);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler_view);
        manager             = new LinearLayoutManager(getActivity());
        btn_translate_from.setOnClickListener(this);
        btn_switch_language.setOnClickListener(this);
        btn_translate_to.setOnClickListener(this);


        trans.setText(db.getTransateFromHistoryTable(sharedPreferences.getString(Constants.LAST_QUERY,"")));
        def.setText(sharedPreferences.getString(Constants.LAST_QUERY,""));
        pos.setText(db.getPosFromLookupTable(sharedPreferences.getString(Constants.LAST_QUERY,"")));

        rv.setLayoutManager(manager);
        adapter = new LookupAdapter(
                getActivity(),
                db.getTop_Row_by_word(sharedPreferences.getString(Constants.LAST_QUERY,"")),
                db.getBot_Row_by_word(sharedPreferences.getString(Constants.LAST_QUERY,""))
        );
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

            getLanguages();
            getDirections();

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    text = input_field.getText().toString();
                    Translate(text);
            }
        });
        btn_add_to_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.ExistInFavouriteTable(sharedPreferences.getString(Constants.LAST_QUERY,""),LanguageQuery()) == 0) {
                Favourite favourite = new Favourite(
                        sharedPreferences.getString(Constants.LAST_QUERY,""),
                        db.getTransateFromHistoryTable(sharedPreferences.getString(Constants.LAST_QUERY,"")),
                        LanguageQuery()
                );
                db.insertFavourite(favourite);

                FavouriteDetail favourite_detail = new FavouriteDetail(
                        sharedPreferences.getString(Constants.LAST_QUERY,""),
                        db.getTransateFromHistoryTable(sharedPreferences.getString(Constants.LAST_QUERY,"")),
                        db.getPosFromLookupTable(sharedPreferences.getString(Constants.LAST_QUERY,"")),
                        db.getTop_Row_by_word(sharedPreferences.getString(Constants.LAST_QUERY,"")),
                        db.getBot_Row_by_word(sharedPreferences.getString(Constants.LAST_QUERY,"")),
                        LanguageQuery()
                );
                db.insertFavouriteDetail(favourite_detail);
                Toast.makeText(getActivityContex(), "Added to favourite :)", Toast.LENGTH_LONG).show();
            }
            else {
                    Toast.makeText(getActivity(), "Already in favourite :)", Toast.LENGTH_LONG).show();
                }
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
        Retrofit retrofitTR = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)  //  Translate
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofitLK = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL2)   // Lookup
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TranslateService tr_service = retrofitTR.create(TranslateService.class); // Translate sevice
        LookupService lookup_service = retrofitLK.create(LookupService.class); //   Lookup sevice

        final Call<TranslaterResponse> CallToTranslate = tr_service.makeTranslateRequest(getTranslateParams());
        final Call<LookupResponse> CallToLookup = lookup_service.makeLookupRequest(getLookupParams());

        CallToLookup.enqueue(new Callback<LookupResponse>() { // Lookup call
            @Override
            public void onResponse(Call<LookupResponse> call, Response<LookupResponse> response) {
                LookupResponse lookup_response = response.body();  // Lookup response
                if (db.TranslateDirectionExistInDictionaryDirectionsTable(LanguageQuery())) {
                        rv.setVisibility(View.VISIBLE);
                        def.setVisibility(View.VISIBLE);
                        pos.setVisibility(View.VISIBLE);
                        def.setText(lookup_response.getDef().get(0).getText());
                        pos.setText(lookup_response.getDef().get(0).getPos());
                        rv.setLayoutManager(manager); // View in Recycler View
                        adapter = new LookupAdapter(
                                getActivity(),
                                lookup_response.RV_top_items_row(),
                                lookup_response.RV_bot_items_row());
                        rv.setAdapter(adapter);
                        //________________________
                        // Definition + Synonyms  |
                        // Meaning                | <-- Single Recycler View Item
                        //________________________|
                        if (db.ExistInHistoryTable(lookup_response.getDef().get(0).getText(), LanguageQuery()) == 1) {
                            Lookup lookup = new Lookup(
                                    lookup_response.getDef().get(0).getText(),
                                    lookup_response.getDef().get(0).getPos(),
                                    lookup_response.RV_top_items_row(),
                                    lookup_response.RV_bot_items_row()
                            );
                            db.insertLookup(lookup);
                        }
                    }
                else {
                    rv.setVisibility(View.INVISIBLE);
                    def.setVisibility(View.INVISIBLE);
                    pos.setVisibility(View.INVISIBLE);
                    Toast.makeText(getActivity(), "Wrong Translate Direction in Lookup", Toast.LENGTH_LONG).show();
                    }
                }
            @Override
            public void onFailure(Call<LookupResponse> call, Throwable t) {
            }
        });

        CallToTranslate.enqueue(new Callback<TranslaterResponse>() {   // Translate call
            @Override
            public void onResponse(Call<TranslaterResponse> call, Response<TranslaterResponse> response) {
                if (db.TranslateDirectionExistInDirectionsTable(LanguageQuery())) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.LAST_QUERY, input_field.getText().toString());
                    editor.apply();

                    TranslaterResponse translate_response = response.body(); // Translate response

                        History history = new History(
                                sharedPreferences.getString(Constants.LAST_QUERY,""),
                                translate_response.getText().get(0),
                                translate_response.getLang()
                        );
                        db.insertHistory(history);
                        trans.setVisibility(View.VISIBLE);
                        trans.setText(translate_response.getText().toString().substring(1, translate_response.getText().toString().length()-1));
//                        trans.setText("Sorry! We can't translate this text :(");
//                        Toast.makeText(getActivity(), "Sorry! We can't translate this text :(", Toast.LENGTH_LONG).show();
                }
                else {
                    trans.setText("Error:( Please check for existing this translate direction !");
                    Toast.makeText(getActivity(), "Wrong Translate Direction in translate", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<TranslaterResponse> call, Throwable t) {
            }
        });
    }

    private void getLanguages() {
        final DataBaseSQLite db = new DataBaseSQLite(getActivityContex());
        sharedPreferences = getPreferences();
        if (db.getLanguagesCount() == 0 ){
            Retrofit retrofitLNG = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)  //  Translate
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
                    Directions directions = new Directions(serverResponse.getResponseDirs(serverResponse));

                    db.insertLanguages(languages);
                    db.insertDirections(directions);

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

    private void getDirections() {
        sharedPreferences = getPreferences();
        if((db.getDictoinaryDirectionsCount()== 0)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL2)  //  Translate
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            DirectionsService service = retrofit.create(DirectionsService.class); // Translate service
            final Call<List<String>> CallForDirs = service.getDirs(getParams());
            CallForDirs.enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    List<String> serverResponse = response.body();
                    Directions directions = new Directions(
                            ArrayToList(serverResponse)
                    );
                    db.insertDictionaryDirections(directions);
                }
                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                }
            });
        }
    }
//=========================================================================================================================================================

    private String LanguageQuery(){
        String from = sharedPreferences.getString(Constants.TRANSLATE_FROM,"");
        String to = sharedPreferences.getString(Constants.TRANSLATE_TO,"");
        return from+"-"+to;
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

    private Map<String,String> getTranslateParams() { // Params for Translate retrofit request
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("lang", LanguageQuery());
        params.put("text", text);
        return params;
    }

    private Map<String,String> getLookupParams() {  // Params for Lookup retrofit request
        SharedPreferences sharedPreferences = getPreferences();
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_UI,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_LOOKUP);
        params.put("lang",LanguageQuery());
        params.put("ui", ui);
        params.put("text", text);
        return params;
    }

    private Map<String, String> getLanguagesParams(){
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_UI,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_TRANSLATE);
        params.put("ui", ui);
        return params;
    }
    private Map<String, String> getParams(){
        String ui = sharedPreferences.getString(Constants.DEFAULT_LANGUAGE_UI,"");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_LOOKUP);
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
        SelectTranslateLanguageFragment fragment = new SelectTranslateLanguageFragment();
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