package com.example.vlad.mytranslatorwithyandex_v101.Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
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
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second.HistoryFragment;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second.ViewPagerFragment;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Third.SettingsFragment;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second.FavouriteFragment;
import com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.First.TranslateFragment;
import com.example.vlad.mytranslatorwithyandex_v101.R;


public class MainScreen extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int[] icons = {R.drawable.main,R.drawable.hostory, R.drawable.set2};
        View view = inflater.inflate(R.layout.viewpager,container,false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.main_tab_content);

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),getActivity()));
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < icons.length; i++) {
            tabLayout.getTabAt(i).setIcon(icons[i]);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.getAdapter().notifyDataSetChanged();
                for (int i = 0; i < icons.length; i++) {
                    tabLayout.getTabAt(i).setIcon(icons[i]);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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
                   ViewPagerFragment historyFragment = new ViewPagerFragment();
                    return historyFragment;
                case 2:
                   SettingsFragment defaultLanguageFragment = new SettingsFragment();
                    return defaultLanguageFragment;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return Constants.MAIN_SCR_TAB_COUNT;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
