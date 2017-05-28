package com.loopcake.loopcakemobile.LCExpandableList;

/**
 * Created by Melih on 22.05.2017.
 */

public class Announcement {
    public String announcementID;
    public String title;
    public String date;
    public String details;
    public String course_id;
    public Announcement(String announcementID,String title,String date,String details,String course_id){
        this.announcementID = announcementID;
        this.title=title;
        this.date=date;
        this.details=details;
        this.course_id = course_id;
    }
}
