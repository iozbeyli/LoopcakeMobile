package com.loopcake.loopcakemobile;

import android.content.Intent;
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


    }

    @Override
    public SectionsPagerAdapter createSectionPagerAdapter(){
        setTitle(Session.selectedCourse.name);
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

    @Override
    public ArrayList<ArrayList<String>> setTextListsForFragments() {
        return createFABTexts(new String[][]{
                {"Edit Course", "Create Project", null},
                {"Create Announcement", null, null},
                {null, null, null}
        });

    }

    @Override
    public ArrayList<ArrayList<View.OnClickListener>> setListenerListsForFragments() {
        return createFABListeners(new Enumerators.CourseActions[][]{
                {Enumerators.CourseActions.EDIT_COURSE, Enumerators.CourseActions.CREATE_PROJECT, null},
                {Enumerators.CourseActions.CREATE_ANNOUNCEMENT, null, null},
                {null, null, null}
        });
    }

    private View.OnClickListener createFABListener(final Enumerators.CourseActions act){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(CourseActivity.this, SubCourseActivity.class);
                in.putExtra("fragment", act);
                startActivity(in);
            }
        };
    }

    private ArrayList<ArrayList<View.OnClickListener>> createFABListeners(Enumerators.CourseActions[][] act){
        ArrayList<ArrayList<View.OnClickListener>> fragmentListenerLists = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {
            ArrayList<View.OnClickListener> listeners = new ArrayList<>();
            for (int j = 0; j < 3 ; j++) {
                if(act[i][j] != null)
                    listeners.add(createFABListener(act[i][j]));
            }
            fragmentListenerLists.add(listeners);
        }
        return fragmentListenerLists;
    }


    private ArrayList<ArrayList<String>> createFABTexts(String[][] lab){
        ArrayList<ArrayList<String>> labelList = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {
            ArrayList<String> labels = new ArrayList<>();
            for (int j = 0; j < 3 ; j++) {
                if(lab[i][j] != null)
                    labels.add(lab[i][j]);
            }
            labelList.add(labels);
        }

        return labelList;
    }



}
