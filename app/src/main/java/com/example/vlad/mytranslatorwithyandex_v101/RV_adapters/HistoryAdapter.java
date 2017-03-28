package com.example.vlad.mytranslatorwithyandex_v101.RV_adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private Context mContext;
    private List<String> wordsData;
    private List<String> translatesData;
    private List<String> dirsData;
    private List<String> filterList;

    public HistoryAdapter(Context mContext, List<String> wordsData, List<String> translatesData, List<String> dirsData) {
        this.mContext = mContext;
        this.wordsData = wordsData;
        this.translatesData = translatesData;
        this.dirsData = dirsData;
        this.filterList = new ArrayList<>();
        this.filterList.addAll(this.wordsData);

    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView lang,word,translate;
        public HistoryViewHolder(View view) {
            super(view);
            lang = (TextView) view.findViewById(R.id.num);
            word = (TextView) view.findViewById(R.id.top);
            translate = (TextView) view.findViewById(R.id.bot);
        }
    }
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_for_history, parent, false);
        HistoryViewHolder viewHolder = new HistoryViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.word.setText(filterList.get(position));
        holder.translate.setText(translatesData.get(position));
        holder.lang.setText(dirsData.get(position));

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

    public void refresh(List<String> first,List<String> second,List<String> third){
        if (wordsData != null) {
            wordsData.clear();
            translatesData.clear();
            dirsData.clear();
            wordsData.addAll(first);
            translatesData.addAll(second);
            dirsData.addAll(third);
        }
        else {
            wordsData = first;
            translatesData = second;
            dirsData = third;
        }
        notifyDataSetChanged();
    }
}
