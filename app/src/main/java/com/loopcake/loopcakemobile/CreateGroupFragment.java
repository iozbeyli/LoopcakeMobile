package com.loopcake.loopcakemobile;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.AsyncCommunication.ImageDownloaderTask;
import com.loopcake.loopcakemobile.AsyncCommunication.NotificationHandler;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
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
    private static final String TAG = "CreateGroup";

    @Override
    public void setLayoutID() {
        layoutID = R.layout.fragment_create_group;
    }

    @Override
    protected void fillList() {
        try {
            JSONObject post = new JSONObject();
            post.put("courseid", Session.project.course.courseid);
            post.put("projectid", Session.project.id);
            AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL+"/getNonGroup",post,this);
            task.execute((Void)null);
            final EditText editName = (EditText) layout.findViewById(R.id.edit_group_name);
            ((TextView)layout.findViewById(R.id.label_max_size)).setText("Select group members. (Max group size is "+Session.project.maxSize+")");
            ((Button)layout.findViewById(R.id.create_group)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject post = new JSONObject();
                    try {
                        post.put("courseid", Session.project.course.courseid);
                        post.put("projectid", Session.project.id);
                        post.put("name", editName.getText());
                        post.put("students", new JSONArray(selectedIDs));
                        post.put("deadline", Session.project.deadline);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "/addGroup", post, new Communicator() {
                        @Override
                        public void successfulExecute(JSONObject jsonObject) {
                            Toast.makeText(getContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                            Session.selectedID = Session.project.id;
                            NotificationHandler.sendNotificationToUser(selectedIDs.toArray(new String[selectedIDs.size()]), "New Group", "You have been added to a group name "+ editName.getText());
                            Intent intent = new Intent(getActivity(),ProjectActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void failedExecute() {

                        }
                    });
                    task.execute((Void)null);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createGroup(){
        try {
            JSONObject post = new JSONObject();
            post.put("courseid", Session.project.course.courseid);
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
        Switch check = (Switch)itemView.findViewById(R.id.user_switch);
        check.setText(item.name+" "+item.surname);
        if(item.id.equals(Session.user.userID)){
            check.setChecked(true);
            check.setClickable(false);
            selectedIDs.add(Session.user.userID);
        }else{
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

        ImageView iv = (ImageView)itemView.findViewById(R.id.user_photo);
        ImageDownloaderTask task = new ImageDownloaderTask(iv, item.photo, layout.getContext());
        task.execute((String[]) null);
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
                    Log.d(TAG, "successfulExecute: "+student);
                    String name = student.getString("name");
                    String surname = student.getString("surname");
                    String id = student.getString("_id");
                    String photo = student.getString("photo");
                    students.add(new StudentSelect(name,surname,id, photo));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                displayList(students,R.layout.user_switch_item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }
}
