package com.loopcake.loopcakemobile;

import com.loopcake.loopcakemobile.LCList.LCListItems.Course;
import com.loopcake.loopcakemobile.LCList.LCListItems.Repo;
import com.loopcake.loopcakemobile.RepoFragments.LCFile;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Melih on 22.04.2017.
 */

public class Session {
   public static boolean loggedin = false;
   public static String token = "";
   public static User user=null;
   public static String selectedID = "";
   public static JSONObject selectedProject;
   public static Course selectedCourse;
   public static Repo selectedRepo;
   public static LCFile selectedFile;
}


