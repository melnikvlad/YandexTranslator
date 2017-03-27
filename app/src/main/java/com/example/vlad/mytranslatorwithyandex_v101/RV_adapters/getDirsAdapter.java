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


public class getDirsAdapter extends RecyclerView.Adapter<getDirsAdapter.DirectionsViewHolder> {
    private Context mContext;
    private List<String> dirsData;
    private List<String> filterList;

    public getDirsAdapter() {
    }

    public getDirsAdapter(Context mContext, List<String> dirsData) {
        this.mContext = mContext;
        this.dirsData = dirsData;
        this.filterList = new ArrayList<>();
        this.filterList.addAll(this.dirsData); // we copy the original list to the filter list and use it for setting row values
    }

    public static class DirectionsViewHolder extends RecyclerView.ViewHolder {
        private TextView dir;

        public DirectionsViewHolder(View view) {
            super(view);
            dir = (TextView) view.findViewById(R.id.lang);
        }
    }

    @Override
    public DirectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_languages, parent, false);
        DirectionsViewHolder viewHolder = new DirectionsViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DirectionsViewHolder holder, int position) {
        holder.dir.setText(filterList.get(position));
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
            filterList.addAll(dirsData);
        }
        else {
            // Iterate in the original List and add it to filter list...
            for (int i=0;i<dirsData.size();i++) {
                if (dirsData.get(i).toLowerCase().contains(text.toLowerCase()) ||
                        dirsData.get(i).toLowerCase().contains(text.toLowerCase())) {
                    // Adding Matched items
                    filterList.add(dirsData.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }




}
