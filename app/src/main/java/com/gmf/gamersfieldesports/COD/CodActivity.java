package com.gmf.gamersfieldesports.COD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gmf.gamersfieldesports.R;

public class CodActivity extends AppCompatActivity {

    private TabLayout codtabs;
    private ViewPager codpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cod);

        codtabs = findViewById(R.id.codtabs);
        codpager = findViewById(R.id.codpager);
        codpager.setAdapter(new cpagerAdapter(getSupportFragmentManager()));
        codtabs.setupWithViewPager(codpager);

    }
    private class cpagerAdapter extends FragmentPagerAdapter{

        public cpagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    codBRFragment codBRFragment = new codBRFragment();
                    return codBRFragment;
                case 1:
                    codTDMFragment codTDMFragment = new codTDMFragment();
                    return codTDMFragment;
                case 2:
                    codENDFragment codENDFragment = new codENDFragment();
                    return codENDFragment;
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
                    return "MULTI-PLAYER";
                case 2:
                    return "ENDED";
                default:
                    return null;
            }
        }
    }
}