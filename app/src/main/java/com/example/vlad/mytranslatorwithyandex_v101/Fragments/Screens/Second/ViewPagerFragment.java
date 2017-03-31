package com.example.vlad.mytranslatorwithyandex_v101.Fragments.Screens.Second;


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
import com.example.vlad.mytranslatorwithyandex_v101.R;

public class ViewPagerFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    HistoryFragment historyFragment;
    FavouriteFragment favouriteFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_viewpager,container,false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.main_tab_content);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),getActivity()));
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter{
        Context context;
        private String fragments [] = {"История","Избранное"};
        public ViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    historyFragment = new HistoryFragment();
                    return historyFragment;
                case 1:
                    favouriteFragment = new FavouriteFragment();
                    return favouriteFragment;
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            return Constants.SECOND_SCR_TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }
}
