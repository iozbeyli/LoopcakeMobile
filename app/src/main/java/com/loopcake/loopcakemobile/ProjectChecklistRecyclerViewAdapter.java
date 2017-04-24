package com.loopcake.loopcakemobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.loopcake.loopcakemobile.ListContents.ChecklistItem;
import com.loopcake.loopcakemobile.ProjectChecklistFragment.OnListFragmentInteractionListener;
import com.loopcake.loopcakemobile.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProjectChecklistRecyclerViewAdapter extends RecyclerView.Adapter<ProjectChecklistRecyclerViewAdapter.ViewHolder> {

    private final List<ChecklistItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ProjectChecklistRecyclerViewAdapter(List<ChecklistItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_projectchecklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getDetail());
        holder.mContentView.setChecked(mValues.get(position).getStatus());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final Switch mContentView;
        public ChecklistItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (Switch) view.findViewById(R.id.checklist_switch);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
