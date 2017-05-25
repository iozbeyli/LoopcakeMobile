package com.loopcake.loopcakemobile.LCExpandableList;

import android.util.Pair;

import java.util.HashMap;

/**
 * Created by Melih on 22.05.2017.
 */

public class Project {
    public String id;
    public String title;
    public String deadline;
    public HashMap<String,Pair<Boolean,Integer>> checklist;
    public String course;
    public int maxSize;

    public Project(String id, String title, String deadline, HashMap<String,Pair<Boolean,Integer>> checklist,String course){
        this.id=id;
        this.deadline=deadline;
        this.title = title;
        this.checklist=checklist;
        this.course=course;
    }

    public int getPoints(){
        int temp = 0;
        for (Pair<Boolean,Integer> value: checklist.values()) {
            temp+=value.second;
        }
        return temp;
    }
}
