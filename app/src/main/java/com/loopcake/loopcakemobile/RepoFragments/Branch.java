package com.loopcake.loopcakemobile.RepoFragments;

/**
 * Created by Melih on 29.05.2017.
 */

public class Branch {
    public String history_response;
    public String name;
    public String repo_id;
    public Branch(String name,String repo_id,String history_response){
        this.name=name;
        this.repo_id=repo_id;
        this.history_response=history_response;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "history_response='" + history_response + '\'' +
                ", name='" + name + '\'' +
                ", repo_id='" + repo_id + '\'' +
                '}';
    }
}
