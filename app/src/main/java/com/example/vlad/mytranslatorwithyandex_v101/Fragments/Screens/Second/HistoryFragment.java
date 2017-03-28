package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second;

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

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.HistoryAdapter;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.getDirsAdapter;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.getLangsAdapter;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView rv;
    private  HistoryAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private DataBaseSQLite db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_fragment, container, false);
        searchView          = (SearchView)view.findViewById(R.id.serchview_settings);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler);
        manager             = new LinearLayoutManager(getActivity());
        db                  = new DataBaseSQLite(getActivity().getApplicationContext());


        Log.d(Constants.TAG,"DATA : "+db.getHistoryWords()+db.getHistoryTranslates()+db.getHistoryDirs());

        rv.setLayoutManager(manager); // View in Recycler View
        adapter = new HistoryAdapter(getActivity(),db.getHistoryWords(),db.getHistoryTranslates(),db.getHistoryDirs());
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

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

        return view;
    }

    public void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("Найти в истории");
    }
}
