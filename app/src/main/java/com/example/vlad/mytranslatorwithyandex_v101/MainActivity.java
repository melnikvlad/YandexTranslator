package com.example.vlad.mytranslatorwithyandex_v101;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.DB.LanguagesSQLite;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.LanguagesFragment;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.ViewPagerFragment;

public class MainActivity extends FragmentActivity
{
    public static Context contextOfApplication;
    private LanguagesSQLite db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();
        db = new LanguagesSQLite(getApplication().getApplicationContext());
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
            Log.d(Constants.TAG,"Переходим из MainActivity--> LanguagesFragment");
            fragment = new LanguagesFragment();
            editor.putInt(Constants.BTN_CLICKED,3);
            editor.putString(Constants.DEFAULT_LANGUAGE,"en");
            editor.putString(Constants.DEFAULT_LANGUAGE_INTERFACE,"en");
            editor.putString(Constants.TRANSLATE_FROM,"en");
            editor.putString(Constants.TRANSLATE_TO,"ru");
            editor.putInt(Constants.REFRESH,1);
            editor.apply();
        }
        else {
            Log.d(Constants.TAG,"Переходим из MainActivity--> ViewPagerFragment");
            fragment = new ViewPagerFragment();
        }
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }
}
