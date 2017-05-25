package com.loopcake.loopcakemobile;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.ChecklistItem;
import com.loopcake.loopcakemobile.LCList.LCListItems.Course;
import com.loopcake.loopcakemobile.ListContents.StudentSelect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MEHMET on 25.05.2017.
 */

public class CreateGroupFragment extends LCListFragment<StudentSelect> implements Communicator{
    private ArrayList<String> selectedIDs = new ArrayList<>();
    
    @Override
    protected void fillList() {
        try {
            JSONObject post = new JSONObject();
            post.put("courseid", Session.project.course);
            post.put("projectid", Session.project.id);
            AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL+"/getNonGroup",post,this);
            task.execute((Void)null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createGroup(){
        try {
            JSONObject post = new JSONObject();
            post.put("courseid", Session.project.course);
            post.put("projectid", Session.project.id);
            post.put("name", "myGroup");
            post.put("maxGroupSize", Session.project.maxSize);
            post.put("deadline", Session.project.deadline);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void listItemPressed(StudentSelect listItem, View itemView) {
    }

    @Override
    public void setItemContent(final StudentSelect item, View itemView) {
        Switch check = (Switch)itemView.findViewById(R.id.checklist_switch);
        check.setText(item.name+" "+item.surname);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(selectedIDs.size()<Session.project.maxSize){
                        selectedIDs.add(item.id);
                    }else{
                        Snackbar.make(layout, "Max group size is "+Session.project.maxSize,Snackbar.LENGTH_LONG).show();
                        buttonView.setChecked(false);
                    }
                }else{
                    selectedIDs.remove(item.id);
                }
            }
        });
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        try {
            JSONArray array = jsonObject.getJSONArray("details");
            ArrayList<StudentSelect> students = new ArrayList<>();
            for(int i=0;i<array.length();i++){
                JSONObject student = null;
                try {
                    student = array.getJSONObject(i);
                    String name = student.getString("name");
                    String surname = student.getString("surname");
                    String id = student.getString("_id");
                    students.add(new StudentSelect(name,surname,id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayList(students,R.layout.fragment_project_checklist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }
}
