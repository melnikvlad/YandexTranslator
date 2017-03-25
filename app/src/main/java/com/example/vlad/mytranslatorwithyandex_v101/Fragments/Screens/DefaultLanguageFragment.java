package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.MainActivity;
import com.example.vlad.mytranslatorwithyandex_v101.R;


public class DefaultLanguageFragment extends Fragment {
    TextView selected_lang;
    Button select_lang;
    Context applicationContext;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        applicationContext = MainActivity.getContextOfApplication();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        View view = inflater.inflate(R.layout.default_lang_fragment,container,false);
        select_lang = (Button) view.findViewById(R.id.select_language);
        selected_lang = (TextView)view.findViewById(R.id.selected_lang);

        selected_lang.setText(sharedPreferences.getString(Constants.DEFAULT_LANGUAGE,""));
        select_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSettingsFragment();
            }
        });
        return view;
    }

    private void goToSettingsFragment(){
        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.BTN_CLICKED,3);
        editor.apply();

        SettingsFragment fragment = new SettingsFragment();
        android.support.v4.app.FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.defaultlang_fragment_frame,fragment);
        ft.commit();
    }


}
