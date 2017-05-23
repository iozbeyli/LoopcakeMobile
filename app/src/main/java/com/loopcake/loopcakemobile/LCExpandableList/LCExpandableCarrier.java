package com.loopcake.loopcakemobile.LCExpandableList;

import android.view.View;

/**
 * Created by Melih on 22.05.2017.
 */

public interface LCExpandableCarrier<T> {

    void setGroupView(View groupView,T item);
    void setChildView(View childView,T item,int childPosition);
    void onChildClicked(T item);
    void onGroupClicked(T item);
    int getChildrenCount(T item);

}
