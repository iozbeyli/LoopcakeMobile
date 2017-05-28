package com.loopcake.loopcakemobile.RepoFragments;

import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.LCList.LCListItems.Repo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Melih on 23.05.2017.
 */

public class LCFile {
    public String name;
    public ArrayList<LCFile> children;
    public Enumerators.FileType fileType;
    public boolean listed=false;
    public String path;
    public String code;
    public String repo_id;
    public String branch_name;
    public String json;
    public LCFile parent;
    private LCFile(String name,String repo_id,String branch_name){
        this.name=name;
        this.repo_id = repo_id;
        this.branch_name=branch_name;
    }

    private void setChildren(JSONArray childrenJSONArray){
        children=new ArrayList<>();
        for(int i = 0;i<childrenJSONArray.length();i++){
            try {
                LCFile child = LCFile.newLCFile(childrenJSONArray.getJSONObject(i),this,this.repo_id,this.branch_name);
                if(child!=null){
                    children.add(child);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static LCFile newLCFile(JSONObject fileJSONObject,LCFile parent,String repo_id,String branch_name){
        try {
            String name = fileJSONObject.getString("name");
            LCFile tempLCFile = new LCFile(name,repo_id,branch_name);
            tempLCFile.json = fileJSONObject.toString();
            tempLCFile.parent = parent;
            if(parent!=null){
                tempLCFile.path=parent.path+"/"+name;
            }else{
                tempLCFile.path=name;
            }
            JSONArray children = fileJSONObject.getJSONArray("children");
            if(children.length()==0){
                tempLCFile.fileType= Enumerators.FileType.FILE;
            }else{
                tempLCFile.fileType = Enumerators.FileType.FOLDER;
                tempLCFile.setChildren(fileJSONObject.getJSONArray("children"));
            }
            return tempLCFile;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "LCFile{" +
                "name='" + name + '\'' +
                ", children=" + children +
                ", fileType=" + fileType +
                ", listed=" + listed +
                ", path='" + path + '\'' +
                ", code='" + code + '\'' +
                ", repo_id='" + repo_id + '\'' +
                ", branch_name='" + branch_name + '\'' +
                ", json='" + json + '\'' +
                ", parent=" + parent +
                '}';
    }
}
