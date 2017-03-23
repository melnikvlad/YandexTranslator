package com.example.vlad.mytranslatorwithyandex_v101.RV_adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Empty;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.R;

import java.util.ArrayList;
import java.util.List;

public class getLangsAdapter extends RecyclerView.Adapter<getLangsAdapter.LanguagesViewHolder>  {
    private Context mContext;
    private List<String> langData;
    private List<String> filterList;

    public class LanguagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView lang;
        private Context context;


        public LanguagesViewHolder(View view) {
            super(view);
            context = view.getContext();
            lang = (TextView) view.findViewById(R.id.lang);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Empty empty = new Empty();
            FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_frame,empty).commit();
            fragmentManager.beginTransaction().addToBackStack(null);
        }
    }

    public getLangsAdapter() {}

    public getLangsAdapter(Context mContext, List<String> langData) {
        super();
        this.mContext = mContext;
        this.langData = langData;
        this.filterList = new ArrayList<>();
        this.filterList.addAll(this.langData); // we copy the original list to the filter list and use it for setting row values
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
