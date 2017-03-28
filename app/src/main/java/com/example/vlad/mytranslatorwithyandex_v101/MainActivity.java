package com.example.vlad.mytranslatorwithyandex_v101;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.DataBaseSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Third.LanguagesFragment;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.MainScreen;

public class MainActivity extends FragmentActivity {
    public static Context contextOfApplication;
    private DataBaseSQLite db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();
        db = new DataBaseSQLite(getApplication().getApplicationContext());
        initView();
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    private void initView(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Fragment fragment;
        if(db.getLanguagesCount() == 0){
            fragment = new LanguagesFragment();
            editor.putInt(Constants.BTN_CLICKED,3);
            editor.putString(Constants.DEFAULT_LANGUAGE,"en");
            editor.putString(Constants.DEFAULT_LANGUAGE_INTERFACE,"en");
            editor.putString(Constants.TRANSLATE_FROM,"en");
            editor.putString(Constants.TRANSLATE_TO,"ru");
            editor.apply();
        }
        else {
            fragment = new MainScreen();
        }
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }
}
