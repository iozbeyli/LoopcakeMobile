package com.loopcake.loopcakemobile;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;

public class SubCourseActivity extends AppCompatActivity {
    private static String TAG = "SubCourse";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Enumerators.CourseActions ca = (Enumerators.CourseActions) getIntent().getSerializableExtra("fragment");
        setTitle(Session.selectedCourse.name);
        LCFragment fra = null;
        switch (ca){
            case CREATE_ANNOUNCEMENT:
                fra = new CreateAnnouncementFragment();
                break;
            case CREATE_PROJECT:
                fra = new CreateProjectFragment();
                break;
            case EDIT_COURSE:
                fra = new EditCourseFragment();
            default:
                Log.wtf(TAG, "unknown course action");
        }
        if(fra != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.subFrame, fra);
            ft.commit();
        }

    }
}
