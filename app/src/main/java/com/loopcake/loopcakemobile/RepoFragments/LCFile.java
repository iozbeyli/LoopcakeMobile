package com.loopcake.loopcakemobile.RepoFragments;

import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;

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
    private LCFile(String name){
        this.name=name;
    }

    private void setChildren(JSONArray childrenJSONArray){
        children=new ArrayList<>();
        for(int i = 0;i<childrenJSONArray.length();i++){
            try {
                LCFile child = LCFile.newLCFile(childrenJSONArray.getJSONObject(i),this);
                if(child!=null){
                    children.add(child);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static LCFile newLCFile(JSONObject fileJSONObject,LCFile parent){
        try {

            String name = fileJSONObject.getString("name");
            LCFile tempLCFile = new LCFile(name);
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

}
