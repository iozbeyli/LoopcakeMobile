package com.loopcake.loopcakemobile;

import java.util.List;
        import java.util.Map;

        import com.loopcake.loopcakemobile.R;

        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.graphics.Typeface;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.view.ViewGroup;
        import android.widget.BaseExpandableListAdapter;
        import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> laptopCollections;
    private List<String> laptops;
    private List<String> dates;
    private List<Integer> progress;

    public ExpandableListAdapter(Activity context, List<String> laptops, List<String> dates,
                                 Map<String, List<String>> laptopCollections) {
        this.context = context;
        this.laptopCollections = laptopCollections;
        this.laptops = laptops;
        this.dates = dates;
    }

    public Object getChild(int groupPosition, int childPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).get(childPosition);
    }

    public void setProgress(List<Integer> progress){
        this.progress = progress;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }



    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            if(progress == null){
                convertView = inflater.inflate(R.layout.announcement_child_item, null);
            }else {
                convertView = inflater.inflate(R.layout.project_child_item, null);
            }
        }

        TextView item = (TextView) convertView.findViewById(R.id.announcementContent);

        /*ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                List<String> child =
                                        laptopCollections.get(laptops.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });*/

        item.setText(laptop);
        if(progress != null){
            int progress = getProgress(groupPosition);
            ProgressBar progressItem = (ProgressBar) convertView.findViewById(R.id.projectProgressBar);
            progressItem.setProgress(progress);
        }
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return laptopCollections.get(laptops.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition) {
        return laptops.get(groupPosition);
    }

    public Object getDate(int groupPosition) {
        return dates.get(groupPosition);
    }

    public int getGroupCount() {
        return laptops.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String laptopName = (String) getGroup(groupPosition);
        String date = (String) getDate(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.announcement_group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.announcement);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        TextView dateItem = (TextView) convertView.findViewById(R.id.announcementDate);
        dateItem.setText(date);

        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getProgress(int groupPosition){
        return progress.get(groupPosition);
    }
}