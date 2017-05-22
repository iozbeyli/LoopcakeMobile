package com.loopcake.loopcakemobile;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.PostDatas.GroupPostDatas;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;
import com.loopcake.loopcakemobile.RepoFragments.RepoBranchTreeFragment;
import com.loopcake.loopcakemobile.RepoFragments.RepoDetailsFragment;
import com.loopcake.loopcakemobile.RepoFragments.RepoFilesFragment;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Melih on 23.05.2017.
 */

public class RepoActivity extends LCTabbedActivity implements Communicator{
    @Override
    public void onCreateFunction() {
        ArrayList<String> fabTexts = new ArrayList<>();
        fabTexts.add("Upload File");
        ArrayList<View.OnClickListener> listeners = new ArrayList<>();
        listeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("faa","a");
            }
        });
        setSubFabs(fabTexts,listeners);
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getRepoURL, RepoPostDatas.getRepoDetailsPostData(Session.selectedRepo.repoID),this);
        asyncCommunicationTask.execute((Void) null);
    }

    @Override
    public SectionsPagerAdapter createSectionPagerAdapter() {
        setTitle(Session.selectedRepo.repoName);
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> pageTitles = new ArrayList<>();
        RepoDetailsFragment repoDetailsFragment = new RepoDetailsFragment();
        fragments.add(repoDetailsFragment);
        RepoFilesFragment repoFilesFragment = new RepoFilesFragment();
        fragments.add(repoFilesFragment);
        RepoBranchTreeFragment repoBranchTreeFragment = new RepoBranchTreeFragment();
        fragments.add(repoBranchTreeFragment);
        pageTitles.add("Details");
        pageTitles.add("Files");
        pageTitles.add("Branch Tree");
        return new SectionsPagerAdapter(getSupportFragmentManager(),fragments,pageTitles);
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        Log.d("repo response",jsonObject.toString());
        setTabView();
    }

    @Override
    public void failedExecute() {

    }
}
