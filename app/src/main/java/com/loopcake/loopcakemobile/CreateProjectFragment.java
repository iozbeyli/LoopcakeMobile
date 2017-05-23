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
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.PostDatas.AnnouncementPostDatas;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by MEHMET on 23.05.2017.
 */

public class CreateProjectFragment extends LCFragment {
    private final static String TAG = "CreateProject";

    public CreateProjectFragment(){
        layoutID = R.layout.fragment_create_project;
    }
    @Override
    public void mainFunction() {
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
                            Snackbar.make(layout, "Project Created!", 1000).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {CreateProjectFragment.this.getActivity().finish();}
                            }, 1000);
                        }

                        @Override
                        public void failedExecute() {
                            Snackbar.make(layout, "Project Creation Failed!", Snackbar.LENGTH_LONG).show();
                        }
                    });

                    comm.execute((Void)null);
                }

            }
        });
    }
}
