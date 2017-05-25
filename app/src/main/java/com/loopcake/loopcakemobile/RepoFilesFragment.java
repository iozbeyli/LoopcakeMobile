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


public class RepoFilesFragment extends LCFragment{

    ArrayList<LCFile> files;
    Drawable file;
    Drawable folder;
    ArrayList<View> views;
    LinearLayout insertPoint;
    private class RepoFileView{
        public ArrayList<RepoFileView> subViews;
        public View view;
        public LCFile file;
        private boolean isOpen=false;
        public RepoFileView(){

        }
        public void displaySubViews(){
            if(isOpen){
                ((LinearLayout) view.findViewById(R.id.repo_file_child_layout)).removeAllViews();
            }else{
                RepoFilesFragment.this.displayFiles(file.children,100,(LinearLayout) view.findViewById(R.id.repo_file_child_layout));
            }
            isOpen=!isOpen;
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
    public void mainFunction() {
        insertPoint = (LinearLayout) layout.findViewById(R.id.repo_file_fragment_layout);
        views = new ArrayList<>();
        folder = ContextCompat.getDrawable(getActivity(),R.drawable.ic_stat_folder);
        file=ContextCompat.getDrawable(getActivity(),R.drawable.ic_stat_file);
        folder.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        file.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        if(Session.selectedRepo.files!=null){
            displayFiles(Session.selectedRepo.files,0,insertPoint);
        }


    }


    public void displayFiles(ArrayList<LCFile> files,int leftPadding,LinearLayout linearLayout){
        for(int i = 0;i<files.size();i++){
            displayFile(files.get(i),leftPadding,linearLayout);
        }
    }

    public void clickOnFile(final RepoFileView item){
        if(item.file.fileType== Enumerators.FileType.FILE){
            AsyncCommunicationTask fileContentComm = new AsyncCommunicationTask(Constants.getFileContentURL, RepoPostDatas.getRepoFileContentPostData(Session.selectedRepo.repoID, Session.user.userID, item.file.path), new Communicator() {
                @Override
                public void successfulExecute(JSONObject jsonObject) {
                    Log.d("code",jsonObject.toString());
                    try {
                        JSONObject fileDetails = jsonObject.getJSONObject("details");
                        item.file.code = fileDetails.getString("data");
                        Session.selectedFile = item.file;
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
        }else{
            item.displaySubViews();
        }

    }

    public void displayFile(final LCFile item, int leftPadding,LinearLayout linearLayout){
        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.repo_file_child_item, null);
        final RepoFileView repoFileView = new RepoFileView();
        repoFileView.view = v;
        repoFileView.file = item;
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
                clickOnFile(repoFileView);
            }
        });
        v.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(v);
    }


}
