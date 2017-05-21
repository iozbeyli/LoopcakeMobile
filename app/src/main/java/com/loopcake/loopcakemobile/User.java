package com.loopcake.loopcakemobile;

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
    public String[] repos;

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

    public void setRepos(String[] repos){
        this.repos =repos;
    }
}
