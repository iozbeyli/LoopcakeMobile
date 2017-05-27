package com.loopcake.loopcakemobile.AsyncCommunication;

import android.util.Log;
import android.widget.Toast;

import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.PostDatas.CoursePostDatas;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.Session;
import com.loopcake.loopcakemobile.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MEHMET on 27.05.2017.
 */

public class NotificationHandler {
    private final static String TAG = "NotificationHandler";

    public  static void sendNotificationToUser(String[] userids, String title, String detail){
        JSONObject postData = new JSONObject();
        try {
            postData.put("sub", title);
            postData.put("receiver", new JSONArray(userids));
            postData.put("text", detail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        send(postData);
    }

    public static void sendNotificationToCourse(final String courseid, final String title, final String detail){
        JSONObject postData = new JSONObject();
        try {
            postData.put("courseid", courseid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getStudentURL, postData, new Communicator() {
            @Override
            public void successfulExecute(JSONObject jsonObject) {
                ArrayList<String> students = new ArrayList<>();
                try {
                    JSONArray details = jsonObject.getJSONArray("details");
                    for(int i=0;i<details.length();i++){
                        String id = details.getJSONObject(i).getString("_id");
                        students.add(id);
                    }

                    if(students.isEmpty()){
                        return;
                    }

                    JSONObject postData = new JSONObject();
                    postData.put("sub", title);
                    postData.put("receiver", new JSONArray(students));
                    postData.put("text", detail);
                    send(postData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failedExecute() {

            }
        });
        asyncCommunicationTask.execute((Void)null);

    }

    private static void send(JSONObject postData){
        AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.notificationURL,postData, new Communicator() {
            @Override
            public void successfulExecute(JSONObject jsonObject) {
            }

            @Override
            public void failedExecute() {
                Log.wtf(TAG, "Notification Failed");
            }
        });
        task.execute((Void)null);
    }
}
