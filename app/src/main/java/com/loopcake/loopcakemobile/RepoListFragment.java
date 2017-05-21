package com.loopcake.loopcakemobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCList.LCListCarrier;
import com.loopcake.loopcakemobile.LCList.LCListItems.RepoItem;
import com.loopcake.loopcakemobile.LCList.LCRecyclerViewAdapter;
import com.loopcake.loopcakemobile.PostDatas.PostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RepoListFragment extends Fragment implements Communicator,LCListCarrier<RepoItem>{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    View layout;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepoListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RepoListFragment newInstance(int columnCount) {
        RepoListFragment fragment = new RepoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.repo_fragment_item_list, container, false);
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask(Constants.getRepoURL, PostDatas.RepoPostDatas.getRepoPostData(),this);
        asyncCommunicationTask.execute((Void) null);
        return layout;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void listItemPressed(RepoItem listItem) {

    }

    @Override
    public void setItemContent(RepoItem item, View itemView) {
        TextView text = (TextView) itemView.findViewById(R.id.content);
        text.setText(item.content);
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
        ArrayList<RepoItem> repoList = new ArrayList<>();
        for(int i=0;i<repos.length();i++){
            JSONObject repo = null;
            try {
                repo = repos.getJSONObject(i);
                String repoName = repo.getString("name");
                String repoID = repo.getString("id");
                repoList.add(new RepoItem(""+i,repoName,repoID));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (layout instanceof RecyclerView) {
            Context context = layout.getContext();
            RecyclerView recyclerView = (RecyclerView) layout;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new LCRecyclerViewAdapter<RepoItem>(repoList,this,R.layout.repo_fragment_item));
        }
    }
}
