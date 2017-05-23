package com.loopcake.loopcakemobile.LCList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.R;

import org.json.JSONObject;

import java.util.List;

public abstract class LCListFragment<T> extends Fragment implements LCListCarrier<T>{

    public int mColumnCount = 1;
    public View layout;
    public RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_item_list, container, false);
        recyclerView = (RecyclerView)layout.findViewById(R.id.list);
        fillList();
        return layout;
    }

    protected abstract void fillList();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public abstract void listItemPressed(T listItem,View itemView);

    @Override
    public abstract void setItemContent(T item, View itemView);

    public void displayList(List<T> list, int itemID){
        if(recyclerView instanceof RecyclerView){
            Context context = layout.getContext();
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new LCRecyclerViewAdapter<T>(list,this,itemID));
        }
    }

}
