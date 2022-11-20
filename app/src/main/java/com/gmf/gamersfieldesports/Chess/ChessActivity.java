package com.gmf.gamersfieldesports.Chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.gmf.gamersfieldesports.R;

public class ChessActivity extends AppCompatActivity {

    private TabLayout chesslayout;
    private ViewPager chesspager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);

        chesslayout = findViewById(R.id.chesstabs);
        chesspager = findViewById(R.id.chesspager);
        chesspager.setAdapter(new chessPagerAdapter(getSupportFragmentManager()));
        chesslayout.setupWithViewPager(chesspager);

    }
    private class chessPagerAdapter extends FragmentPagerAdapter{

        public chessPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    chessBRFragment chessBRFragment = new chessBRFragment();
                    return chessBRFragment;
                case 1:
                    chessENDFragment chessENDFragment = new chessENDFragment();
                    return chessENDFragment;
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