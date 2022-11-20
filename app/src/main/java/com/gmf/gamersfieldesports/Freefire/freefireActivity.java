package com.gmf.gamersfieldesports.Freefire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gmf.gamersfieldesports.R;

public class freefireActivity extends AppCompatActivity {

    private TabLayout freefiretabs;
    private ViewPager freefirepager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freefire);

        freefirepager = findViewById(R.id.freefirepager);
        freefiretabs = findViewById(R.id.freefiretabs);
        freefirepager.setAdapter(new fpagerAdapter(getSupportFragmentManager()));
        freefiretabs.setupWithViewPager(freefirepager);

    }
    private class fpagerAdapter extends FragmentPagerAdapter{

        public fpagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    freefireBRFragment freefireBRFragment = new freefireBRFragment();
                    return freefireBRFragment;
                case 1:
                    freefireTDMFragment freefireTDMFragment = new freefireTDMFragment();
                    return freefireTDMFragment;
                case 2:
                    freefireENDFragment freefireENDFragment = new freefireENDFragment();
                    return freefireENDFragment;
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
                    return "CLASH SQUAD";
                case 2:
                    return "ENDED";
                default:
                    return null;
            }
        }
    }
}