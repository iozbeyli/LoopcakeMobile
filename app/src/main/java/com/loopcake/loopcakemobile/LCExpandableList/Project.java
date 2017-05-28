package com.loopcake.loopcakemobile.LCExpandableList;

import android.util.Pair;

import com.loopcake.loopcakemobile.LCList.LCListItems.Course;

import org.json.JSONArray;

import java.util.HashMap;

/**
 * Created by Melih on 22.05.2017.
 */

public class Project {
    public String id;
    public String title;
    public String deadline;
    public String details;
    public HashMap<String,Pair<Boolean,Integer>> checklist;
    public Course course;
    public int maxSize;
    public JSONArray attachments;

    public Project(String id, String title, String deadline,
                   HashMap<String,Pair<Boolean,Integer>> checklist,Course course, String details, JSONArray attachments){
        this.id=id;
        this.deadline=deadline;
        this.title = title;
        this.checklist=checklist;
        this.course =course;
        this.details = details;
        this.attachments = attachments;
    }

    public int getPoints(){
        int temp = 0;
        for (Pair<Boolean,Integer> value: checklist.values()) {
            temp+=value.second;
        }
        return temp;
    }
}
