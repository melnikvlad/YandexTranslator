package com.example.vlad.mytranslatorwithyandex_v101.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vlad.mytranslatorwithyandex_v101.Constants.Constants;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.FavouriteFragment;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.SettingsFragment;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.TranslateFragment;
import com.example.vlad.mytranslatorwithyandex_v101.R;


public class ViewPagerFragment extends Fragment {
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int[] icons = {R.drawable.profile,R.drawable.search2, R.drawable.settings2};
        View view = inflater.inflate(R.layout.viewpager,container,false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.main_tab_content);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),getActivity()));
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }
        tabLayout.getTabAt(0).select();
        return view;
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        Context context;

        public ViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context =context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                   TranslateFragment translateFragment = new TranslateFragment();
                    return translateFragment;
                case 1:
                   FavouriteFragment favouriteFragment = new FavouriteFragment();
                    return favouriteFragment;
                case 2:
                   SettingsFragment settingsFragment = new SettingsFragment();
                    return settingsFragment;

                default:
                    translateFragment = new TranslateFragment();
                    return translateFragment;
            }
        }

        @Override
        public int getCount() {
            return Constants.TAB_COUNT;
        }
    }
}
