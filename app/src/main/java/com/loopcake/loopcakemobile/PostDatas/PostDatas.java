package com.loopcake.loopcakemobile.PostDatas;

import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.Session;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Melih on 20.05.2017.
 */

public class PostDatas {

    public static class ProjectPostDatas{
        public static JSONObject getGroupPostData(){
            JSONObject reqData = new JSONObject();
            try {
                reqData.put("operation","4");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  reqData;
        }
    }
    public static class RepoPostDatas{
        public static JSONObject getRepoPostData(){
            JSONObject reqData = new JSONObject();
            try {
                reqData.put("operation","1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return  reqData;
        }
    }

}
