package com.gmf.gamersfieldesports.Snakerivals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gmf.gamersfieldesports.R;

public class SnakerivalsActivity extends AppCompatActivity {

    private TabLayout snakerivalstabs;
    private ViewPager snakerivalspager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snakerivals);

        snakerivalstabs = findViewById(R.id.snakerivalstabs);
        snakerivalspager = findViewById(R.id.snakerivalspager);
        snakerivalspager.setAdapter(new snakepagerAdapter(getSupportFragmentManager()));
        snakerivalstabs.setupWithViewPager(snakerivalspager);

    }
    private class snakepagerAdapter extends FragmentPagerAdapter{

        public snakepagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    snakeBRFragment snakeBRFragment = new snakeBRFragment();
                    return snakeBRFragment;
                case 1:
                    snakeENDFragment snakeENDFragment = new snakeENDFragment();
                    return snakeENDFragment;
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