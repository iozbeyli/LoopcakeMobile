package com.loopcake.loopcakemobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.AsyncCommunication.ImageDownloaderTask;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.PostDatas.CoursePostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;

/**
 * Created by MEHMET on 27.05.2017.
 */

public class GroupMemberFragment extends LCListFragment<User> implements Communicator {
    private final static String TAG = "GroupMember";

    @Override
    public void setLayoutID() {
        layoutID = R.layout.fragment_group_member;
    }

    @Override
    protected void fillList() {
        JSONObject post = new JSONObject();
        try {
            post.put("operation","2");
            post.put("users",Session.selectedGroup.getJSONArray("students"));
            AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.apiURL+"/user", post,this);
            asyncCommunicationTask.execute((Void)null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button but = (Button) layout.findViewById(R.id.add_member);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                TextView title = new TextView(getContext());
                title.setText("Write Student E-Mails");
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Constants.colorPrimary);
                title.setTypeface(null, BOLD);
                title.setPadding(50,50,50,50);
                title.setTextSize(20);

                builder.setCustomTitle(title);
                final EditText input = new EditText(getContext());
                input.setHint("Seperate with , for multiple students");
                builder.setView(input);
                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        JSONObject post = new JSONObject();
                        try {
                            JSONArray students = Session.selectedGroup.getJSONArray("students");
                            String[] selected = input.getText().toString().split(",");
                            int max = Session.project.maxSize;
                            if((students.length()+selected.length)>max){
                                Snackbar.make(layout,"Max group size is "+max, Snackbar.LENGTH_SHORT).show();
                                return;
                            }

                            post.put("students", new JSONArray(selected));
                            //post.put("maxGroupSize", max);
                            post.put("groupid",Session.selectedGroup.getString("_id"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "/addMember",
                                post, new Communicator() {
                            @Override
                            public void successfulExecute(JSONObject jsonObject) {
                                Intent intent = new Intent(getActivity(),ProjectActivity.class);
                                startActivity(intent);
                            }
                            @Override
                            public void failedExecute() {

                                Log.wtf(TAG, "failedExecution");
                            }
                        });
                        task.execute((Void) null);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        Button leave = (Button) layout.findViewById(R.id.leave);
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject post = new JSONObject();
                try {
                    post.put("groupid",Session.selectedGroup.getString("_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "/leaveGroup", post, new Communicator() {
                    @Override
                    public void successfulExecute(JSONObject jsonObject) {
                        Intent intent = new Intent(getActivity(),ProjectActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void failedExecute() {
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });
                task.execute((Void)null);
            }
        });

    }

    @Override
    public void listItemPressed(User listItem, View itemView) {

    }

    @Override
    public void setItemContent(User item, View itemView) {
        TextView content = (TextView) itemView.findViewById(R.id.content);
        content.setText(item.name+" "+item.surname);
        ImageView iv = (ImageView)itemView.findViewById(R.id.user_photo);
        ImageDownloaderTask task = new ImageDownloaderTask(iv, item.photoID, layout.getContext());
        task.execute((String[]) null);
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        ArrayList<User> students = new ArrayList<>();
        try {
            JSONArray details = jsonObject.getJSONArray("details");
            for(int i=0;i<details.length();i++){
                JSONObject student = details.getJSONObject(i);
                Log.d(TAG, "successfulExecute: "+student);
                String name = student.getString("name");
                String surname = student.getString("surname");
                String id = student.getString("_id");
                String photo = student.getString("photo");
                User studentObject = new User();
                studentObject.name=name;
                studentObject.surname=surname;
                studentObject.userID = id;
                studentObject.photoID = photo;
                students.add(studentObject);
            }
            displayList(students,R.layout.user_item);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }
}
