package com.loopcake.loopcakemobile;

import com.loopcake.loopcakemobile.LCList.LCListItems.Repo;

import java.util.ArrayList;

/**
 * Created by Melih on 22.04.2017.
 */

public class User {
    public String id;
    public String name;
    public String surname;
    public String email;
    public String type;
    public String photoID;
    public String universityID;

    public ArrayList<Repo> getRepos() {
        return repos;
    }

    public void setRepos(ArrayList<Repo> repos) {
        this.repos = repos;
    }

    public ArrayList<Repo> repos;

    public User(String name,String surname,String email,String type,String photoID,String universityID){
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.type=type;
        this.photoID=photoID;
        this.universityID=universityID;
    }
    public User(){

    }

}
