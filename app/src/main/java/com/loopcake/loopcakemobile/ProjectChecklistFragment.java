package com.loopcake.loopcakemobile;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.ChecklistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProjectChecklistFragment extends LCListFragment<ChecklistItem>{
    HashMap<String,ChecklistItem> pt = new HashMap<>();
    public ProjectChecklistFragment() {
    }

    @Override
    public void setLayoutID() {
        layoutID = R.layout.fragment_project_checklist;
    }

    @Override
    protected void fillList() {
        ArrayList<ChecklistItem> items = new ArrayList<>();
        try {
            Log.d("array", "onCreateView: "+Session.selectedGroup);
            JSONArray jsonarray = Session.selectedGroup.getJSONArray("checklist");
            for (int i = 0; i <jsonarray.length() ; i++) {
                int point = jsonarray.getJSONObject(i).getInt("point");
                boolean status = jsonarray.getJSONObject(i).getBoolean("status");
                String cpid = jsonarray.getJSONObject(i).getString("cpid");
                String _id = jsonarray.getJSONObject(i).getString("_id");
                String detail = jsonarray.getJSONObject(i).getString("details");
                String label = jsonarray.getJSONObject(i).getString("label");
                ChecklistItem item = new ChecklistItem(point, status, _id, cpid,detail,label);
                items.add(item);
                pt.put(item.getcpid(),item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateProgressBar();
        setUpdateButton();

        displayList(items,R.layout.fragment_project_checkpoint);
    }

    private void setUpdateButton() {
        Button but = (Button) layout.findViewById(R.id.update_checklist);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String groupid = Session.selectedGroup.getString("_id");
                    JSONArray checkpoints = new JSONArray();
                    for (ChecklistItem it: pt.values()) {
                        JSONObject obj = new JSONObject();
                        obj.put("cpid",it.getcpid());
                        obj.put("status",it.getStatus());
                        checkpoints.put(obj);
                    }
                    JSONObject post = new JSONObject();
                    post.put("groupid", groupid);
                    post.put("operation", "4");
                    post.put("checkpoints", checkpoints);
                    Toast.makeText(getContext(),checkpoints.toString(),Toast.LENGTH_LONG).show();
                    AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "/updateChecklist", post, new Communicator() {
                        @Override
                        public void successfulExecute(JSONObject jsonObject) {
                            Snackbar.make(layout,"Checklist Updated", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failedExecute() {
                            Snackbar.make(layout,"Update Failed!", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    task.execute((Void) null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void listItemPressed(ChecklistItem listItem, View itemView) {
    }

    @Override
    public void setItemContent(final ChecklistItem item, View itemView) {
        Switch check = (Switch)itemView.findViewById(R.id.checklist_switch);
        check.setText(item.getLabel()+" ("+item.getPoint()+" Points)");
        check.setChecked(item.getStatus());

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pt.get(item.getcpid()).toggleStatus();
                updateProgressBar();
            }
        });
    }

    private void updateProgressBar(){
        ProgressBar bar = (ProgressBar) layout.findViewById(R.id.checklist_progressBar);
        int total = 0;
        int completed = 0;
        for (ChecklistItem it:pt.values()) {
            total += it.getPoint();
            if(it.getStatus()){
                completed += it.getPoint();
            }
        }

        bar.setMax(total);
        bar.setProgress(completed);
        int progress = bar.getProgress();
        ((TextView) layout.findViewById(R.id.label_progress)).setText("Total Progress: "+progress+"%");

    }

}
