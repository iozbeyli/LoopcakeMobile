package com.loopcake.loopcakemobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCExpandableList.Announcement;
import com.loopcake.loopcakemobile.LCExpandableList.LCExpandableFragment;
import com.loopcake.loopcakemobile.LCExpandableList.Project;
import com.loopcake.loopcakemobile.PostDatas.PostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ProjectListFragment extends LCExpandableFragment<Project> implements Communicator{

    private AsyncCommunicationTask mAuthTask = null;

    public ProjectListFragment() {
        // Required empty public constructor
    }

    @Override
    public void fillList() {
        mAuthTask = new AsyncCommunicationTask(Constants.getGroupURL, PostDatas.ProjectPostDatas.getGroupPostData(),this);
        mAuthTask.execute((Void) null);
    }

    @Override
    public void setGroupView(View groupView, Project item) {
        TextView title = (TextView)groupView.findViewById(R.id.project_title);
        TextView deadline = (TextView) groupView.findViewById(R.id.project_deadline);
        title.setTypeface(null, Typeface.BOLD);
        title.setText(item.title);
        deadline.setText(item.deadline);
    }

    @Override
    public void setChildView(View childView, Project item, int childPosition) {
        ProgressBar progressBar = (ProgressBar)childView.findViewById(R.id.project_progress_bar);
        TextView projectDetail = (TextView)childView.findViewById(R.id.project_content);
        progressBar.setProgress(item.getPoints());
        projectDetail.setText(item.title);
    }

    @Override
    public void onGroupClicked(Project item) {

    }

    @Override
    public int getChildrenCount(Project item) {
        return 1;
    }

    @Override
    public void onChildClicked(Project item) {
        showProject(item.id);
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        try {
            JSONArray announcements = jsonObject.getJSONArray("details");
            Boolean successBool = (Boolean)jsonObject.get("success");
            Log.d("Project List result",jsonObject.toString());
            if(successBool){
                createProjectList(announcements);
                displayList(R.layout.project_group_item,R.layout.project_child_item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }


    private void showProject(String id) {
        Session.selectedID = id;
        Intent intent = new Intent(getActivity(),ProjectActivity.class);
        startActivity(intent);
    }


    private void createProjectList(JSONArray announcements) {
        items=new ArrayList<>();
        for(int i=0;i<announcements.length();i++){
            try {
                JSONArray checklist = announcements.getJSONObject(i).getJSONArray("checklist");
                String name= announcements.getJSONObject(i).getJSONObject("project").getString("name");
                String deadline = announcements.getJSONObject(i).getJSONObject("project").getString("deadline");
                String id =announcements.getJSONObject(i).getString("_id");
                String course= announcements.getJSONObject(i).getJSONObject("course").getString("name");
                HashMap<String,Pair<Boolean,Integer>> checklistDict=new HashMap<>();
                for (int j = 0; j < checklist.length(); j++) {
                    boolean status = checklist.getJSONObject(j).getBoolean("status");
                    int point = checklist.getJSONObject(j).getInt("point");
                    Pair<Boolean,Integer> tempPair = new Pair<>(status,point);
                    checklistDict.put(""+j,tempPair);
                }
                //progressList.add(temp);
                Project tempProject = new Project(id,name,deadline,checklistDict,course);
                items.add(tempProject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
