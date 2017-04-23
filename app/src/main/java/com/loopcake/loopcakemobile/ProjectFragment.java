package com.loopcake.loopcakemobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment implements Communicator{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View layout;
    private OnFragmentInteractionListener mListener;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance(String param1, String param2) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_project, container, false);
        String apiURL ="http://207.154.203.163:8000/api/getGroup";
        JSONObject postData = new JSONObject();
        try {
            postData.put("operation","2");
            postData.put("id",Session.selectedID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(apiURL,postData,this);
        asyncCommunicationTask.execute((Void) null);
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        try {
            jsonObject = jsonObject.getJSONArray("details").getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("dds", "successfulExecute: JSON CAME" +jsonObject);
        TextView name = (TextView) layout.findViewById(R.id.group_name);
        TextView checklist = (TextView) layout.findViewById(R.id.checklist);
        TextView student = (TextView) layout.findViewById(R.id.student);
        TextView project = (TextView) layout.findViewById(R.id.project);
        TextView course = (TextView) layout.findViewById(R.id.course);

        try {
            name.setText("Group Name: "+jsonObject.getString("name"));
            checklist.setText("Checklist: "+jsonObject.getJSONArray("checklist").toString());
            student.setText("Members: "+jsonObject.getJSONArray("students").toString());
            project.setText("Project: "+jsonObject.getString("project"));
            course.setText("Course: "+jsonObject.getString("course"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {
        Log.d("dds", "FAIL");
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
