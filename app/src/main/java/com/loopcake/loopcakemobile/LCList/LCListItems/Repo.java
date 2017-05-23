package com.loopcake.loopcakemobile.LCList.LCListItems;

import com.loopcake.loopcakemobile.RepoFragments.LCFile;
import com.loopcake.loopcakemobile.User;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Melih on 21.05.2017.
 */

public class Repo {
    public final String position;
    public final String repoName;
    public final String repoID;
    public boolean isRepoPersonal;
    public JSONArray tags;
    public JSONArray branchPointers;
    public JSONArray collaborators;
    public JSONArray membersJSONArray;
    public ArrayList<User> members;
    public ArrayList<LCFile> files;

    public Repo(String position, String repoName, String repoID) {
        this.position = position;
        this.repoName = repoName;
        this.repoID = repoID;
    }


    @Override
    public String toString() {
        return "Repo{" +
                "position='" + position + '\'' +
                ", repoName='" + repoName + '\'' +
                ", repoID='" + repoID + '\'' +
                '}';
    }
}
