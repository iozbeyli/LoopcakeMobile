package com.loopcake.loopcakemobile.CourseFragments;

import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
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
            JSONArray array = jsonObject.getJSONArray("details");
            JSONObject course = array.getJSONObject(0);
            String courseName = course.getString("name");
            String code = course.getString("code");
            String instructor = course.getString("instructor");
            if(!course.isNull("details"))               details = course.getString("details");
            if(!course.isNull("programmingLanguage"))  langs = course.getString("programmingLanguage");
            Session.selectedCourse.code = code;
            Session.selectedCourse.instructor = instructor;
            Session.selectedCourse.details = details;
            Session.selectedCourse.langs = langs;

            TextView courseNameView = (TextView)layout.findViewById(R.id.courseDetailTitle);
            courseNameView.setText(courseName);
            TextView codeView = (TextView)layout.findViewById(R.id.courseDetailCode);
            codeView.setText(code);
            TextView instructorName = (TextView)layout.findViewById(R.id.courseDetailInstructorName);
            instructorName.setText(details);
            TextView instructorDetails = (TextView)layout.findViewById(R.id.courseDetailInstructorDetails);
            instructorDetails.setText(langs);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failedExecute() {

    }

}
