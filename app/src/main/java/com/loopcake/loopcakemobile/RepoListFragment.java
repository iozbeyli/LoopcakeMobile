package com.loopcake.loopcakemobile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.ListContents.RepoContent;
import com.loopcake.loopcakemobile.ListContents.RepoContent.RepoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RepoListFragment extends Fragment implements Communicator{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
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
        JSONObject loginData = new JSONObject();
        try {
            loginData.put("operation","1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask("http://207.154.203.163:8000/api/getRepo",loginData,this);
        asyncCommunicationTask.execute((Void) null);
        // Set the adapter
        if (layout instanceof RecyclerView) {
            Context context = layout.getContext();
            RecyclerView recyclerView = (RecyclerView) layout;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new RepoRecyclerViewAdapter(RepoContent.ITEMS, mListener));
        }
        return layout;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(RepoItem item);
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
        RepoContent.clearItems();
        for(int i=0;i<repos.length();i++){
            JSONObject repo = null;
            try {
                repo = repos.getJSONObject(i);
                String repoName = repo.getString("name");
                String repoID = repo.getString("id");
                RepoContent.addItem(new RepoItem(""+i,repoName,repoID));
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
            recyclerView.setAdapter(new RepoRecyclerViewAdapter(RepoContent.ITEMS, mListener));
        }
    }
}
