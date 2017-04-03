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

    /*
        First of all we check for existing data in our SQLite,
        if we haven't  load necessary default language,
        then setup some constants and go to LanguagesFragment, where we should select language,
        else  go to the app main screen
    */
    private void initView(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Fragment fragment;
        if(db.getLanguagesCount() == 0){
            fragment = new LanguagesFragment();
            editor.putInt(Constants.ID_OF_ACTION,3); // Some id for a several buttons
            editor.putString(Constants.DEFAULT_LANGUAGE_UI,"en");
            editor.putString(Constants.DEFAULT_LANGUAGE_INTERFACE,"en");
            editor.putString(Constants.TRANSLATE_FROM,"en");
            editor.putString(Constants.TRANSLATE_TO,"ru");
            editor.putString(Constants.LAST_ACTION,""); // remember what word we worked with last time
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
