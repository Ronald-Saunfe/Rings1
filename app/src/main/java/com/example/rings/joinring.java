package com.example.rings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rings.fragment.International;
import com.example.rings.fragment.national;
import com.example.rings.fragment.search;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.kekstudio.dachshundtablayout.DachshundTabLayout;
import com.kekstudio.dachshundtablayout.indicators.DachshundIndicator;

public class joinring extends AppCompatActivity {

    DachshundTabLayout nts;
    ViewPager2 vwpg2;
    EditText Etsearchjr;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    TextView natoionaltab, internationaltab;
    ImageView imgsearch;

    int vpstate=0, pos =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinring);

        nts = findViewById(R.id.tbl);
        Etsearchjr = findViewById(R.id.Etsearchjr);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.vwpg2);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        nts.setupWithViewPager(mPager);
        //set the indicator animation
        DachshundIndicator indicator = new DachshundIndicator(nts);
        nts.setAnimatedIndicator(indicator);

        imgsearch = new ImageView(this);
        imgsearch.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_search_24));
        imgsearch.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.sd_label_text_color), android.graphics.PorterDuff.Mode.SRC_IN);

        natoionaltab = new TextView(this);
        natoionaltab.setText("NATIONAL");
        natoionaltab.setTextSize( 14 / getResources().getDisplayMetrics().scaledDensity);

        internationaltab = new TextView(this);
        internationaltab.setText("INTERNATIONAL");
        internationaltab.setTextSize( 14 / getResources().getDisplayMetrics().scaledDensity);

        nts.getTabAt(0).setCustomView(imgsearch);
        nts.getTabAt(1).setCustomView(natoionaltab);
        nts.getTabAt(2).setCustomView(internationaltab);

        mPager.setCurrentItem(1);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("onPageScrolled:=======position===="+position+"==========positionOffset======"+positionOffset+"======positionOffsetPixels===="+positionOffsetPixels);
                //if the viewpager is scrolled to the left to the icon tab  position = 0
                //if the viewpager is currently being scrolled state ==1
                //if the viewpager is on the icon tab the position = 0
                // the positionOffset shows deviation from current position to the initial position
                if (position == 0 && vpstate==1){
                    natoionaltab.setAlpha(positionOffset);
                    internationaltab.setAlpha(positionOffset);
                    Etsearchjr.setAlpha(Math.abs(1-positionOffset));
                    Etsearchjr.setVisibility(View.VISIBLE);
                }
                pos = position;
            }

            @Override
            public void onPageSelected(int position) {
                System.out.println("onPageSelected:=======position==="+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                vpstate = state;
                if (state ==0 &&pos==0 ){
                    natoionaltab.setAlpha(0f);
                    internationaltab.setAlpha(0f);
                    Etsearchjr.setAlpha(1f);
                    Etsearchjr.setVisibility(View.VISIBLE);
                }
                else if (state ==0 &&pos==1 || state ==0 &&pos==2  ){
                    Etsearchjr.setAlpha(0f);
                    natoionaltab.setAlpha(1f);
                    internationaltab.setAlpha(1f);
                }

                if (state ==0 &&pos==0 ){
                    Etsearchjr.setVisibility(View.VISIBLE);
                    natoionaltab.setVisibility(View.GONE);
                    internationaltab.setVisibility(View.GONE);

                    imgsearch.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.gren), android.graphics.PorterDuff.Mode.SRC_IN);
                    natoionaltab.setTextColor(getResources().getColor(R.color.sd_label_text_color));
                    internationaltab.setTextColor(getResources().getColor(R.color.sd_label_text_color));
                }
                else if (state ==0 &&pos==1 ){
                    Etsearchjr.setVisibility(View.GONE);
                    natoionaltab.setVisibility(View.VISIBLE);
                    internationaltab.setVisibility(View.VISIBLE);

                    imgsearch.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.sd_label_text_color), android.graphics.PorterDuff.Mode.SRC_IN);
                    natoionaltab.setTextColor(getResources().getColor(R.color.gren));
                    internationaltab.setTextColor(getResources().getColor(R.color.sd_label_text_color));
                }
                else if (state ==0 &&pos==2 ){
                    Etsearchjr.setVisibility(View.GONE);
                    natoionaltab.setVisibility(View.VISIBLE);
                    internationaltab.setVisibility(View.VISIBLE);

                    internationaltab.setTextColor(getResources().getColor(R.color.gren));
                    natoionaltab.setTextColor(getResources().getColor(R.color.sd_label_text_color));
                    imgsearch.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.sd_label_text_color), android.graphics.PorterDuff.Mode.SRC_IN);
                }

                System.out.println("onPageScrollStateChanged:=======state==="+state);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new search();
                case 1:
                    return new national();
                case 2:
                    return new International();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return super.getPageTitle(position);
        }
    }
}