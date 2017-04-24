package com.loopcake.loopcakemobile.TabbedActivities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.loopcake.loopcakemobile.CourseActivity;

import java.util.List;

/**
 * Created by Melih on 24.04.2017.
 */

public class SectionsPagerAdapter  extends FragmentPagerAdapter{
    List<String> pageTitles;
    List<Fragment> fragments;
    public SectionsPagerAdapter(FragmentManager fm,List<Fragment> fragments,List<String> pageTitles) {
        super(fm);
        this.pageTitles = pageTitles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        //return CourseActivity.PlaceholderFragment.newInstance(position + 1);
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
       return fragments.size();
       // return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position <=(pageTitles.size()-1)){
            return pageTitles.get(position);
        }
        return null;

    }
}
