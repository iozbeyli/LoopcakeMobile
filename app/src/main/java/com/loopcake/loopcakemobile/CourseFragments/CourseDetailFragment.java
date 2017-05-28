package com.loopcake.loopcakemobile.CourseFragments;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.AsyncCommunication.ImageDownloaderTask;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CourseDetailFragment extends LCFragment implements Communicator{

    public CourseDetailFragment() {
        // Required empty public constructor
        layoutID = R.layout.fragment_course_detail;
    }

    @Override
    public void mainFunction() {
    // Inflate the layout for this fragment
        JSONObject postData = new JSONObject();
        try {
            postData.put("operation","2");
            postData.put("id", Session.selectedID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.apiURL+"course",postData,this);
        asyncCommunicationTask.execute((Void)null);
    }


    @Override
    public void successfulExecute(JSONObject jsonObject) {
        try {
            String details = "";
            String langs = "";
            String syllabus= null;
            JSONArray array = jsonObject.getJSONArray("details");
            JSONObject course = array.getJSONObject(0);
            String courseName = course.getString("name");
            String code = course.getString("code");
            String instructor = course.getString("instructor");
            if(!course.isNull("details")) details = course.getString("details");
            if(!course.isNull("programmingLanguage"))  langs = course.getString("programmingLanguage");
            if(!course.isNull("syllabus")) syllabus = course.getString("syllabus");
            Session.selectedCourse.code = code;
            Session.selectedCourse.instructor = instructor;
            Session.selectedCourse.details = details;
            Session.selectedCourse.langs = langs;

            TextView courseNameView = (TextView)layout.findViewById(R.id.label_course_name);
            courseNameView.setText(courseName+" "+code);
            TextView detail = (TextView)layout.findViewById(R.id.label_course_detail);
            detail.append(details);
            TextView syllabusView = (TextView)layout.findViewById(R.id.label_syllabus);
            if(syllabus != null)
                syllabusView.setText(syllabus);
            else
                syllabusView.setVisibility(View.GONE);

            TextView prog = (TextView) layout.findViewById(R.id.label_programming_langs);
            prog.append(langs);
            setInstructorCard();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failedExecute() {

    }


    private void setInstructorCard() {
        final TextView ins_name = (TextView) layout.findViewById(R.id.content);
        final ImageView ins_photo = (ImageView) layout.findViewById(R.id.user_photo);
        JSONObject post = new JSONObject();
        try {
            post.put("users", Session.selectedCourse.instructor);
            post.put("operation","2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL+"/user",post, new Communicator() {
            @Override
            public void successfulExecute(JSONObject jsonObject) {
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

            }
        });
        task.execute((Void) null);
    }

}
