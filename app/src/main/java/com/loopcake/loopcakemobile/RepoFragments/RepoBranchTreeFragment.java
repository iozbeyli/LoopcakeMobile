package com.loopcake.loopcakemobile.RepoFragments;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.PostDatas.RepoPostDatas;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.RepoActivity;
import com.loopcake.loopcakemobile.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Melih on 23.05.2017.
 */

public class RepoBranchTreeFragment extends LCFragment implements Communicator{

    private WebView webView;
    private String historyResponse;
    private ArrayList<String> branchList;
    public RepoBranchTreeFragment(){
        layoutID = R.layout.fragment_repo_branch_tree;
    }

    @Override
    public void mainFunction() {
        AsyncCommunicationTask repoBranchComm = new AsyncCommunicationTask(Constants.getFileHistoryURL, RepoPostDatas.getRepoHistoryPostData(Session.selectedRepo.repoID,Session.user.userID),this);
        repoBranchComm.execute((Void)null);

    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        Log.d("Repo History",jsonObject.toString());
        try {
            JSONArray commitsJSONArray = jsonObject.getJSONArray("details");
            ArrayList<Commit> commits = new ArrayList<>();
            for(int i=0;i<commitsJSONArray.length();i++){
                JSONObject commit = commitsJSONArray.getJSONObject(i);
                commits.add(parseCommitJSONObject(commit));
            }
            historyResponse=commitsJSONArray.toString();
            JSONArray branchesJSONArray = jsonObject.getJSONArray("branches");
            branchList = new ArrayList<>();
            int currentBranchPosition = 0;
            for(int i=0;i<branchesJSONArray.length();i++){
                String branch = branchesJSONArray.getString(i);
                branchList.add(branch);
                if(Session.selectedRepo.currentBranch.equals(branch)){
                    currentBranchPosition=i;
                }
            }
            Spinner spinner = (Spinner)layout.findViewById(R.id.spinner_branch);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, branchList );
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(currentBranchPosition);
            final int finalCurrentBranchPosition = currentBranchPosition;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if(finalCurrentBranchPosition !=position){
                        AsyncCommunicationTask checkoutComm = new AsyncCommunicationTask(Constants.getRepoCheckoutURL, RepoPostDatas.getRepoCheckoutPostData(Session.selectedRepo.repoID, Session.user.userID, branchList.get(position)), new Communicator() {
                            @Override
                            public void successfulExecute(JSONObject jsonObject) {
                                Log.d("Checkout response",jsonObject.toString());
                                ((RepoActivity)getActivity()).onCreateFunction();
                            }

                            @Override
                            public void failedExecute() {

                            }
                        });
                        checkoutComm.execute((Void) null);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }

            });
            webView = (WebView) layout.findViewById(R.id.repo_branch_web_view);
            webView.setWebChromeClient(new WebChromeClient() {});
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "Android");
            webView.loadUrl("file:///android_asset/repoBranchTree.html");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public Commit parseCommitJSONObject(JSONObject jsonObject){
        try {
            JSONObject logJSONObject = jsonObject.getJSONObject("log");
            String sha = logJSONObject.getString("sha");
            String message = logJSONObject.getString("message");
            String time = logJSONObject.getString("time");
            JSONArray branchesJSONArray = logJSONObject.getJSONArray("branch");
            ArrayList<String> branches = new ArrayList<>();
            for(int i=0;i<branchesJSONArray.length();i++){
                branches.add(branchesJSONArray.getString(i));
            }
            JSONArray childrenJSONObject = jsonObject.getJSONArray("children");
            ArrayList<Commit> children = new ArrayList<>();
            for(int i=0;i<childrenJSONObject.length();i++){
                children.add(parseCommitJSONObject(childrenJSONObject.getJSONObject(i)));
            }
            Commit newCommit = new Commit(sha,message,time,branches,children);
            return newCommit;

        } catch (JSONException e) {
            e.printStackTrace();
        }
            return null;
    }

    @Override
    public void failedExecute() {

    }

    public class JavaScriptInterface {
        Context mContext;
        JavaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public String getFromAndroid() {
            return historyResponse;
        }
    }
}
