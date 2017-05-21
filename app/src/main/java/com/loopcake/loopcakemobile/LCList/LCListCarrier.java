package com.loopcake.loopcakemobile.LCList;

import android.view.View;

/**
 * Created by Melih on 21.05.2017.
 */

public interface LCListCarrier<T> {
    void listItemPressed(T listItem);
    void setItemContent(T item,View itemView);
}
