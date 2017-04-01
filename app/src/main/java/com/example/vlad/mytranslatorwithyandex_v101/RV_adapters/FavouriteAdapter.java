package com.example.vlad.mytranslatorwithyandex_v101.RV_adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.MainScreen;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second.FavouriteDetailFragment;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.Models.Translate.FavouriteDetail;
import com.example.vlad.mytranslatorwithyandex_v101.R;

import java.util.ArrayList;
import java.util.List;



public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    private Context mContext;
    private List<String> wordsData;
    private List<String> translatesData;
    private List<String> dirsData;
    private List<String> filterList;

    public FavouriteAdapter(Context mContext, List<String> wordsData, List<String> translatesData, List<String> dirsData) {
        this.mContext = mContext;
        this.wordsData = wordsData;
        this.translatesData = translatesData;
        this.dirsData = dirsData;
        this.filterList = new ArrayList<>();
        this.filterList.addAll(this.wordsData);

    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {
        private TextView lang,word,translate;
        private CardView cardView;
        public FavouriteViewHolder(View view) {
            super(view);
            lang = (TextView) view.findViewById(R.id.num);
            word = (TextView) view.findViewById(R.id.top);
            translate = (TextView) view.findViewById(R.id.bot);
            cardView = (CardView)view.findViewById(R.id.card_view);
        }
    }
    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_for_history, parent, false);
        FavouriteViewHolder viewHolder = new FavouriteViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, int position) {
        holder.word.setText(filterList.get(position));
        holder.translate.setText(translatesData.get(position));
        holder.lang.setText(dirsData.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getPreferences();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Constants.LAST_FAVOURITE,holder.word.getText().toString());
                editor.apply();
                goToFavouriteDetailFragment();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    // Do Search...
    public void filter(final String text) {
        filterList.clear();
        // If there is no search value, then add all original list items to filter list
        if (TextUtils.isEmpty(text)) {
            filterList.addAll(wordsData);
        }
        else {
            // Iterate in the original List and add it to filter list...
            for (int i=0;i<wordsData.size();i++) {
                if (wordsData.get(i).toLowerCase().contains(text.toLowerCase()) ||
                        wordsData.get(i).toLowerCase().contains(text.toLowerCase())) {
                    // Adding Matched items
                    filterList.add(wordsData.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    private Context getActivityContex(){
        Context applicationContext = MainActivity.getContextOfApplication();
        return applicationContext;
    }
    private SharedPreferences getPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivityContex());
        return prefs;
    }
    private  void goToFavouriteDetailFragment(){
        FavouriteDetailFragment favouriteDetail = new FavouriteDetailFragment();
        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.favourite_frame,favouriteDetail).commit();
        fragmentManager.beginTransaction().addToBackStack(null);
    }
}