package com.loopcake.loopcakemobile.ListContents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Melih on 23.04.2017.
 */

public class RepoContent {
    /**
     * An array of sample (dummy) items.
     */
    public static List<RepoContent.RepoItem> ITEMS = new ArrayList<RepoContent.RepoItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, RepoContent.RepoItem> ITEM_MAP = new HashMap<String, RepoContent.RepoItem>();

    private static final int COUNT = 25;

   /* static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }*/

    public static void clearItems(){
        ITEMS = new ArrayList<RepoContent.RepoItem>();
        ITEM_MAP = new HashMap<String, RepoItem>();
    }

    public static void addItem(RepoContent.RepoItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CourseContent.CourseItem createDummyItem(int position) {
        return new CourseContent.CourseItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class RepoItem {
        public final String id;
        public final String content;
        public final String details;

        public RepoItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
