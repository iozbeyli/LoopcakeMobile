package com.loopcake.loopcakemobile;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.Course;
import com.loopcake.loopcakemobile.PostDatas.CoursePostDatas;

import org.json.JSONObject;

/**
 * Created by MEHMET on 23.05.2017.
 */

public class EditCourseFragment extends LCFragment {
    public EditCourseFragment(){
        layoutID = R.layout.fragment_edit_course;
    }


    @Override
    public void mainFunction() {
        final EditText name = (EditText) layout.findViewById(R.id.edit_name);
        final EditText code = (EditText) layout.findViewById(R.id.edit_code);
        final EditText langs = (EditText) layout.findViewById(R.id.edit_langs);
        final EditText details = (EditText) layout.findViewById(R.id.edit_details);
        name.setText(Session.selectedCourse.name);
        code.setText(Session.selectedCourse.code);
        langs.setText(Session.selectedCourse.langs);
        details.setText(Session.selectedCourse.details);

        final Button but = (Button) layout.findViewById(R.id.edit_save);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.selectedCourse.name = name.getText().toString();
                Session.selectedCourse.code = code.getText().toString();
                Session.selectedCourse.langs = langs.getText().toString();
                Session.selectedCourse.details = details.getText().toString();
                AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "editCourse",
                        CoursePostDatas.getCoursePostData(Session.selectedCourse), new Communicator() {
                    @Override
                    public void successfulExecute(JSONObject jsonObject) {
                        but.setFocusable(false);
                        Snackbar.make(layout, jsonObject.toString()+" Course Edited!", 1000).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {EditCourseFragment.this.getActivity().finish();}
                        }, 1000);
                    }

                    @Override
                    public void failedExecute() {
                        Snackbar.make(layout, "Course Editing Failed!", Snackbar.LENGTH_LONG).show();
                    }
                });
                task.execute((Void) null);
            }
        });

    }
}
