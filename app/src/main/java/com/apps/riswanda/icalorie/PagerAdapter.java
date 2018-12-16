package com.apps.riswanda.icalorie;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.Switch;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fn, int NumberOfTabs){
        super(fn);
        this.mNoOfTabs = NumberOfTabs;
    }
    @Override
    public Fragment getItem(int Position) {
        switch(Position){

            case 0:
                HomeScreen homeScreen = new HomeScreen();
                return homeScreen;
            case 1:
                MenuMakan menuMakan = new MenuMakan();
                return menuMakan;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
