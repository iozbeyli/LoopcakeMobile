package com.loopcake.loopcakemobile.LCList.LCListItems;

/**
 * Created by Melih on 21.05.2017.
 */

public class Course {
    public final String id;
    public final String title;
    public final String details;

    public Course(String id, String title, String details) {
        this.id = id;
        this.title = title;
        this.details = details;
    }

    @Override
    public String toString() {
        return title;
    }
}
