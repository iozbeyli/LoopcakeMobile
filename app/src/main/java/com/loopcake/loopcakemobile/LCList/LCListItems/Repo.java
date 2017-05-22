package com.loopcake.loopcakemobile.LCList.LCListItems;

/**
 * Created by Melih on 21.05.2017.
 */

public class Repo {
    public final String id;
    public final String content;
    public final String details;

    public Repo(String id, String content, String details) {
        this.id = id;
        this.content = content;
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}
