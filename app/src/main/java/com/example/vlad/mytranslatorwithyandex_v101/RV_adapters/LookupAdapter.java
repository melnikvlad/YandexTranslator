package com.example.vlad.mytranslatorwithyandex_v101.RV_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.R;

import java.util.List;


public class LookupAdapter extends RecyclerView.Adapter<LookupAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mTopData;
    private List<String> mBotData;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView top,bot,num;
        public ViewHolder(View view) {
            super(view);
            top = (TextView)view.findViewById(R.id.top);
            bot = (TextView)view.findViewById(R.id.bot);
            num = (TextView)view.findViewById(R.id.num);
        }
    }

    public LookupAdapter(Context mContext, List<String> mTopData,List<String> mBotData) {
        this.mContext = mContext;
        this.mTopData = mTopData;
        this.mBotData = mBotData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.top.setText(mTopData.get(position).toString());
        holder.bot.setText(mBotData.get(position).toString());
        holder.num.setText(""+(position+1));

    }

    @Override
    public int getItemCount() {
        return mTopData.size();
    }
}
