package com.loopcake.loopcakemobile;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.AsyncCommunication.NotificationHandler;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.PostDatas.AnnouncementPostDatas;

import org.json.JSONObject;

/**
 * Created by MEHMET on 22.05.2017.
 */

public class CreateAnnouncementFragment extends LCFragment {
    private final static String TAG = "CreateAnnouncement";

    public CreateAnnouncementFragment(){
        layoutID = R.layout.fragment_create_announcement;
    }

    @Override
    public void mainFunction() {
        final Button but = (Button)layout.findViewById(R.id.create_announcement);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title    = ((EditText)layout.findViewById(R.id.edit_title)).getText().toString();
                String content  = ((EditText)layout.findViewById(R.id.edit_content)).getText().toString();
                AsyncCommunicationTask comm = new AsyncCommunicationTask(Constants.apiURL+"/announce",
                        AnnouncementPostDatas.createAnnouncementPostData(title, content), new Communicator() {
                    @Override
                    public void successfulExecute(JSONObject jsonObject) {
                        but.setFocusable(false);
                        NotificationHandler.sendNotificationToCourse(Session.selectedCourse.courseid, "New Announcement", title);
                        Snackbar.make(layout, "Announcement Created!", 1000).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CreateAnnouncementFragment.this.getActivity().finish();
                            }
                        }, 1000);
                    }

                    @Override
                    public void failedExecute() {
                        Snackbar.make(layout, "Project Creation Failed!", Snackbar.LENGTH_LONG).show();
                    }
                });

                comm.execute((Void)null);

            }
        });

    }
}
