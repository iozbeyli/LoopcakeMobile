package com.loopcake.loopcakemobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.PostDatas.GroupPostDatas;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectActivity extends LCTabbedActivity implements Communicator {

    @Override
    public void onCreateFunction() {



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
    public ArrayList<ArrayList<String>> setTextListsForFragments() {
        ArrayList<ArrayList<String>> fragmentTextLists=new ArrayList<>();
        ArrayList<String> fabTexts = new ArrayList<>();
        fabTexts.add("Naber");
        fragmentTextLists.add(fabTexts);
        fabTexts = new ArrayList<>();
        fabTexts.add("Nabe");
        fragmentTextLists.add(fabTexts);
        fragmentTextLists.add(fabTexts);
        return fragmentTextLists;
    }

    @Override
    public ArrayList<ArrayList<View.OnClickListener>> setListenerListsForFragments() {
        ArrayList<ArrayList<View.OnClickListener>> fragmentListenerLists = new ArrayList<>();
        ArrayList<View.OnClickListener> listeners = new ArrayList<>();
        listeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("faa","a");
            }
        });
        fragmentListenerLists.add(listeners);
        fragmentListenerLists.add(listeners);
        fragmentListenerLists.add(listeners);
        return fragmentListenerLists;
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
