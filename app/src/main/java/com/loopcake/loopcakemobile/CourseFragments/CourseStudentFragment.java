package com.loopcake.loopcakemobile.CourseFragments;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.PostDatas.CoursePostDatas;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseStudentFragment extends LCListFragment<User> implements Communicator{

    public CourseStudentFragment() {
        // Required empty public constructor
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        ArrayList<User> students = new ArrayList<>();
        try {
            JSONArray details = jsonObject.getJSONArray("details");
            for(int i=0;i<details.length();i++){
                JSONObject student = details.getJSONObject(i);
                String name = student.getString("name");
                String surname = student.getString("surname");
                String id = student.getString("_id");
                User studentObject = new User();
                studentObject.name=name;
                studentObject.surname=surname;
                studentObject.userID = id;
                Log.d("name",name+" "+surname);
                Log.d("id",id);
                students.add(studentObject);
            }
            displayList(students,R.layout.fragment_item);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failedExecute() {

    }

    @Override
    protected void fillList() {
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getStudentURL, CoursePostDatas.getCourseStudentPostData(),this);
        asyncCommunicationTask.execute((Void)null);
    }

    @Override
    public void listItemPressed(User listItem) {

    }

    @Override
    public void setItemContent(User item, View itemView) {
        TextView content = (TextView) itemView.findViewById(R.id.content);
        content.setText(item.name+" "+item.surname);
    }

}
