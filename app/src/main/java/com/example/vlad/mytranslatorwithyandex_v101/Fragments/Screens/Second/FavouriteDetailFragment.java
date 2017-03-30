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
import android.widget.Button;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Third.SettingsFragment;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.R;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.FavouriteAdapter;
import com.example.vlad.mytranslatorwithyandex_v101.RV_adapters.LookupAdapter;

public class FavouriteDetailFragment  extends Fragment{
    private TextView trans,def,pos;
    private Button back_to_favourite;
    private RecyclerView rv;
    private LookupAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private DataBaseSQLite db;
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourite_detail_fragment, container, false);
        trans               = (TextView)view.findViewById(R.id.translate);
        def                 = (TextView)view.findViewById(R.id.def);
        pos                 = (TextView)view.findViewById(R.id.pos);
        back_to_favourite   = (Button) view.findViewById(R.id.back_to_favourite_from_details);
        rv                  = (RecyclerView)view.findViewById(R.id.recycler_view);
        manager             = new LinearLayoutManager(getActivity());
        db                  = new DataBaseSQLite(getActivity().getApplicationContext());
        sharedPreferences   = getPreferences();
        db = new DataBaseSQLite(getActivityContex());

        trans.setText(db.getFavouriteTranslate(sharedPreferences.getString(Constants.LAST_FAVOURITE,"")));
        def.setText(sharedPreferences.getString(Constants.LAST_FAVOURITE,""));
        pos.setText(db.getFavouritePos(sharedPreferences.getString(Constants.LAST_FAVOURITE,"")));

        rv.setLayoutManager(manager); // View in Recycler View
        adapter = new LookupAdapter(
                getActivity(),
                db.getFavouriteTop_Row(sharedPreferences.getString(Constants.LAST_FAVOURITE,"")),
                db.getFavouriteBot_Row(sharedPreferences.getString(Constants.LAST_FAVOURITE,""))
        );
        adapter.notifyDataSetChanged();
        rv.setAdapter(adapter);

        back_to_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFavoirute();
            }
        });

        return view;
    }
    private Context getActivityContex(){
        Context applicationContext = MainActivity.getContextOfApplication();
        return applicationContext;
    }

    private SharedPreferences getPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivityContex());
        return prefs;
    }
    private void goToFavoirute(){
        FavouriteFragment fragment = new FavouriteFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.favourite_detail_fragment_frame,fragment);
        ft.commit();
    }

}
