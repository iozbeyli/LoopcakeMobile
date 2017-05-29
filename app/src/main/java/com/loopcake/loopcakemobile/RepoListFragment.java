package com.loopcake.loopcakemobile;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCDatabase.LCDatabaseHelper;
import com.loopcake.loopcakemobile.LCDatabase.LCNetworkChecker;
import com.loopcake.loopcakemobile.LCList.LCListFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.Repo;
import com.loopcake.loopcakemobile.PostDatas.PostDatas;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RepoListFragment extends LCListFragment<Repo> implements Communicator{

    public RepoListFragment() {
    }

    @Override
    public void setLayoutID() {

    }

    @Override
    protected void fillList() {
        if(LCNetworkChecker.isNetworkConnected(getActivity())){
            AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getRepoURL, RepoPostDatas.getRepoListPostData(),this);
            asyncCommunicationTask.execute((Void) null);
        }else{
            LCDatabaseHelper helper = new LCDatabaseHelper(getActivity());
            SQLiteDatabase db = helper.getReadableDatabase();
            displayList(helper.getRepos(db),R.layout.fragment_item);
        }

    }

    @Override
    public void listItemPressed(Repo listItem, View itemView) {
        Session.selectedRepo=listItem;
        Intent intent = new Intent(getActivity(),RepoActivity.class);
        startActivity(intent);
    }

    @Override
    public void setItemContent(Repo item, View itemView) {
        TextView text = (TextView) itemView.findViewById(R.id.content);
        text.setText(item.repoName);
    }

    public void successfulExecute(JSONObject jsonObject){
        Log.d("Communicator","Succesfully Execute");
        try {
            JSONArray array = jsonObject.getJSONArray("details");
            JSONObject element = array.getJSONObject(0);
            JSONArray repoArray = element.getJSONArray("repos");
            fillRepoList(repoArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void failedExecute(){

    }
    public void fillRepoList(JSONArray repos){
        ArrayList<Repo> repoList = new ArrayList<>();
        for(int i=0;i<repos.length();i++){
            JSONObject repo = null;
            try {
                repo = repos.getJSONObject(i);
                Log.d("Repo",repo.toString());
                String repoName = repo.getString("name");
                String repoID = repo.getString("id");
                Repo temp = new Repo(repoName,repoID,null,null);
                repoList.add(temp);
                LCDatabaseHelper helper = new LCDatabaseHelper(getActivity());
                helper.insertRepo(temp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        displayList(repoList,R.layout.fragment_item);
    }
}
