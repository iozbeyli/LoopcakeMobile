package com.loopcake.loopcakemobile.PostDatas;

import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.Session;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Melih on 22.05.2017.
 */

public class AnnouncementPostDatas{
    public static JSONObject getAnnouncementPostData(Enumerators.AnnouncementType type){
        JSONObject postData=new JSONObject();
        try {
            switch (type){
                case STUDENT:
                    postData.put("operation","2");
                    break;
                case COURSE:
                    postData.put("course", Session.selectedID);
                    postData.put("operation","1");
                    break;
                case INSTRUCTOR:
                    postData.put("operation","2");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }
}
