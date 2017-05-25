package com.loopcake.loopcakemobile;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCList.LCListItems.Repo;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;
import com.loopcake.loopcakemobile.RepoFragments.LCFile;
import com.loopcake.loopcakemobile.RepoFragments.RepoBranchTreeFragment;
import com.loopcake.loopcakemobile.RepoFragments.RepoDetailsFragment;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Melih on 23.05.2017.
 */

public class RepoActivity extends LCTabbedActivity implements Communicator{
    @Override
    public void onCreateFunction() {
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
    public ArrayList<ArrayList<String>> setTextListsForFragments() {
        ArrayList<ArrayList<String>> fragmentTextLists=new ArrayList<>();
        ArrayList<String> fabTexts = new ArrayList<>();
        fabTexts.add("Naber");
        fragmentTextLists.add(fabTexts);
        fragmentTextLists.add(fabTexts);
        ArrayList<String> fabTexts3 = new ArrayList<>();
        fabTexts.add("Create Branch");
        fragmentTextLists.add(fabTexts3);
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
        ArrayList<View.OnClickListener> listeners3 = new ArrayList<>();
        listeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("faa","a");
            }
        });
        fragmentListenerLists.add(listeners3);
        return fragmentListenerLists;
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        Log.d("repo response",jsonObject.toString());
        try {
            JSONArray repoDetailArray = jsonObject.getJSONArray("details");
            JSONObject repoDetails = repoDetailArray.getJSONObject(0);
            Repo temp = Session.selectedRepo;
            temp.isRepoPersonal=repoDetails.getBoolean("isRepoPersonal");
            temp.tags = repoDetails.getJSONArray("tags");
            temp.branchPointers=repoDetails.getJSONArray("branchPointers");
            temp.collaborators = repoDetails.getJSONArray("collaborators");
            JSONArray members = repoDetails.getJSONArray("members");
            temp.membersJSONArray=members;
            Session.selectedRepo=temp;
            AsyncCommunicationTask commFiles= new AsyncCommunicationTask(Constants.getFileListURL, RepoPostDatas.getRepoFileListPostData(Session.selectedRepo.repoID, Session.user.userID), new Communicator() {
                @Override
                public void successfulExecute(JSONObject jsonObject) {
                    Log.d("Repo Files response",jsonObject.toString());
                    try {
                        JSONArray filesJSONArray = jsonObject.getJSONArray("details");
                        ArrayList<LCFile> repoFiles = new ArrayList<>();
                        for(int i=0;i<filesJSONArray.length();i++){
                            JSONObject fileJSONObject = filesJSONArray.getJSONObject(i);
                            LCFile file = LCFile.newLCFile(fileJSONObject,null);
                            if(file!=null){
                                repoFiles.add(file);
                            }
                        }
                        Session.selectedRepo.files=repoFiles;
                        Session.selectedRepo.currentSha=jsonObject.getString("head");
                        Session.selectedRepo.currentBranch =jsonObject.getString("branch");
                        setTabView();
                        //reverseAllFiles();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failedExecute() {

                }
            });
            commFiles.execute((Void)null);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void failedExecute() {

    }
}
