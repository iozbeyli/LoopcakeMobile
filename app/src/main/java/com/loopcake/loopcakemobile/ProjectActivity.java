package com.loopcake.loopcakemobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.PostDatas.GroupPostDatas;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectActivity extends LCTabbedActivity implements Communicator {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getGroupURL, GroupPostDatas.getGroupDetailsPostData(),this);
        asyncCommunicationTask.execute((Void) null);
    }

    @Override
    public SectionsPagerAdapter createSectionPagerAdapter(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> pageTitles = new ArrayList<>();
        ProjectChecklistFragment clFragment = new ProjectChecklistFragment();
        fragments.add(clFragment);
        ProjectDetailFragment projectDetailFragment = new ProjectDetailFragment();
        fragments.add(projectDetailFragment);
        ProjectAttachmentFragment projectAttachmentFragment = new ProjectAttachmentFragment();
        fragments.add(projectAttachmentFragment);
        pageTitles.add("Progress");
        pageTitles.add("Details");
        pageTitles.add("Submission");
        return new SectionsPagerAdapter(getSupportFragmentManager(),fragments,pageTitles);
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        Log.d("execute", "successfulExecute: ");
        Session.selectedProject = jsonObject;

        setTabView();
    }

    @Override
    public void failedExecute() {
        Log.d("execute", "failedExecute: ");
    }
}
