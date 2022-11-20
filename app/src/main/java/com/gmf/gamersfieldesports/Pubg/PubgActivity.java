package com.gmf.gamersfieldesports.Pubg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gmf.gamersfieldesports.R;

public class PubgActivity extends AppCompatActivity {

    private TabLayout pubgtabs;
    private ViewPager pubgpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubg);

        pubgtabs = findViewById(R.id.pubgtabs);
        pubgpager = findViewById(R.id.pubgpager);
        pubgpager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        pubgtabs.setupWithViewPager(pubgpager);

    }

    private class pagerAdapter extends FragmentPagerAdapter{

        public pagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    pubgBR_Fragment pubgBR_fragment = new pubgBR_Fragment();
                    return pubgBR_fragment;
                case 1:
                    pubgTDMFragment pubgTDMFragment = new pubgTDMFragment();
                    return pubgTDMFragment;
                case 2:
                    pubgENDFragment pubgENDFragment = new pubgENDFragment();
                    return pubgENDFragment;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "BATTLE ROYAL";
                case 1:
                    return "TDMs";
                case 2:
                    return "ENDED";
                default:
                    return null;
            }
        }
    }
}