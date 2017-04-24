package com.loopcake.loopcakemobile.CourseFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class CourseDetailFragment extends Fragment implements Communicator{
    private View layout;
    private OnFragmentInteractionListener mListener;

    public CourseDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        layout= inflater.inflate(R.layout.fragment_course_detail, container, false);

        return layout;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        try {
            JSONArray details = jsonObject.getJSONArray("details");
            JSONObject course = details.getJSONObject(0);
            String courseName = course.getString("name");
            String code = course.getString("code");
            String instructor = course.getString("instructor");
            TextView courseNameView = (TextView)layout.findViewById(R.id.courseDetailTitle);
            courseNameView.setText(courseName);
            TextView codeView = (TextView)layout.findViewById(R.id.courseDetailCode);
            codeView.setText(code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failedExecute() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
