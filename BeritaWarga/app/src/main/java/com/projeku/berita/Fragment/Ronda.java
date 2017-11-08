package com.projeku.berita.Fragment;

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

import com.projeku.berita.R;

/**
 * Created by Fathurrahman on 24/04/2017.
 */

public class Ronda extends Fragment {

    private View view;
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    public static Ronda newInstance() {
        return new Ronda();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ronda, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        Viewpager adapter = new Viewpager(getActivity().getSupportFragmentManager(), getActivity());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        return view;
    }

    // class lain
    public class Viewpager extends FragmentStatePagerAdapter {

        private String tabTitles[] = new String[]{"Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu"};
        private Context context;

        public Viewpager(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    RondaSen a = new RondaSen();
                    return a;
                case 1:
                    RondaSel b = new RondaSel();
                    return b;
                case 2:
                    RondaRab c = new RondaRab();
                    return c;
                case 3:
                    RondaKam d = new RondaKam();
                    return d;
                case 4:
                    RondaJum e = new RondaJum();
                    return e;
                case 5:
                    RondaSab f = new RondaSab();
                    return f;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return tabTitles[position];
        }
    }
}
