package com.example.vlad.mytranslatorwithyandex_v101;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.vlad.mytranslatorwithyandex_v101.Fragments.ViewPagerFragment;

public class MainActivity extends FragmentActivity
{
    public static Context contextOfApplication;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contextOfApplication = getApplicationContext();
        initView();
    }
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
    private void initView(){
        Fragment fragment = new ViewPagerFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }
}
