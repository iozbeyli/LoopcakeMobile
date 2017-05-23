package com.loopcake.loopcakemobile.LCList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopcake.loopcakemobile.R;

import java.util.List;

/**
 * Created by Melih on 21.05.2017.
 */

public class LCRecyclerViewAdapter<T> extends RecyclerView.Adapter<LCRecyclerViewAdapter.ViewHolder> {

    private final List<T> values;
    private final LCListCarrier listener;
    private final int itemLayoutID;

    public LCRecyclerViewAdapter(List<T> values,LCListCarrier listener,int itemLayoutID) {
        this.values = values;
        this.listener = listener;
        this.itemLayoutID = itemLayoutID;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayoutID, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LCRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.item = values.get(position);
        listener.setItemContent(holder.item,holder.view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    listener.listItemPressed(holder.item,holder.view);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public T item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

        }
    }
}
