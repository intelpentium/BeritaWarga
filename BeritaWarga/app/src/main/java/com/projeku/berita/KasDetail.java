package com.projeku.berita;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.projeku.berita.Fragment.KasDetailAgs;
import com.projeku.berita.Fragment.KasDetailApr;
import com.projeku.berita.Fragment.KasDetailDes;
import com.projeku.berita.Fragment.KasDetailFeb;
import com.projeku.berita.Fragment.KasDetailJul;
import com.projeku.berita.Fragment.KasDetailJun;
import com.projeku.berita.Fragment.KasDetailMar;
import com.projeku.berita.Fragment.KasDetailMay;
import com.projeku.berita.Fragment.KasDetailNov;
import com.projeku.berita.Fragment.KasDetailOkt;
import com.projeku.berita.Fragment.KasDetailSep;
import com.projeku.berita.Fragment.Ronda;
import com.projeku.berita.Fragment.KasDetailJan;

/**
 * Created by Fathurrahman on 28/04/2017.
 */

public class KasDetail extends AppCompatActivity {

    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kas_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager = (ViewPager) findViewById(R.id.pager);
        Viewpager adapter = new Viewpager(getSupportFragmentManager(), KasDetail.this);

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
    }

    // class lain
    public class Viewpager extends FragmentStatePagerAdapter {

        private String tabTitles[] = new String[]{"Jan","Feb","Mar","Apr","May","Jun", "Jul","Ags","Sep","Okt","Nov","Des"};
        private Context context;

        public Viewpager(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    KasDetailJan a = new KasDetailJan();
                    return a;
                case 1:
                    KasDetailFeb b = new KasDetailFeb();
                    return b;
                case 2:
                    KasDetailMar c = new KasDetailMar();
                    return c;
                case 3:
                    KasDetailApr d = new KasDetailApr();
                    return d;
                case 4:
                    KasDetailMay e = new KasDetailMay();
                    return e;
                case 5:
                    KasDetailJun f = new KasDetailJun();
                    return f;
                case 6:
                    KasDetailJul g = new KasDetailJul();
                    return g;
                case 7:
                    KasDetailAgs h = new KasDetailAgs();
                    return h;
                case 8:
                    KasDetailSep i = new KasDetailSep();
                    return i;
                case 9:
                    KasDetailOkt j = new KasDetailOkt();
                    return j;
                case 10:
                    KasDetailNov k = new KasDetailNov();
                    return k;
                case 11:
                    KasDetailDes l = new KasDetailDes();
                    return l;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            ActivityCompat.finishAfterTransition(KasDetail.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
