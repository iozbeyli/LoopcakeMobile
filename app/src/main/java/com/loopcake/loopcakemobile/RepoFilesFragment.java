package com.loopcake.loopcakemobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;
import com.loopcake.loopcakemobile.RepoFragments.LCFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RepoFilesFragment extends LCFragment implements Communicator {

    ArrayList<LCFile> files;
    Drawable file;
    Drawable folder;
    ArrayList<View> views;
    LinearLayout insertPoint;
    private static class RepoFileView{
        public ArrayList<RepoFileView> subViews;
        public View view;
        public LCFile file;
        public RepoFileView(){

        }
        public void addSubView(View toAdd,RelativeLayout layoutToAdd){
            //RelativeLayout rLayout = (RelativeLayout)findViewById(R.id.yourRelativeId);
            //rLParams.addRule(RelativeLayout.BELOW, viewtoBeBelow.getId());
        }

    }

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
                LCFile file = LCFile.newLCFile(fileJSONObject,null);
                if(file!=null){
                    repoFiles.add(file);
                }
            }
            Session.selectedRepo.files=repoFiles;
            displayFiles(repoFiles,0);
            reverseAllFiles();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }

    @Override
    public void mainFunction() {
        insertPoint = (LinearLayout) layout.findViewById(R.id.repo_file_fragment_layout);
        views = new ArrayList<>();
        folder = ContextCompat.getDrawable(getActivity(),R.drawable.ic_stat_folder);
        file=ContextCompat.getDrawable(getActivity(),R.drawable.ic_stat_file);
        folder.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        file.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        if(files==null){
            AsyncCommunicationTask commFiles= new AsyncCommunicationTask(Constants.getFileListURL,RepoPostDatas.getRepoFileListPostData(Session.selectedRepo.repoID,Session.user.userID),this);
            commFiles.execute((Void)null);
        }

    }


    public void displayFiles(ArrayList<LCFile> files,int leftPadding){
        for(int i = 0;i<files.size();i++){
            displayFile(files.get(i),leftPadding);
        }
    }

    public void clickOnFile(final LCFile item){
        if(item.fileType== Enumerators.FileType.FILE){
            AsyncCommunicationTask fileContentComm = new AsyncCommunicationTask(Constants.getFileContentURL, RepoPostDatas.getRepoFileContentPostData(Session.selectedRepo.repoID, Session.user.userID, item.path), new Communicator() {
                @Override
                public void successfulExecute(JSONObject jsonObject) {
                    Log.d("code",jsonObject.toString());
                    try {
                        JSONObject fileDetails = jsonObject.getJSONObject("details");
                        item.code = fileDetails.getString("data");
                        Session.selectedFile = item;
                        Intent in = new Intent(getActivity(), SubRepoActivity.class);
                        startActivity(in);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failedExecute() {

                }
            });
            fileContentComm.execute((Void)null);
        }

    }

    public void displayFile(final LCFile item, int leftPadding){
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.repo_file_child_item, null);
        RepoFileView repoFileView = new RepoFileView();
        repoFileView.view = v;

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
        views.add(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOnFile(item);
            }
        });
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        insertPoint.addView(v);
        if(item.children!=null){
            displayFiles(item.children,leftPadding+100);
        }
    }

    public void reverseAllFiles(){
        insertPoint.removeAllViews();
        for(int x = 0; x < views.size(); x++) {
            insertPoint.addView(views.get(x));
        }
    }
}
