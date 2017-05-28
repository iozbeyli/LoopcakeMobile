package com.loopcake.loopcakemobile;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.PostDatas.GroupPostDatas;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.loopcake.loopcakemobile.Enumerators.Enumerators.ProjectActions.CREATE_GROUP;
import static com.loopcake.loopcakemobile.Enumerators.Enumerators.ProjectActions.GROUP_MEMBER;

public class ProjectActivity extends LCTabbedActivity implements Communicator {
    private final static String TAG = "ProjectActivity";

    @Override
    public void onCreateFunction() {
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getGroupURL, GroupPostDatas.getGroupDetailsPostData(),this);
        asyncCommunicationTask.execute((Void) null);
    }

    @Override
    public SectionsPagerAdapter createSectionPagerAdapter(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> pageTitles = new ArrayList<>();
        ProjectChecklistFragment clFragment = new ProjectChecklistFragment();
        fragments.add(clFragment);
        ProjectDetailFragment projectDetailFragment = new ProjectDetailFragment();
        fragments.add(projectDetailFragment);
        ProjectSubmissionFragment projectAttachmentFragment = new ProjectSubmissionFragment();
        fragments.add(projectAttachmentFragment);
        pageTitles.add("Progress");
        pageTitles.add("Details");
        pageTitles.add("Submission");
        return new SectionsPagerAdapter(getSupportFragmentManager(),fragments,pageTitles);
    }


    @Override
    public ArrayList<ArrayList<String>> setTextListsForFragments() {
        return createFABTexts(new String[][]{
                {null, null, null},
                {"Members", null, null},
                {null, null, null}
        });

    }

    @Override
    public ArrayList<ArrayList<View.OnClickListener>> setListenerListsForFragments() {
        return createFABListeners(new Enumerators.ProjectActions[][]{
                {null, null, null},
                {GROUP_MEMBER, null, null},
                {null, null, null}
        });
    }
    @Override
    public void successfulExecute(JSONObject jsonObject) {
        Log.d("execute", "successfulExecute: ");
        Session.selectedGroup = jsonObject;
        try {
            if(!jsonObject.isNull("details")) {
                Object res = jsonObject.getJSONArray("details").get(0);
                if (res instanceof Boolean) {
                     Snackbar.make(findViewById(R.id.main_content),"nogroup",2000).show();
                    Intent in = new Intent(ProjectActivity.this, SubProjectActivity.class);
                    in.putExtra("fragment", CREATE_GROUP);
                    startActivity(in);
                }else{
                    Session.selectedGroup = jsonObject.getJSONArray("details").getJSONObject(0);
                    setTitle(Session.selectedGroup.getString("name"));
                    setTabView();
                }
            }else{
                Toast.makeText(this,"null",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {
        Log.d("execute", "failedExecute: ");
    }

    private ArrayList<ArrayList<View.OnClickListener>> createFABListeners(Enumerators.ProjectActions[][] act){
        ArrayList<ArrayList<View.OnClickListener>> fragmentListenerLists = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {
            ArrayList<View.OnClickListener> listeners = new ArrayList<>();
            for (int j = 0; j < 3 ; j++) {
                if(act[i][j] != null)
                    listeners.add(createFABListener(act[i][j]));
            }
            fragmentListenerLists.add(listeners);
        }
        return fragmentListenerLists;
    }


    private View.OnClickListener createFABListener(final Enumerators.ProjectActions act){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ProjectActivity.this, SubProjectActivity.class);
                in.putExtra("fragment", act);
                startActivity(in);
            }
        };
    }


    private ArrayList<ArrayList<String>> createFABTexts(String[][] lab){
        ArrayList<ArrayList<String>> labelList = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {
            ArrayList<String> labels = new ArrayList<>();
            for (int j = 0; j < 3 ; j++) {
                if(lab[i][j] != null)
                    labels.add(lab[i][j]);
            }
            labelList.add(labels);
        }

        return labelList;
    }
}
