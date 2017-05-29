package com.loopcake.loopcakemobile;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.AsyncCommunication.NotificationHandler;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.ChecklistItem;
import com.loopcake.loopcakemobile.PostDatas.AnnouncementPostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by MEHMET on 23.05.2017.
 */

public class CreateProjectFragment extends LCFragment {
    private final static String TAG = "CreateProject";
    ArrayList<Checkpoint> checks = new ArrayList<>();

    public CreateProjectFragment(){
        layoutID = R.layout.fragment_create_project;
    }
    @Override
    public void mainFunction() {
        final EditText ch_label = (EditText) layout.findViewById(R.id.edit_check_label);
        final EditText ch_point = (EditText) layout.findViewById(R.id.edit_check_point);
        final TextView ch_content = (TextView) layout.findViewById(R.id.check_content);
        ((Button) layout.findViewById(R.id.check_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = ch_label.getText().toString();
                int point = Integer.parseInt(ch_point.getText().toString());
                Checkpoint ch = new Checkpoint(label,point);
                checks.add(ch);
                ch_content.append(ch.toString()+"\n");
            }
        });
        ((Button) layout.findViewById(R.id.check_clear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checks.clear();
                ch_content.setText("");
            }
        });


        final JSONObject post = new JSONObject();
        Button datePicker = (Button)layout.findViewById(R.id.picker_date);
        final TextView label = (TextView) layout.findViewById(R.id.label_deadline);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SlideDateTimeListener listener = new SlideDateTimeListener() {
                    @Override
                    public void onDateTimeSet(Date date)
                    {
                        try {
                            post.put("deadline", date.getTime());
                            label.setText(date.toString());
                        } catch (JSONException e) {e.printStackTrace();}
                    }
                    @Override
                    public void onDateTimeCancel() {}
                };
                new SlideDateTimePicker.Builder(getFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        .build()
                        .show();

            }
        });

        final Button but = (Button)layout.findViewById(R.id.create_announcement);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name    = ((EditText)layout.findViewById(R.id.edit_name)) .getText().toString();
                String detail  = ((EditText)layout.findViewById(R.id.edit_detail)).getText().toString();
                String maxGroupSize  = ((EditText)layout.findViewById(R.id.edit_group_size)).getText().toString();
                try {
                    post.put("name", name);
                    post.put("details", detail);
                    post.put("courseID", Session.selectedCourse.courseid);
                    post.put("maxGroupSize",maxGroupSize);
                } catch (JSONException e) {e.printStackTrace();}

                if(post.isNull("deadline")){
                    Snackbar.make(layout, "Pick a Deadline", Snackbar.LENGTH_LONG).show();
                }else{
                    AsyncCommunicationTask comm = new AsyncCommunicationTask(Constants.apiURL+"/addProject",
                            post, new Communicator() {
                        @Override
                        public void successfulExecute(JSONObject jsonObject) {
                            but.setFocusable(false);
                            if(!checks.isEmpty()){
                                try {
                                    Log.d(TAG, "Creating Checklist");
                                    createChecklist(jsonObject);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                NotificationHandler.sendNotificationToCourse(Session.selectedCourse.courseid, "New Project", Session.selectedCourse.name);
                                Snackbar.make(layout, "Project Created!", 1000).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {CreateProjectFragment.this.getActivity().finish();}
                                }, 1000);
                            }

                        }

                        @Override
                        public void failedExecute() {
                            Snackbar.make(layout, "Project Creation Failed!", Snackbar.LENGTH_LONG).show();
                        }

                        private void createChecklist(JSONObject jsonObject) throws JSONException {
                            JSONObject post = new JSONObject();
                            post.put("operation", "1");
                            post.put("projectid", jsonObject.getString("id"));
                            JSONArray ch_array = new JSONArray();
                            for (Checkpoint ch:checks) {
                                ch_array.put(ch.getJSON());
                            }
                            checks.clear();
                            post.put("checkpoints", ch_array);

                            AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL+"/updateChecklist", post, this);
                            task.execute((Void) null);
                        }
                    });

                    comm.execute((Void)null);
                }

            }
        });
    }

    private class Checkpoint{
        private String label;
        private int point;
        private Checkpoint(String label, int point){
            this.label = label;
            this.point = point;
        }
        private JSONObject getJSON() throws JSONException {
            JSONObject obj = new JSONObject();
            obj.put("label", label);
            obj.put("point", point);
            return obj;
        }

        @Override
        public String toString() {
            return label + " (" + point + " points)";
        }
    }
}
