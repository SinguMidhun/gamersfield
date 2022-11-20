package com.gmf.gamersfieldesports.Ludo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gmf.gamersfieldesports.R;

public class LudoActivity extends AppCompatActivity {

    private TabLayout ludotabs;
    private ViewPager ludopager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ludo);

        ludotabs = findViewById(R.id.ludotabs);
        ludopager = findViewById(R.id.ludopager);
        ludopager.setAdapter(new ludopagerAdapter(getSupportFragmentManager()));
        ludotabs.setupWithViewPager(ludopager);

    }

    private class ludopagerAdapter extends FragmentPagerAdapter {

        public ludopagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    ludoBRFragment ludoBRFragment = new ludoBRFragment();
                    return ludoBRFragment;
                case 1:
                    ludoENDFragment ludoENDFragment = new ludoENDFragment();
                    return ludoENDFragment;
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
                    return "ARENA";
                case 1:
                    return "ENDED";
                default:
                    return null;
            }
        }
    }

}