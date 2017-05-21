package com.loopcake.loopcakemobile.PostDatas;

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
}
