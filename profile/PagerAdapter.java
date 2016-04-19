package com.example.adrien.lolhelper.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Created by Adrien CESARO on 15/02/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int mNumOfTabs){
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch(position){

            case  0:
                ProfileRanked tab2 = new ProfileRanked();
                return tab2;
            case  1:
                ProfileRecentGame tab1 = new ProfileRecentGame();
                return tab1;
            case 2:
                ProfileChampion tab3 = new ProfileChampion();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount(){
        return mNumOfTabs;
    }


}
