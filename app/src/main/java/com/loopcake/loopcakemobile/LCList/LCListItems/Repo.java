package com.loopcake.loopcakemobile.LCList.LCListItems;

import com.loopcake.loopcakemobile.RepoFragments.LCFile;
import com.loopcake.loopcakemobile.User;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Melih on 21.05.2017.
 */

public class Repo {

    public final String repoName;
    public final String repoID;
    public boolean isRepoPersonal;
    public JSONArray tags;
    public JSONArray branchPointers;
    public JSONArray collaborators;
    public JSONArray membersJSONArray;
    public ArrayList<User> members;
    public ArrayList<LCFile> files;
    public String currentBranch;
    public String currentSha;

    public Repo(String repoName, String repoID,String currentBranch,String currentSha) {
        this.repoName = repoName;
        this.repoID = repoID;
        this.currentBranch = currentBranch;
        this.currentSha =currentSha;
    }


    @Override
    public String toString() {
        return "Repo{" +
                "repoName='" + repoName + '\'' +
                ", repoID='" + repoID + '\'' +
                ", isRepoPersonal=" + isRepoPersonal +
                ", tags=" + tags +
                ", branchPointers=" + branchPointers +
                ", collaborators=" + collaborators +
                ", membersJSONArray=" + membersJSONArray +
                ", members=" + members +
                ", files=" + files +
                ", currentBranch='" + currentBranch + '\'' +
                ", currentSha='" + currentSha + '\'' +
                '}';
    }
}
