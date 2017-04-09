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
    private List<String> filterWords;
    private List<String> filterTrans;
    private List<String> filterDirs;

    public FavouriteAdapter(Context mContext, List<String> wordsData, List<String> translatesData, List<String> dirsData) {
        this.mContext = mContext;
        this.wordsData = wordsData;
        this.translatesData = translatesData;
        this.dirsData = dirsData;
        this.filterWords = new ArrayList<>();
        this.filterTrans = new ArrayList<>();
        this.filterDirs = new ArrayList<>();
        this.filterWords.addAll(this.wordsData);
        this.filterTrans.addAll(this.translatesData);
        this.filterDirs.addAll(this.dirsData);

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
        holder.word.setText(filterWords.get(position));
        holder.translate.setText(filterTrans.get(position));
        holder.lang.setText(filterDirs.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getPreferences();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(Constants.LAST_FAVOURITE,holder.word.getText().toString());
                editor.putString(Constants.LAST_FAVOURITE_DIR,holder.lang.getText().toString());
                editor.apply();
                goToFavouriteDetailFragment();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != filterWords ? filterWords.size() : 0);
    }
    public void deleteItem(int position) {
        filterWords.remove(position);
        filterTrans.remove(position);
        filterDirs.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    public String getWord(int position){
        return filterWords.get(position);
    }
    public String getTrans(int position){
        return filterTrans.get(position);
    }
    public String getDirs(int position){
        return filterDirs.get(position);
    }

    // Do Search...
    public void filter(final String text) {
        filterWords.clear();
        filterTrans.clear();
        filterDirs.clear();
        // If there is no search value, then add all original list items to filter list
        if (TextUtils.isEmpty(text)) {
            filterWords.addAll(wordsData);
            filterTrans.addAll(translatesData);
            filterDirs.addAll(dirsData);
        }
        else {
            // Iterate in the original List and add it to filter list...
            for (int i=0;i<wordsData.size();i++) {
                if (wordsData.get(i).toLowerCase().contains(text.toLowerCase()) ||
                        wordsData.get(i).toLowerCase().contains(text.toLowerCase())) {
                    // Adding Matched items
                    filterWords.add(wordsData.get(i));
                    filterTrans.add(translatesData.get(i));
                    filterDirs.add(dirsData.get(i));
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
