package com.example.vlad.mytranslatorwithyandex_v101.RV_adapters;


import android.app.Activity;
import android.app.Application;
import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.R;

import java.util.ArrayList;
import java.util.List;

public class getLangsAdapter extends RecyclerView.Adapter<getLangsAdapter.LanguagesViewHolder> {
    private Context mContext;
    private List<String> langData;
    private List<String> filterList;

    public getLangsAdapter() {}

    public getLangsAdapter(Context mContext, List<String> langData) {
        this.mContext = mContext;
        this.langData = langData;
        this.filterList = new ArrayList<>();
        // we copy the original list to the filter list and use it for setting row values
        this.filterList.addAll(this.langData);
    }

    @Override
    public getLangsAdapter.LanguagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_settings, parent, false);
        getLangsAdapter.LanguagesViewHolder viewHolder = new getLangsAdapter.LanguagesViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(getLangsAdapter.LanguagesViewHolder holder, int position) {

        holder.lang.setText(filterList.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != filterList ? filterList.size() : 0);
    }

    public static class LanguagesViewHolder extends RecyclerView.ViewHolder{
        public TextView lang;

        public LanguagesViewHolder(View view) {
            super(view);
            lang = (TextView) view.findViewById(R.id.lang);
        }
    }

    // Do Search...
    public void filter(final String text) {
        filterList.clear();
                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    filterList.addAll(langData);
                }
                else {
                    // Iterate in the original List and add it to filter list...
                    for (int i=0;i<langData.size();i++) {
                        if (langData.get(i).toLowerCase().contains(text.toLowerCase()) ||
                                langData.get(i).toLowerCase().contains(text.toLowerCase())) {
                            // Adding Matched items
                            filterList.add(langData.get(i));
                        }
                    }
                }
        notifyDataSetChanged();
    }
}
