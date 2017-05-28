package com.loopcake.loopcakemobile;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.LCDatabase.LCDatabaseHelper;
import com.loopcake.loopcakemobile.LCDatabase.LCNetworkChecker;
import com.loopcake.loopcakemobile.LCExpandableList.Announcement;
import com.loopcake.loopcakemobile.LCExpandableList.LCExpandableFragment;
import com.loopcake.loopcakemobile.PostDatas.AnnouncementPostDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class AnnouncementFragment extends LCExpandableFragment<Announcement> implements Communicator{

    private AsyncCommunicationTask mAuthTask = null;
    private Enumerators.AnnouncementType announcementType= Enumerators.AnnouncementType.STUDENT;

    // TODO: Rename and change types and number of parameters
    public static AnnouncementFragment newInstance(Enumerators.AnnouncementType type) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.announcementType= type;
        return fragment;
    }

    @Override
    public void fillList() {
        loaderController.showProgress(true);
        if(LCNetworkChecker.isNetworkConnected(getActivity())){
            mAuthTask = new AsyncCommunicationTask(Constants.getAnnounceURL, AnnouncementPostDatas.getAnnouncementPostData(announcementType),this);
            mAuthTask.execute((Void) null);
        }else{
            loaderController.showProgress(true);
            LCDatabaseHelper helper = new LCDatabaseHelper(getActivity());
            items = helper.getUserAnnouncements();
            loaderController.showProgress(false);
            displayList(R.layout.announcement_group_item,R.layout.announcement_child_item);
        }

    }

    @Override
    public void setGroupView(View groupView, Announcement item) {
        TextView title = (TextView)groupView.findViewById(R.id.announcement);
        TextView date = (TextView)groupView.findViewById(R.id.announcementDate);
        title.setText(item.title);
        date.setText(item.date);
    }

    @Override
    public void setChildView(View childView, Announcement item, int childPosition) {
        TextView content = (TextView) childView.findViewById(R.id.announcementContent);
        content.setText(item.details);
    }

    @Override
    public void onChildClicked(Announcement item) {

    }

    @Override
    public void onGroupClicked(Announcement item) {

    }

    @Override
    public int getChildrenCount(Announcement item) {
        return 1;
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        try {
            JSONArray announcements = jsonObject.getJSONArray("details");
            Boolean successBool = (Boolean)jsonObject.get("success");
            JSONObject announcement = announcements.getJSONObject(0);
            String title = (String)announcement.get("title");
            Log.d("Announcement",jsonObject.toString());
            if(successBool){
                createAnnouncementList(announcements);
                displayList(R.layout.announcement_group_item,R.layout.announcement_child_item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }

    private void createAnnouncementList(JSONArray announcements) {
        items =new ArrayList<>();
        for(int i=0;i<announcements.length();i++){
            try {
                String announcementID = announcements.getJSONObject(i).getString("_id");
                String title =announcements.getJSONObject(i).getString("title");
                String date = announcements.getJSONObject(i).getString("date");
                String content = announcements.getJSONObject(i).getString("content");
                JSONObject course = announcements.getJSONObject(i).getJSONObject("course");
                String course_id = course.getString("_id");
                Announcement announcement = new Announcement(announcementID,title,date,content,course_id);
                items.add(announcement);
                LCDatabaseHelper helper = new LCDatabaseHelper(getActivity());
                helper.insertAnnouncement(announcement);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        loaderController.showProgress(false);
    }

}
