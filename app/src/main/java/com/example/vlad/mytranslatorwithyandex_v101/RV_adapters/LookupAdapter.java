package com.example.vlad.mytranslatorwithyandex_v101.RV_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.R;

import java.util.List;


public class LookupAdapter extends RecyclerView.Adapter<LookupAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tag;
        public ViewHolder(View view) {
            super(view);
            tag = (TextView)view.findViewById(R.id.tag);
        }
    }

    public LookupAdapter(Context mContext, List<String> mDataSet) {
        this.mContext = mContext;
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tag.setText(mDataSet.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}
