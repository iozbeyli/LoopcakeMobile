package com.loopcake.loopcakemobile;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.PostDatas.AnnouncementPostDatas;

import org.json.JSONObject;

/**
 * Created by MEHMET on 22.05.2017.
 */

public class CreateAnnouncementFragment extends LCFragment {
    private static String TAG = "CreateAnnouncement";

    public CreateAnnouncementFragment(){
        layoutID = R.layout.fragment_create_announcement;
    }

    @Override
    public void mainFunction() {
        Button but = (Button)layout.findViewById(R.id.create_announcement);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title    = ((EditText)layout.findViewById(R.id.edit_title))  .getText().toString();
                String content  = ((EditText)layout.findViewById(R.id.edit_content)).getText().toString();

                AsyncCommunicationTask comm = new AsyncCommunicationTask(Constants.apiURL,
                        AnnouncementPostDatas.createAnnouncementPostData(title, content), new Communicator() {
                    @Override
                    public void successfulExecute(JSONObject jsonObject) {
                        Log.d(TAG, "successfulExecute: "+jsonObject);
                    }

                    @Override
                    public void failedExecute() {

                    }
                });

            }
        });

    }
}
