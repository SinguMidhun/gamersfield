package com.gmf.gamersfieldesports.Minimilitia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gmf.gamersfieldesports.R;

public class MinimilitiaActivity extends AppCompatActivity {

    private TabLayout minitabs;
    private ViewPager minipager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minimilitia);

        minitabs = findViewById(R.id.minimilitiatabs);
        minipager = findViewById(R.id.minimilitiapager);
        minipager.setAdapter(new minipagerAdapter(getSupportFragmentManager()));
        minitabs.setupWithViewPager(minipager);

    }
    private class minipagerAdapter extends FragmentPagerAdapter{

        public minipagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    miniBRFragment miniBRFragment = new miniBRFragment();
                    return miniBRFragment;
                case 1:
                    miniENDFragment miniENDFragment = new miniENDFragment();
                    return miniENDFragment;
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
                    return "BATTLE ROYAL";
                case 1:
                    return "ENDED";
                default:
                    return null;
            }
        }
    }
}