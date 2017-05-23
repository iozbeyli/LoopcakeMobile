package com.loopcake.loopcakemobile;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;
import com.loopcake.loopcakemobile.RepoFragments.LCFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RepoFilesFragment extends LCListFragment<LCFile> implements Communicator {

    private ArrayList<LCFile> files;
    public RepoFilesFragment(){

    }

    public static RepoFilesFragment newInstance(ArrayList<LCFile> files){
        RepoFilesFragment fragment = new RepoFilesFragment();
        fragment.files=files;
        return fragment;
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
            Log.d("Repo Files response",jsonObject.toString());
            try {
                JSONArray filesJSONArray = jsonObject.getJSONArray("details");
                ArrayList<LCFile> repoFiles = new ArrayList<>();
                for(int i=0;i<filesJSONArray.length();i++){
                    JSONObject fileJSONObject = filesJSONArray.getJSONObject(i);
                    LCFile file = LCFile.newLCFile(fileJSONObject);
                    if(file!=null){
                        repoFiles.add(file);
                    }
                }
                Session.selectedRepo.files=repoFiles;
                displayList(repoFiles,R.layout.fragment_repo_files);
            } catch (JSONException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void failedExecute() {

    }

    @Override
    protected void fillList() {
        if(files==null){
            AsyncCommunicationTask commFiles = new AsyncCommunicationTask(Constants.getFileListURL, RepoPostDatas.getRepoFileListPostData(Session.selectedRepo.repoID,Session.user.userID),this);
            commFiles.execute((Void)null);
        }else{

        }
    }

    @Override
    public void listItemPressed(LCFile listItem) {

    }

    @Override
    public void setItemContent(LCFile item, View itemView) {
        TextView textView = (TextView)itemView.findViewById(R.id.repo_file_name);
        textView.setText(item.name);
    }
}
