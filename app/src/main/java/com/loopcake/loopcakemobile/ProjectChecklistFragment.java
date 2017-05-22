package com.loopcake.loopcakemobile;

import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.ChecklistItem;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ProjectChecklistFragment extends LCListFragment<ChecklistItem>{

    public ProjectChecklistFragment() {
    }

    @Override
    protected void fillList() {
        ArrayList<ChecklistItem> items = new ArrayList<ChecklistItem>();
        try {
            Log.d("array", "onCreateView: "+Session.selectedProject);
            JSONArray jsonarray = Session.selectedProject.getJSONArray("details").getJSONObject(0).getJSONArray("checklist");
            for (int i = 0; i <jsonarray.length() ; i++) {
                String detail = ""+jsonarray.getJSONObject(i).getInt("point");
                boolean status = jsonarray.getJSONObject(i).getBoolean("status");
                String cpid = jsonarray.getJSONObject(i).getString("cpid");
                String _id = jsonarray.getJSONObject(i).getString("_id");
                ChecklistItem item = new ChecklistItem(detail, status, _id, cpid);
                items.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        displayList(items,R.layout.fragment_project_checklist);
    }


    @Override
    public void listItemPressed(ChecklistItem listItem) {

    }

    @Override
    public void setItemContent(ChecklistItem item, View itemView) {
        Switch check = (Switch)itemView.findViewById(R.id.checklist_switch);
        check.setText(item.getDetail());
        check.setChecked(item.getStatus());
    }

}
