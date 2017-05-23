package com.loopcake.loopcakemobile.PostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Melih on 23.05.2017.
 */
public class RepoPostDatas{
    public static JSONObject getRepoListPostData(){
        JSONObject reqData = new JSONObject();
        try {
            reqData.put("operation","1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  reqData;
    }
    public static JSONObject getRepoDetailsPostData(String repoID){
        JSONObject reqData = new JSONObject();
        try {
            reqData.put("operation","2");
            reqData.put("id",repoID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  reqData;
    }
    public static JSONObject getRepoMembersPostData(JSONArray members){
        JSONObject reqData = new JSONObject();
        try {
            reqData.put("operation","2");
            reqData.put("users",members);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  reqData;
    }
}