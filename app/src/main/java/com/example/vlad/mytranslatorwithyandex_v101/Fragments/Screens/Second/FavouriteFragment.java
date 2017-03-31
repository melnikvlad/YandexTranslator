package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second;

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
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.FavouriteAdapter;

public class FavouriteFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView rv;
    private FavouriteAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private DataBaseSQLite db;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_fragment, container, false);
        searchView          = (SearchView)view.findViewById(R.id.serchview_settings);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler);
        manager             = new LinearLayoutManager(getActivity());
        db                  = new DataBaseSQLite(getActivity().getApplicationContext());

        rv.setLayoutManager(manager);
        adapter = new FavouriteAdapter(
                getActivity(),
                db.getWordsFromFavouriteTable(),
                db.getTranslatesFromFavouriteTable(),
                db.getDirsFromFavouriteTable()
        );
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
        searchView.setQueryHint("Найти в избранном");
    }
}
