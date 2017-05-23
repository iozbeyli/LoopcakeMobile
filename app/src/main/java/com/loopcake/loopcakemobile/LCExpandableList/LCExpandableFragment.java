package com.loopcake.loopcakemobile.LCExpandableList;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.PostDatas.AnnouncementPostDatas;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.ViewControllers.ViewController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public abstract class LCExpandableFragment<T> extends Fragment implements LCExpandableCarrier<T>{

    protected ExpandableListView expandableListView;
    protected ViewController.LoaderController loaderController;
    protected View layout;
    protected View progressBar;
    protected View expandableList;
    protected ArrayList<T> items;

    public LCExpandableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_expandable_list, container, false);
        progressBar = layout.findViewById(R.id.expandable_progress);
        expandableList = layout.findViewById(R.id.expandable_list);
        Log.d("Expandab", "onCreateView: "+expandableList.toString());
        loaderController = new ViewController.LoaderController(progressBar,expandableList,getActivity());
        fillList();
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public abstract void fillList();

    public void displayList(int groupLayout,int childLayout){
        expandableListView = (ExpandableListView) expandableList;
        LCExpandableAdapter expAdt = new LCExpandableAdapter(getActivity(),this,items,groupLayout,childLayout);
        expandableListView.setAdapter(expAdt);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                onChildClicked(items.get(groupPosition));
                return true;
            }
        });
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                onGroupClicked(items.get(groupPosition));
                return false;
            }
        });

    }

    @Override
    public abstract void setGroupView(View groupView, T item);

    @Override
    public abstract void setChildView(View childView, T item);

    public abstract void onGroupClicked(T item);

    public abstract void onChildClicked(T item);

}
