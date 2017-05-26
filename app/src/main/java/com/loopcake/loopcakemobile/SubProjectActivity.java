package com.loopcake.loopcakemobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.ChecklistItem;
import com.loopcake.loopcakemobile.ListContents.StudentSelect;

/**
 * Created by MEHMET on 25.05.2017.
 */

public class SubProjectActivity extends AppCompatActivity {
    private static String TAG = "SubProject";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Enumerators.ProjectActions ca = (Enumerators.ProjectActions) getIntent().getSerializableExtra("fragment");
        setTitle(Session.project.title);
        Fragment fra = null;
        switch (ca){
            case CREATE_GROUP:
                fra = new CreateGroupFragment();
                break;
            case GROUP_MEMBER:
                fra = new GroupMemberFragment();
                break;
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
