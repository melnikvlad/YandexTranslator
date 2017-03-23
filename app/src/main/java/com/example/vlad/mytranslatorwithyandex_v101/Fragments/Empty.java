package com.example.vlad.mytranslatorwithyandex_v101.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.R;



public class Empty extends Fragment {
   // TextView default_language;
    //private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.error,container,false);
        //default_language = (TextView) view.findViewById(R.id.empty);
       // Context applicationContext = MainActivity.getContextOfApplication();
        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        //default_language.setText(sharedPreferences.getString(Constants.DEFAULT_LANGUAGE,"").toString());

        return view;
    }
}
