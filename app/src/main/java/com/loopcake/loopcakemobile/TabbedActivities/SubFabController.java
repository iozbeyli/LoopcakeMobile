package com.loopcake.loopcakemobile.TabbedActivities;

import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import com.loopcake.loopcakemobile.R;

import java.util.ArrayList;

/**
 * Created by Melih on 23.05.2017.
 */

public class SubFabController {
    private int currentNumberOfFabs;
    private boolean fabsOpen=false;
    private ArrayList<ArrayList<String>> textListForFragments;
    private ArrayList<ArrayList<View.OnClickListener>> listenerListForFragments;
    private ArrayList<FloatingActionButton> floatingActionButtons;
    private ArrayList<TextView> textOfFabs;
    private ArrayList<View> subFabLayouts;
    private View subFabsView;
    public SubFabController(ArrayList<FloatingActionButton> floatingActionButtons, ArrayList<TextView> textOfFabs,ArrayList<View> subFabLayouts,View subFabView){
        this.floatingActionButtons=floatingActionButtons;
        this.textOfFabs=textOfFabs;
        this.subFabLayouts =subFabLayouts;
        this.subFabsView=subFabView;
        closeSubFabs();
    }

    public void closeSubFabs(){
        for(int i=0;i<floatingActionButtons.size();i++){
            subFabLayouts.get(i).setVisibility(View.VISIBLE);
        }
        subFabsView.setVisibility(View.GONE);
    }


    public void openSubFabs(){
        for(int i=0;i<floatingActionButtons.size();i++){
            subFabLayouts.get(i).setVisibility(View.GONE);
        }
        subFabsView.setVisibility(View.VISIBLE);
    }

    public void setSubFabs(int fragmentIndex){
        closeSubFabs();
        fabsOpen=false;

        ArrayList<String> texts= textListForFragments.get(fragmentIndex);
        ArrayList<View.OnClickListener> listeners = listenerListForFragments.get(fragmentIndex);
        currentNumberOfFabs=texts.size();
        int numberOfFabs=texts.size();
        for(int i = 0;i<texts.size();i++){
            textOfFabs.get(i).setText(texts.get(i));
            floatingActionButtons.get(i).setOnClickListener(listeners.get(i));
        }
    }


    private void openSub(int index){
        subFabLayouts.get(index).setVisibility(View.VISIBLE);
    }

    public void openCloseFabs(){
        if(!fabsOpen){
            openSubFabs();
            for(int i=0;i<currentNumberOfFabs;i++){
                openSub(i);
            }
        }else{
            closeSubFabs();
        }
        fabsOpen=!fabsOpen;
    }

    public void setTextListForFragments(ArrayList<ArrayList<String>> textListForFragments){
        this.textListForFragments = textListForFragments;
    }

    public void setListenerListForFragments(ArrayList<ArrayList<View.OnClickListener>> listenerListForFragments){
        this.listenerListForFragments=listenerListForFragments;
    }

    public ArrayList<String> getFragmentTextList(int position){
        return textListForFragments.get(position);
    }

    public ArrayList<String> getFragmentListenerList(int position){
        return textListForFragments.get(position);
    }




}
