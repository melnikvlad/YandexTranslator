package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.First;

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
import android.widget.LinearLayout;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
/*
    This is main fragment, where we do translate things
*/
public class TranslateFragment extends Fragment implements View.OnClickListener {
    private LinearLayout error_container;
    private TextView trans, def, pos, error_mesage;
    private EditText input_field;
    private Button translate, btn_translate_from, btn_switch_language, btn_translate_to, btn_add_to_favourite;
    private String text = "";
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private SharedPreferences sharedPreferences;
    private DataBaseSQLite db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.translate_fragment, container, false);
        error_container     = (LinearLayout) view.findViewById(R.id.error_container);
        btn_translate_from  = (Button) view.findViewById(R.id.translate_from);
        btn_switch_language = (Button) view.findViewById(R.id.switch_language);
        btn_translate_to    = (Button) view.findViewById(R.id.translate_to);
        translate           = (Button) view.findViewById(R.id.btn_tanslate);
        btn_add_to_favourite= (Button) view.findViewById(R.id.add_to_favourite);
        trans               = (TextView) view.findViewById(R.id.translate);
        def                 = (TextView) view.findViewById(R.id.def);
        pos                 = (TextView) view.findViewById(R.id.pos);
        error_mesage        = (TextView) view.findViewById(R.id.error_message);
        input_field         = (EditText) view.findViewById(R.id.inputfield);
        rv                  = (RecyclerView) view.findViewById(R.id.recycler_view);
        manager             = new LinearLayoutManager(getActivity());
        db                  = new DataBaseSQLite(getActivityContex());
        sharedPreferences   = getPreferences();

        btn_translate_from.setOnClickListener(this);
        btn_switch_language.setOnClickListener(this);
        btn_translate_to.setOnClickListener(this);
        /*
            Before start translate update languages and directions in the language of selected ui
        */
        getLanguages();
        getDirections();
        /*
            When the screen created we need to load data, which represent our last translate action
            So first of all, make invisible "add to favourite" button, cuz nothing to add at the beginning, while the space is out off info
            Then put in TextViews and RecyclerView data comparatively our last translate action
            In other words, view detail of last history item from SQLite
        */
        error_container.setVisibility(View.GONE);
        trans.setText(db.getTransateFromHistoryTable(sharedPreferences.getString(Constants.LAST_ACTION, "")));
        def.setText(sharedPreferences.getString(Constants.LAST_ACTION, ""));
        pos.setText(db.getPosFromLookupTable(sharedPreferences.getString(Constants.LAST_ACTION, "")));

        rv.setLayoutManager(manager);
        adapter = new LookupAdapter(
                getActivity(),
                db.getTop_Row_by_word(sharedPreferences.getString(Constants.LAST_ACTION, "")),
                db.getBot_Row_by_word(sharedPreferences.getString(Constants.LAST_ACTION, ""))
        );
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);
        /*
            if data loaded( if something is in history), we make "add button" visible and invisible if it not
        */
        if(adapter.getItemCount() != 0){
            btn_add_to_favourite.setVisibility(View.VISIBLE);
        }
        else{
            btn_add_to_favourite.setVisibility(View.INVISIBLE);
        }
        /*
              CLick and do to server translate request
        */
        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = input_field.getText().toString();
                Translate(text);
            }
        });
        /*
            "Add button" click --> if in Favourite table of SQLite DB not exist row with similar word and translate direction
            we can add this in Favourite table and add the details in FavouriteDetail table
            else we CAN'T
        */
        btn_add_to_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.ExistInFavouriteTable(sharedPreferences.getString(Constants.LAST_ACTION, ""), LanguageQuery()) == 0) {
                    Favourite favourite = new Favourite(
                            sharedPreferences.getString(Constants.LAST_ACTION, ""),
                            db.getTransateFromHistoryTable(sharedPreferences.getString(Constants.LAST_ACTION, "")),
                            LanguageQuery()
                    );
                    db.insertFavourite(favourite);

                    FavouriteDetail favourite_detail = new FavouriteDetail(
                            sharedPreferences.getString(Constants.LAST_ACTION, ""),
                            db.getTransateFromHistoryTable(sharedPreferences.getString(Constants.LAST_ACTION, "")),
                            db.getPosFromLookupTable(sharedPreferences.getString(Constants.LAST_ACTION, "")),
                            db.getTop_Row_by_word(sharedPreferences.getString(Constants.LAST_ACTION, "")),
                            db.getBot_Row_by_word(sharedPreferences.getString(Constants.LAST_ACTION, "")),
                            LanguageQuery()
                    );
                    db.insertFavouriteDetail(favourite_detail);
                    Toast.makeText(getActivityContex(), "Добавлено в избранное :)", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "Уже есть в избранном :)", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
    /*
        Change FROM_TO languages and swap them with each other
    */
    @Override
    public void onClick(View view) {
        DataBaseSQLite db = new DataBaseSQLite(getActivityContex());
        switch (view.getId()) {
            case R.id.translate_from:
                goToLanguages(R.id.translate_from);
                break;
            case R.id.translate_to:
                goToLanguages(R.id.translate_to);
                break;
            case R.id.switch_language:
                swapLanguages();
                btn_translate_from.setText(db.getValueByKey(sharedPreferences.getString(Constants.TRANSLATE_FROM, "")));
                btn_translate_to.setText(db.getValueByKey(sharedPreferences.getString(Constants.TRANSLATE_TO, "")));
                break;
        }
    }

    //====================================================== MAIN TRANSLATE AND LOOKUP METHOD ==================================================================================
    private void Translate(String text) {
        /*
            When we clicked on "translate button", need to set invisible "error container"
            Make Retrofit call to server
            Get Response data and check the response code
            if it 200 then view translate and lookup data
            else view error message and nothing more
        */
        error_container.setVisibility(View.GONE);
        db = new DataBaseSQLite(getActivityContex());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TranslateService tr_service = retrofit.create(TranslateService.class);
        final Call<TranslaterResponse> CallToTranslate = tr_service.makeTranslateRequest(getTranslateParams());

        CallToTranslate.enqueue(new Callback<TranslaterResponse>() {
            @Override
            public void onResponse(Call<TranslaterResponse> call, Response<TranslaterResponse> response) {
                TranslaterResponse translate_response = response.body();
                switch (translate_response.getCode()) {
                    case 401:
                        viewErrorMessage("Неправильный API-ключ");
                        break;
                    case 402:
                        viewErrorMessage("API-ключ заблокирован");
                        break;
                    case 404:
                        viewErrorMessage("Превышено суточное ограничение на объем переведенного текста");
                        break;
                    case 413:
                        viewErrorMessage("Превышен максимально допустимый размер текста");
                        break;
                    case 422:
                        viewErrorMessage("Текст не может быть переведен");
                        break;
                    case 200:
                        viewTranslate(translate_response);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<TranslaterResponse> call, Throwable t) {
            }
        });
    }

    private void viewTranslate(final TranslaterResponse translate_response) {
        /*
            As we know there is 2 supported lists of translate directions
            So if our direction exist in Directions table we will go on and view translate stuff
        */
        if (db.TranslateDirectionExistInDirectionsTable(LanguageQuery())) {
            /*
                set "add button" visible, cuz now we have things to add in favourite
                set trans visible,cuz we can make it invisible if there is error in translate and view only error container
                ser trans response translation and transform it,cuz we get it from server in square braces
            */
                btn_add_to_favourite.setVisibility(View.VISIBLE);
                trans.setVisibility(View.VISIBLE);
                trans.setText(translate_response.getText().toString().substring(1, translate_response.getText().toString().length() - 1));
                /*
                    Make Retrofit call for Lookup
                */
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL2)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LookupService lookup_service = retrofit.create(LookupService.class);
                final Call<LookupResponse> CallToLookup = lookup_service.makeLookupRequest(getLookupParams());

                CallToLookup.enqueue(new Callback<LookupResponse>() {
                    @Override
                    public void onResponse(Call<LookupResponse> call, Response<LookupResponse> response) {
                        LookupResponse lookup_response = response.body();
                        if (db.TranslateDirectionExistInDictionaryDirectionsTable(LanguageQuery())) {
                            /*
                                So if our direction exist in Dictionary Directions table we will go on and make Views visible
                            */
                            rv.setVisibility(View.VISIBLE);
                            def.setVisibility(View.VISIBLE);
                            pos.setVisibility(View.VISIBLE);
                            /*
                                But if there are some errors: typo or word doen't exist in dictionary, we can have empty response object
                                So to prevent app crashes -- > check for response object size nad if it is empty ---> view error and break process
                             */
                            if (lookup_response.getDef().size() == 0) {
                                rv.setVisibility(View.INVISIBLE);
                                def.setVisibility(View.INVISIBLE);
                                pos.setVisibility(View.INVISIBLE);
                                viewErrorMessage("Нет дополнительного перевода");
                                return;
                            }
                            /*
                                if everything with Lookup is OK --> update in Prefs LAST_ACTION word--->view in Recycler view
                            */
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.LAST_ACTION, input_field.getText().toString());
                            editor.apply();

                            def.setText(lookup_response.getDef().get(0).getText());
                            pos.setText(lookup_response.getDef().get(0).getPos());
                            rv.setLayoutManager(manager); // View in Recycler View
                            adapter = new LookupAdapter(
                                    getActivity(),
                                    lookup_response.RV_top_items_row(),
                                    lookup_response.RV_bot_items_row());
                            rv.setAdapter(adapter);
                            /*________________________
                             | Definition + Synonyms  |
                             | Meaning                | <-- Single Recycler View Item
                             |________________________|                                        */
                            /*
                                if Lookup response word + dir doesn't exist in History table,
                                when we can save this in Lookup table and add it in History table
                            */
                            if (db.ExistInHistoryTable(lookup_response.getDef().get(0).getText(), LanguageQuery()) == 0) {
                                Lookup lookup = new Lookup(
                                        lookup_response.getDef().get(0).getText(),
                                        lookup_response.getDef().get(0).getPos(),
                                        lookup_response.RV_top_items_row(),
                                        lookup_response.RV_bot_items_row()
                                );
                                History history = new History(
                                        sharedPreferences.getString(Constants.LAST_ACTION, ""),
                                        translate_response.getText().get(0),
                                        translate_response.getLang()
                                );
                                db.insertLookup(lookup,LanguageQuery());
                                db.insertHistory(history);
                            }
                        }
                        else {
                            viewErrorMessage("Заданное направление перевода не поддерживается Яндекс.Словарем");
                        }
                    }
                    @Override
                    public void onFailure(Call<LookupResponse> call, Throwable t) {
                        viewErrorMessage("Перевода не существует");
                    }
                });
        }
        else {
            viewErrorMessage("Заданное направление перевода не поддерживается Яндекс.Переводчиком");
        }
    }

    private void getLanguages() {
        final DataBaseSQLite db = new DataBaseSQLite(getActivityContex());
        sharedPreferences = getPreferences();
        if (db.getLanguagesCount() == 0 ){
            Retrofit retrofitLNG = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            LanguagesService lang_service = retrofitLNG.create(LanguagesService.class);
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
                    .baseUrl(Constants.BASE_URL2)
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
    private void viewErrorMessage(String text){ // view error container + word
        rv.setVisibility(View.INVISIBLE);
        //trans.setVisibility(View.INVISIBLE);
        pos.setVisibility(View.INVISIBLE);

        def.setText(input_field.getText().toString());
        btn_add_to_favourite.setVisibility(View.INVISIBLE);
        error_container.setVisibility(View.VISIBLE);
        error_mesage.setText(text);
    }
    private String LanguageQuery(){ // transform FROM-TO values to keys for retrofit request
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
            editor.putInt(Constants.ID_OF_ACTION,1);
            editor.apply();
        }
        if(id == R.id.translate_to) {
            editor.putInt(Constants.ID_OF_ACTION,2);
            editor.apply();
        }
        SelectTranslateLanguageFragment fragment = new SelectTranslateLanguageFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.translate_fragment_frame,fragment);
        ft.commit();
    }

    private void  swapLanguages(){ // swap FROM-TO
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