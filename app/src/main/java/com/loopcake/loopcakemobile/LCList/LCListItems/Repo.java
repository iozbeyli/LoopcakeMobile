package com.loopcake.loopcakemobile.LCList.LCListItems;

/**
 * Created by Melih on 21.05.2017.
 */

public class Repo {
    public final String position;
    public final String repoName;
    public final String repoID;

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
