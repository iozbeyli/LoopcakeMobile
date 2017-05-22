package com.loopcake.loopcakemobile;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.Course;
import com.loopcake.loopcakemobile.PostDatas.CoursePostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseListFragment extends LCListFragment<Course> implements Communicator{

    public CourseListFragment() {
    }

    @Override
    protected void fillList() {
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getCourseURL, CoursePostDatas.getCoursePostData(),this);
        asyncCommunicationTask.execute((Void) null);
    }

    @Override
    public void listItemPressed(Course listItem) {
        Log.d("item","clicked");
        Session.selectedID = listItem.details;
        Session.selectedCourse=listItem;
        Intent intent = new Intent(getActivity(),CourseActivity.class);
        startActivity(intent);
    }

    @Override
    public void setItemContent(Course item, View itemView) {
        TextView content =(TextView) itemView.findViewById(R.id.content);
        content.setText(item.title);
    }

    public void fillCourseList(JSONArray courses){
        ArrayList<Course> courseList = new ArrayList<Course>();
        for(int i=0;i<courses.length();i++){
            JSONObject course = null;
            try {
                course = courses.getJSONObject(i);
                String courseTitle = course.getString("name");
                String courseID = course.getString("_id");
                courseList.add(new Course(""+i,courseTitle,courseID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        displayList(courseList,R.layout.fragment_item);
    }

    public void successfulExecute(JSONObject jsonObject){
        Log.d("Communicator","Succesfully Execute");
        Boolean successBool = null;
        try {
            successBool = (Boolean)jsonObject.get("success");
            if(successBool){
            JSONArray details = jsonObject.getJSONArray("details");
            fillCourseList(details);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void failedExecute(){

    }


}
