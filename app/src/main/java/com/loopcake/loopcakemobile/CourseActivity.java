package com.loopcake.loopcakemobile;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.loopcake.loopcakemobile.CourseFragments.CourseDetailFragment;
import com.loopcake.loopcakemobile.CourseFragments.CourseStudentFragment;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;

import java.util.ArrayList;

public class CourseActivity extends LCTabbedActivity {

    @Override
    public void onCreateFunction() {
        ArrayList<String> fabTexts = new ArrayList<>();
        fabTexts.add("Naber");
        ArrayList<View.OnClickListener> listeners = new ArrayList<>();
        listeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("faa","a");
            }
        });
        setSubFabs(fabTexts,listeners);
    }

    @Override
    public SectionsPagerAdapter createSectionPagerAdapter(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> pageTitles = new ArrayList<>();
        fragments.add(new CourseDetailFragment());
        AnnouncementFragment announcementFragment = AnnouncementFragment.newInstance(Enumerators.AnnouncementType.COURSE);
        fragments.add(announcementFragment);
        CourseStudentFragment studentListFragment = new CourseStudentFragment();
        fragments.add(studentListFragment);
        pageTitles.add("Details");
        pageTitles.add("Announce");
        pageTitles.add("Students");
        return new SectionsPagerAdapter(getSupportFragmentManager(),fragments,pageTitles);
    }

}
