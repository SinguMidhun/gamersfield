package com.gmf.gamersfieldesports.Pubglite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gmf.gamersfieldesports.R;

public class pubgliteActivity extends AppCompatActivity {

    private TabLayout pubglitetabs;
    private ViewPager pubglitepager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubglite);

        pubglitetabs = findViewById(R.id.pubglitetabs);
        pubglitepager = findViewById(R.id.pubglitepager);
        pubglitepager.setAdapter(new pliteAdapter(getSupportFragmentManager()));
        pubglitetabs.setupWithViewPager(pubglitepager);

    }
    private class pliteAdapter extends FragmentPagerAdapter{

        public pliteAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    pubgliteBRFragment pubgliteBRFragment = new pubgliteBRFragment();
                    return pubgliteBRFragment;
                case 1:
                    pubgliteTDMFragment pubgliteTDMFragment = new pubgliteTDMFragment();
                    return pubgliteTDMFragment;
                case 2:
                    pubgliteENDFragment pubgliteENDFragment = new pubgliteENDFragment();
                    return pubgliteENDFragment;
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