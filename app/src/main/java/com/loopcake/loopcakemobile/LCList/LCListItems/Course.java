package com.loopcake.loopcakemobile.LCList.LCListItems;

/**
 * Created by Melih on 21.05.2017.
 */

public class Course {
    public String id;
    public String name;
    public String courseid;
    public String code;
    public String langs;
    public String details;
    public String instructor;

    public Course(String id, String name, String courseid, String code, String langs, String details, String instructor) {
        this.id = id;
        this.name = name;
        this.courseid = courseid;
        this.code = code;
        this.langs = langs;
        this.details = details;
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return name;
    }
}
