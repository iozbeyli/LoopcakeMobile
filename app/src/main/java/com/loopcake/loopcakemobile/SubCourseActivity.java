package com.loopcake.loopcakemobile;

import android.app.Activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;

import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;

public class SubCourseActivity extends AppCompatActivity {
    private static String TAG = "SubCourse";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Enumerators.CourseActions ca = (Enumerators.CourseActions) getIntent().getSerializableExtra("fragment");
        String courseName = getIntent().getExtras().getString("courseName");
        setTitle(courseName);
        switch (ca){
            case CREATE_ANNOUNCEMENT:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.subFrame, new CreateAnnouncementFragment());
                ft.commit();
                break;
            default:
                Log.wtf(TAG, "unknown course action");
        }
    }
}
