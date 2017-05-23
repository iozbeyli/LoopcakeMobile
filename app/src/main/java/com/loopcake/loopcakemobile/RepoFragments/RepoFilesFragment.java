package com.loopcake.loopcakemobile.RepoFragments;

import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.LCExpandableList.LCExpandableAdapter;
import com.loopcake.loopcakemobile.LCExpandableList.LCExpandableFragment;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Melih on 23.05.2017.
 */

public class RepoFilesFragment extends LCExpandableFragment<LCFile> implements Communicator{
    public RepoFilesFragment(){

    }

    @Override
    public void fillList() {
        AsyncCommunicationTask repoFilesComm = new AsyncCommunicationTask(Constants.getFileListURL, RepoPostDatas.getRepoFileListPostData(Session.selectedRepo.repoID,Session.user.userID),this);
        repoFilesComm.execute((Void)null);

    }

    @Override
    public void setGroupView(View groupView, LCFile item) {
        Log.d("Group item",item.name);
        TextView nameText = (TextView) groupView.findViewById(R.id.repo_file_group_text);
        nameText.setText(item.name);
    }

    @Override
    public void setChildView(View childView, LCFile item, int childPosition) {
        Log.d("Child item",item.name);
        final LCFile childItem = item.children.get(childPosition);
        TextView childNameText = (TextView) childView.findViewById(R.id.repo_file_child_text);
        childNameText.setText(childItem.name);
        ExpandableListView childExpandable = (ExpandableListView) childView.findViewById(R.id.repo_file_child_list);
        LCExpandableAdapter childExpAdt = new LCExpandableAdapter(getActivity(),this,childItem.children,R.layout.repo_file_group_item,R.layout.repo_file_child_item);
        childExpandable.setAdapter(childExpAdt);
        childExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                onChildClicked(childItem.children.get(groupPosition));
                return true;
            }
        });
        childExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                onGroupClicked(childItem.children.get(groupPosition));
                return false;
            }
        });
    }

    @Override
    public void onGroupClicked(LCFile item) {

    }

    @Override
    public int getChildrenCount(LCFile item) {
        if(item.children==null){
            return 0;
        }else{
            return item.children.size();
        }
    }

    @Override
    public void onChildClicked(LCFile item) {

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
            items = repoFiles;
            displayList(R.layout.repo_file_group_item,R.layout.repo_file_child_item);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failedExecute() {

    }
}
