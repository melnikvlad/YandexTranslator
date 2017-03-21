package com.example.vlad.mytranslatorwithyandex_v101.RV_adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.R;

import java.util.List;

public class getLangsAdapter extends RecyclerView.Adapter<getLangsAdapter.LanguagesViewHolder> {
    private Context mContext;
    private List<String> langData;

    public getLangsAdapter(Context mContext, List<String> langData) {
        this.mContext = mContext;
        this.langData = langData;
    }

    @Override
    public getLangsAdapter.LanguagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_settings,parent,false);
        getLangsAdapter.LanguagesViewHolder viewHolder = new getLangsAdapter.LanguagesViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(getLangsAdapter.LanguagesViewHolder holder, int position) {
        holder.lang.setText(langData.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return langData.size();
    }

    public static class LanguagesViewHolder extends RecyclerView.ViewHolder{
        public TextView lang;
        public LanguagesViewHolder(View view) {
            super(view);
            lang = (TextView)view.findViewById(R.id.lang);

        }
    }
}
