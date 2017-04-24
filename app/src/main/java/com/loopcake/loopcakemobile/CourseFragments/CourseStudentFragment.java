package com.loopcake.loopcakemobile.CourseFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.ListContents.ListItem;
import com.loopcake.loopcakemobile.ListFragment;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseStudentFragment extends Fragment implements Communicator{

    View layout;
    private OnFragmentInteractionListener mListener;

    public CourseStudentFragment() {
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
        layout =inflater.inflate(R.layout.fragment_course_student, container, false);
        JSONObject postData = new JSONObject();
        try {
            postData.put("courseid", Session.selectedID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.apiURL+"getStudents",postData,this);
        asyncCommunicationTask.execute((Void)null);
        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        ArrayList<ListItem> students = new ArrayList<>();
        try {
            JSONArray details = jsonObject.getJSONArray("details");
            for(int i=0;i<details.length();i++){
                JSONObject student = details.getJSONObject(0);
                String name = student.getString("name");
                String surname = student.getString("surname");
                String id = student.getString("_id");
                ListItem listItem = new ListItem(name+" "+surname,id);
                Log.d("name",name+" "+surname);
                Log.d("id",id);
                students.add(listItem);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ListFragment listFragment = new ListFragment().newInstance(students);
                ft.replace(R.id.course_student_list_container, listFragment);
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failedExecute() {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
