package com.loopcake.loopcakemobile;

/**
 * Created by Melih on 23.04.2017.
 */

public class Constants {
    public final static String apiURL = "http://207.154.203.163:8000/api/";
    //public final static String apiURL = "http://172.20.156.37:8000/api/";
    public final static String gitAPIURL="http://207.154.203.163:9560/api/";

    public final static String getGroupURL = apiURL+"getGroup";
    public final static String getRepoURL = apiURL+"getRepo";
    public final static String getCourseURL = apiURL+"course";
    public final static String getStudentURL = apiURL+"getStudents";
    public final static String getAnnounceURL = apiURL+"getAnnounce";

    public final static String getFileListURL = gitAPIURL+"list";
}
