package com.loopcake.loopcakemobile.RepoFragments;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.Session;
import com.loopcake.loopcakemobile.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Melih on 23.05.2017.
 */

public class RepoStudentListFragment extends LCListFragment<User> implements Communicator{
    @Override
    protected void fillList() {
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.apiURL+"user", RepoPostDatas.getRepoMembersPostData(Session.selectedRepo.membersJSONArray),this);
        asyncCommunicationTask.execute((Void) null);
    }

    @Override
    public void listItemPressed(User listItem, View itemView) {

    }

    @Override
    public void setItemContent(User item, View itemView) {
        TextView content =(TextView) itemView.findViewById(R.id.content);
        content.setText(item.name+" "+item.surname);
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        Log.d("get repo students",jsonObject.toString());
        try {
            Session.selectedRepo.members=new ArrayList<>();
            JSONArray repoStudentsDetails = jsonObject.getJSONArray("details");
            for(int i=0;i<repoStudentsDetails.length();i++){
                JSONObject student = repoStudentsDetails.getJSONObject(i);
                String name = student.getString("name");
                String surname = student.getString("surname");
                String photoID = student.getString("photo");
                User temp = new User();
                temp.name=name;
                temp.surname=surname;
                temp.photoID=photoID;
                Session.selectedRepo.members.add(temp);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        displayList(Session.selectedRepo.members, R.layout.fragment_item);

    }

    @Override
    public void failedExecute() {

    }
}
