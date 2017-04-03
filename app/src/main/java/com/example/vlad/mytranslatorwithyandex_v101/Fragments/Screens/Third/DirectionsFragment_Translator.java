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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Interfaces.LanguagesService;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Directions;

import com.example.vlad.mytranslatorwithyandex_v101.Models.getLangs.Translator_getLangsResponse.getLangsResponse;
import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.getDirsAdapter;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
    View Translator.API supported directions in RecyclerView
 */
public class DirectionsFragment_Translator extends Fragment {
    private SearchView searchView;
    private RecyclerView rv;
    private getDirsAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private DataBaseSQLite db;
    private Button back_to_settings;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.directions_fragment,container,false);
        back_to_settings    = (Button)view.findViewById(R.id.back_to_settings_from_directions);
        searchView          = (SearchView)view.findViewById(R.id.serchview_settings);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler_view_setings);
        manager             = new LinearLayoutManager(getActivity());
        db                  = new DataBaseSQLite(getActivity().getApplicationContext());

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

        getDirections(); // Load all directions, supported by Translator.API with Retrofit

        return view;
    }

    private void getDirections() {
        if((db.getLanguagesCount()== 0)) {// if we hadn't load anything, when do it by Retrofit call, else load data in rv from SQLite DB
            Retrofit retrofitLNG = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            LanguagesService lang_service = retrofitLNG.create(LanguagesService.class);

            final Call<getLangsResponse> CallToLanguages = lang_service.getLangs(getParams());

            CallToLanguages.enqueue(new Callback<getLangsResponse>() {
                @Override
                public void onResponse(Call<getLangsResponse> call, Response<getLangsResponse> response) {
                    getLangsResponse serverResponse = response.body();
                    Directions directions = new Directions(
                        serverResponse.getResponseDirs(serverResponse)
                    );

                    db.insertDirections(directions);

                    rv.setLayoutManager(manager);
                    adapter = new getDirsAdapter(
                            getActivity(),
                            db.RewriteDirsToValuesInDirectionsTable(db.getDirectionsFromDirectionsTable().getDirs())
                            // get Directions keys from db,transform them and view their values,they are also in db
                    );
                    rv.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<getLangsResponse> call, Throwable t) {
                }
            });
        }
       else {
           rv.setLayoutManager(manager);
           adapter = new getDirsAdapter(
                   getActivity(),
                   db.RewriteDirsToValuesInDirectionsTable(db.getDirectionsFromDirectionsTable().getDirs())
                   // get Directions keys from db,transform them and view their values,they are also in db
           );
           rv.setAdapter(adapter);
       }
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Поиск напралений");
    }

    private Map<String, String> getParams(){ // Params for Translate retrofit request
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY_LOOKUP);
        return params;
    }

    private void goToSettingsFragment(){
        SettingsFragment fragment = new SettingsFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.directions_frame,fragment);
        ft.commit();
    }
}
