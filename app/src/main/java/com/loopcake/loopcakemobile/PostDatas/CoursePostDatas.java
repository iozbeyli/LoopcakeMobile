package com.loopcake.loopcakemobile.PostDatas;

import com.loopcake.loopcakemobile.LCList.LCListItems.Course;
import com.loopcake.loopcakemobile.Session;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Melih on 22.05.2017.
 */

public class CoursePostDatas {
    public static JSONObject getCoursePostData(){
        JSONObject reqData = new JSONObject();
        try {
            reqData.put("operation","3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  reqData;
    }
    public static JSONObject getCourseStudentPostData(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("courseid", Session.selectedID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  postData;
    }

    public static JSONObject getCoursePostData(Course course){
        JSONObject reqData = new JSONObject();
        try {
            reqData.put("name",course.name);
            reqData.put("code",course.code);
            reqData.put("details",course.details);
            reqData.put("langs",course.langs);
            reqData.put("courseid",course.courseid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  reqData;
    }
}
