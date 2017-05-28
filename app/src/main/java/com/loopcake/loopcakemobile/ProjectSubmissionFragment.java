package com.loopcake.loopcakemobile;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.LCList.LCListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class ProjectSubmissionFragment extends LCListFragment<ProjectSubmissionFragment.Attachment> implements Communicator {
    private String submissionid = "";
    @Override
    public void successfulExecute(JSONObject jsonObject) {
        JSONObject submission = new JSONObject();
        JSONArray attachs = new JSONArray();
        String date="";
        try {
            submission = jsonObject.getJSONArray("details").getJSONObject(0);
            submissionid = submission.getString("_id");
            attachs = submission.getJSONArray("attachment");
            date = submission.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Button button = (Button) layout.findViewById(R.id.download_report);
        if(submission.isNull("report")){
            button.setVisibility(View.GONE);
        }else{
            try {
                final String report = submission.getString("report");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(Constants.apiURL+"/download?_id="+report);
                        DownloadManager.Request req = new DownloadManager.Request(uri);
                        req.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,"report.pdf");
                        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        Long reference = manager.enqueue(req);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ((TextView) layout.findViewById(R.id.label_last_activity)).setText("Last Activity: "+date);
        ArrayList<Attachment> list = new ArrayList<>();
        for (int i = 0; i < attachs.length(); i++) {
            try {
                JSONObject obj = attachs.getJSONObject(i);
                list.add(new Attachment(obj.getString("filename"),obj.getString("attachmentid")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        displayList(list, R.layout.file_item);

    }

    @Override
    public void failedExecute() {

    }

    @Override
    public void setLayoutID() {
        layoutID = R.layout.fragment_project_submission;
    }

    @Override
    protected void fillList() {
        Button button_attach_repo = (Button)layout.findViewById(R.id.attach_repo);
        if(Session.selectedGroup.isNull("repo")){
            button_attach_repo.setVisibility(View.GONE);
        }else{
            button_attach_repo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject post = new JSONObject();
                    try {
                        post.put("submissionid", submissionid);
                        post.put("repo",Session.selectedGroup.getString("repo"));
                        post.put("deadline",Session.project.deadline);
                        post.put("groupname",Session.selectedGroup.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "/submitRepo", post, new Communicator() {
                        @Override
                        public void successfulExecute(JSONObject jsonObject) {
                            //Toast.makeText(getContext(),"Successful", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void failedExecute() {
                            //Toast.makeText(getContext(),"Failed", Toast.LENGTH_LONG).show();
                        }
                    });
                    task.execute((Void)null);
                }
            });
        }
        JSONObject post = new JSONObject();
        try {
            post.put("groupID", Session.selectedGroup.getString("_id"));
            post.put("operation", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL+"/getSubmission", post, this);
        task.execute((Void) null);
    }

    @Override
    public void listItemPressed(Attachment listItem, View itemView) {

    }

    @Override
    public void setItemContent(final Attachment item, final View itemView) {
        TextView tv = (TextView)itemView.findViewById(R.id.filename);
        tv.setText(item.filename);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(Constants.apiURL+"/download?_id="+item.id);
                DownloadManager.Request req = new DownloadManager.Request(uri);
                req.setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,item.filename);
                req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = manager.enqueue(req);
            }
        });

        ImageView iv = (ImageView) itemView.findViewById(R.id.remove_icon);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject post = new JSONObject();
                try {
                    post.put("operation", "2");
                    post.put("submissionid", submissionid);
                    post.put("attachmentid", item.id);
                    post.put("deadline", Session.project.deadline);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "/remove", post, new Communicator() {
                    @Override
                    public void successfulExecute(JSONObject jsonObject) {
                        itemView.setVisibility(View.GONE);
                    }

                    @Override
                    public void failedExecute() {

                    }
                });
                task.execute((Void) null);
            }
        });

    }

    public class Attachment{
        private String filename;
        private String id;
        private Attachment(String filename, String id){
            this.filename = filename;
            this.id = id;
        }
    }
}
