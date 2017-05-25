package com.loopcake.loopcakemobile.PostDatas;

import android.util.Log;

import com.loopcake.loopcakemobile.Session;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Melih on 22.05.2017.
 */

public class GroupPostDatas {
    public static JSONObject getGroupPostData(){
        JSONObject reqData = new JSONObject();
        try {
            reqData.put("operation","4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  reqData;
    }
    public static JSONObject getGroupDetailsPostData(){
        JSONObject postData = new JSONObject();
        try {
            postData.put("operation","3");
            postData.put("projectid", Session.selectedID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }
}
