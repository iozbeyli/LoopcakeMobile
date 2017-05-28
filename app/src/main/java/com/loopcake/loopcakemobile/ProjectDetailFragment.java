package com.loopcake.loopcakemobile;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.AsyncCommunication.ImageDownloaderTask;
import com.loopcake.loopcakemobile.LCExpandableList.Project;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.Course;

import org.json.JSONException;
import org.json.JSONObject;



public class ProjectDetailFragment extends LCFragment {
    public static final String TAG = "ProjectDeatil";
    public ProjectDetailFragment() {
        // Required empty public constructor
        layoutID = R.layout.fragment_project_detail;
    }

    @Override
    public void mainFunction() {
        JSONObject jsonObject = null;
        try {
            jsonObject = Session.selectedGroup;
            Project project =Session.project;
            TextView project_title = (TextView) layout.findViewById(R.id.label_project_title);
            TextView project_detail = (TextView) layout.findViewById(R.id.label_project_details);
            TextView project_attach = (TextView) layout.findViewById(R.id.label_project_attachments);
            TextView project_deadline = (TextView) layout.findViewById(R.id.label_project_deadline);
            TextView course = (TextView) layout.findViewById(R.id.label_course);
            TextView group_name = (TextView) layout.findViewById(R.id.label_group_name);
            TextView group_details = (TextView) layout.findViewById(R.id.label_group_details);

            project_title.setText(project.title);
            project_detail.append(project.details);

            if(project.attachments.length()>0)
                project_attach.setText(project.attachments.toString());
            else
                project_attach.setVisibility(View.GONE);

            project_deadline.append(project.deadline);
            course.setText(project.course.name+" "+project.course.code);
            setInstructorCard();
            group_name.append(jsonObject.getString("name"));
            group_details.setText(jsonObject.getString("details"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setInstructorCard() {
        final TextView ins_name = (TextView) layout.findViewById(R.id.content);
        final ImageView ins_photo = (ImageView) layout.findViewById(R.id.user_photo);
        JSONObject post = new JSONObject();
        try {
            post.put("users", Session.project.course.instructor);
            post.put("operation","2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL+"/user",post, new Communicator() {
            @Override
            public void successfulExecute(JSONObject jsonObject) {
                Log.d(TAG, "successfulExecute: "+jsonObject);
                try {
                    JSONObject details = jsonObject.getJSONArray("details").getJSONObject(0);
                    String name = details.getString("name");
                    String surname = details.getString("surname");
                    String photo = details.getString("photo");
                    ins_name.setText(name+" "+surname);
                    ImageDownloaderTask task = new ImageDownloaderTask(ins_photo,photo,getContext());
                    task.execute((String[]) null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failedExecute() {
                Log.d(TAG, "failedExecute: ");
            }
        });
        task.execute((Void) null);
    }

}
