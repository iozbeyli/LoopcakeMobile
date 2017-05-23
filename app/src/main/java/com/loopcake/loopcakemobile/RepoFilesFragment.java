package com.loopcake.loopcakemobile;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.LCExpandableList.Announcement;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;
import com.loopcake.loopcakemobile.RepoFragments.LCFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class RepoFilesFragment extends LCFragment implements Communicator {

    ArrayList<LCFile> files;
    Drawable file;
    Drawable folder;

    public RepoFilesFragment(){
        layoutID = R.layout.fragment_repo_files;
    }

    public static RepoFilesFragment newInstance(ArrayList<LCFile> files){
        RepoFilesFragment fragment = new RepoFilesFragment();
        fragment.files=files;
        return  fragment;
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
            displayFiles(repoFiles,30);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }

    @Override
    public void mainFunction() {
        folder = ContextCompat.getDrawable(getActivity(),R.drawable.ic_stat_folder);
        file=ContextCompat.getDrawable(getActivity(),R.drawable.ic_stat_file);
        folder.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        file.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        if(files==null){
            AsyncCommunicationTask commFiles= new AsyncCommunicationTask(Constants.getFileListURL,RepoPostDatas.getRepoFileListPostData(Session.selectedRepo.repoID,Session.user.userID),this);
            commFiles.execute((Void)null);
        }

    }

    public void addFrameLayout(){
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.repo_file_child_item, null);

// fill in any details dynamically here

// insert into main view
        ViewGroup insertPoint = (ViewGroup) layout.findViewById(R.id.repo_file_fragment_layout);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
    }

    public void displayFiles(ArrayList<LCFile> files,int leftPadding){
        for(int i = 0;i<files.size();i++){
            displayFile(files.get(i),leftPadding);
        }
    }

    public void displayFile(LCFile item,int leftPadding){
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.repo_file_child_item, null);

        TextView textView = (TextView) v.findViewById(R.id.repo_file_child_text);
        textView.setText(item.name);
        v.setPadding(leftPadding,0,0,0);
        ImageView iv = (ImageView) v.findViewById(R.id.repo_file_child_image);
        switch (item.fileType){
            case FILE:
                iv.setImageDrawable(file);
                break;
            case FOLDER:
                iv.setImageDrawable(folder);
                break;
        }


// insert into main view
        ViewGroup insertPoint = (ViewGroup) layout.findViewById(R.id.repo_file_fragment_layout);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        if(item.children!=null){
            displayFiles(item.children,leftPadding+30);
        }
    }
}
