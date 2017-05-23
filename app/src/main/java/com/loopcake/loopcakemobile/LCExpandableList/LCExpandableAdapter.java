package com.loopcake.loopcakemobile.LCExpandableList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.loopcake.loopcakemobile.R;

import java.util.List;

/**
 * Created by Melih on 22.05.2017.
 */

public class LCExpandableAdapter<T> extends BaseExpandableListAdapter {
    private LCExpandableCarrier carrier;
    private List<T> items;
    private Activity activity;
    private int groupLayout;
    private int childLayout;
    public LCExpandableAdapter(Activity activity,LCExpandableCarrier<T> carrier, List<T> items,int groupLayout,int childLayout){
        this.carrier=carrier;
        this.items=items;
        this.activity=activity;
        this.groupLayout=groupLayout;
        this.childLayout=childLayout;

    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        T item = items.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(groupLayout, null);
        }
        carrier.setGroupView(convertView,items.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
                convertView = inflater.inflate(childLayout, null);
        }
        carrier.setChildView(convertView,items.get(groupPosition),childPosition);
        return convertView;

    }

    @Override
    public int getGroupCount() {
        if(items==null){
            return 0;
        }else{
            return items.size();
        }

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = carrier.getChildrenCount(items.get(groupPosition));
        Log.d(items.get(groupPosition).toString(),""+count);
        return count;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
