package com.loopcake.loopcakemobile;

import android.util.Log;
import android.widget.TextView;

import com.loopcake.loopcakemobile.LCFragment.LCFragment;

import org.json.JSONException;
import org.json.JSONObject;



public class ProjectDetailFragment extends LCFragment {

    public ProjectDetailFragment() {
        // Required empty public constructor
        layoutID = R.layout.fragment_project_detail;
    }

    @Override
    public void mainFunction() {
        JSONObject jsonObject = null;
        try {
            jsonObject = Session.selectedProject.getJSONArray("details").getJSONObject(0);
            Log.d("dds", "successfulExecute: JSON CAME" +jsonObject);
            TextView detailView = (TextView) layout.findViewById(R.id.project_detail_text);
            String detail= "Group Name: "+jsonObject.getString("name")+"\n--------\n"+
                    "Checklist: "+jsonObject.getJSONArray("checklist").toString()+"\n--------\n"+
                    "Members: "+jsonObject.getJSONArray("students").toString()+"\n--------\n"+
                    "Project: "+jsonObject.getString("project")+"\n--------\n"+
                    "Course: "+jsonObject.getString("course");

            detailView.setText(detail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
