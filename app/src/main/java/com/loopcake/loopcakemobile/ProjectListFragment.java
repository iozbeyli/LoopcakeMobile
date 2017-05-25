package com.loopcake.loopcakemobile;

import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCExpandableList.Announcement;
import com.loopcake.loopcakemobile.LCExpandableList.LCExpandableFragment;
import com.loopcake.loopcakemobile.LCExpandableList.Project;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.PostDatas.CoursePostDatas;
import com.loopcake.loopcakemobile.PostDatas.PostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class ProjectListFragment extends LCListFragment<Project> implements Communicator{

    private final static String TAG = "ProjectList";
    private AsyncCommunicationTask mAuthTask = null;
    private ArrayList<Project> items = new ArrayList<>();

    public ProjectListFragment() {
        // Required empty public constructor
    }

    @Override
    public void setLayoutID() {

    }

    @Override
    public void fillList() {
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getCourseURL, CoursePostDatas.getCoursePostData(),this);
        asyncCommunicationTask.execute((Void) null);
    }

    @Override
    public void listItemPressed(final Project listItem,final View itemView) {
        try {
            JSONObject post = new JSONObject();
            post.put("operation","2");
            post.put("projectid",listItem.id);
            AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "/getProject", post, new Communicator() {
                @Override
                public void successfulExecute(JSONObject jsonObject) {
                    try {
                        listItem.deadline = jsonObject.getJSONArray("details").getJSONObject(0).getString("deadline");
                        listItem.maxSize = jsonObject.getJSONArray("details").getJSONObject(0).getInt("maxGroupSize");
                        Log.d(TAG, "successfulExecute: "+jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Session.project = listItem;
                    showProject(listItem.id);
                }

                @Override
                public void failedExecute() {
                    Toast.makeText(getContext(),"press failed",Toast.LENGTH_LONG).show();

                }
            });
            task.execute((Void)null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setItemContent(Project item, View itemView) {
        ((TextView)itemView.findViewById(R.id.content)).setText(item.title);
    }

    /*@Override
    public void setGroupView(View groupView, Project item) {
        TextView title = (TextView)groupView.findViewById(R.id.project_title);
        //TextView deadline = (TextView) groupView.findViewById(R.id.project_deadline);
        title.setTypeface(null, Typeface.BOLD);
        title.setText(item.title);
        //deadline.setText(item.deadline);
    }

    @Override
    public void setChildView(View childView, Project item, int childPosition) {
        //ProgressBar progressBar = (ProgressBar)childView.findViewById(R.id.project_progress_bar);
        TextView projectDetail = (TextView)childView.findViewById(R.id.project_content);
        //progressBar.setProgress(item.getPoints());
        projectDetail.setText(item.title);
    }

    @Override
    public void onGroupClicked(Project item) {

    }

    @Override
    public int getChildrenCount(Project item) {
        return 1;
    }

    @Override
    public void onChildClicked(Project item) {
        showProject(item.id);
    }*/

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        Boolean successBool = null;
        try {
            successBool = (Boolean)jsonObject.get("success");
            JSONObject post = new JSONObject();
            post.put("operation", "1");
            if(successBool){
                JSONArray details = jsonObject.getJSONArray("details");
                final ArrayList<Project> projects = new ArrayList<Project>();
                for(int i=0;i<details.length();i++){
                    final String courseID = details.getJSONObject(i).getString("_id");
                    post.put("courseid",courseID);
                    AsyncCommunicationTask async = new AsyncCommunicationTask(Constants.apiURL + "/getProject",
                            post, new Communicator() {
                        @Override
                        public void successfulExecute(JSONObject jsonObject) {
                            JSONArray announcements = null;
                            try {
                                announcements = jsonObject.getJSONArray("details");
                                Boolean successBool = (Boolean)jsonObject.get("success");
                                if(successBool) {
                                    projects.addAll(createProjectList(announcements,courseID));
                                    items = projects;
                                    displayList(items,R.layout.fragment_item);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failedExecute() {

                        }
                    });
                    async.execute((Void) null).get();
                }

                //items = projects;

                //displayList(R.layout.project_group_item,R.layout.project_child_item);

            }
        }catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }


    private void showProject(String id) {
        Session.selectedID = id;
        Intent intent = new Intent(getActivity(),ProjectActivity.class);
        startActivity(intent);
    }


    private ArrayList<Project> createProjectList(JSONArray announcements, String course) {
        ArrayList<Project> items=new ArrayList<>();
        for(int i=0;i<announcements.length();i++){
            try {
                String name= announcements.getJSONObject(i).getString("name");
                String id =announcements.getJSONObject(i).getString("_id");
                Project tempProject = new Project(id,name,"",null,course);
                items.add(tempProject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }
}
