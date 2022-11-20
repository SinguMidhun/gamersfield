package com.gmf.gamersfieldesports.Luckydraw;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.gmf.gamersfieldesports.R;
import com.google.android.material.tabs.TabLayout;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

public class LuckydrawActivity extends AppCompatActivity {

    private TabLayout luckytabs;
    private ViewPager luckypager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckydraw);

        //referance
        luckytabs = findViewById(R.id.luckytabs);
        luckypager = findViewById(R.id.luckypager);
        luckypager.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        luckytabs.setupWithViewPager(luckypager);

        StartAppSDK.init(this, getString(R.string.startAppId), true);

    }

    private class pagerAdapter extends FragmentPagerAdapter {

        public pagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    luckyBRFragment luckyBRFragment = new luckyBRFragment();
                    return luckyBRFragment;
                case 1:
                    luckyENDFragment luckyENDFragment = new luckyENDFragment();
                    return luckyENDFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "ON GOING";
                case 1:
                    return "ENDED";
                default:
                    return null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StartAppAd.onBackPressed(this);
    }
}