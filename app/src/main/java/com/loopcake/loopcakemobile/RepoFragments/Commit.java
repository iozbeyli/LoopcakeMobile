package com.loopcake.loopcakemobile.RepoFragments;

import java.util.ArrayList;

/**
 * Created by Melih on 25.05.2017.
 */

public class Commit{
    String sha;
    String message;
    String time;
    ArrayList<String> branches;
    ArrayList<Commit> children=new ArrayList<>();
    public Commit(String sha,String message,String time,ArrayList<String> branches,ArrayList<Commit> children){
        this.sha=sha;
        this.message=message;
        this.time=time;
        this.branches=branches;
        this.children=children;
    }
}