package com.loopcake.loopcakemobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.PostDatas.PostDatas;
import com.loopcake.loopcakemobile.ViewControllers.ViewController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class AnnouncementFragment extends Fragment implements Communicator {

    private OnFragmentInteractionListener mListener;

    private ExpandableListView expandableListView;
    private List<String> groupList;
    private List<String> dateList;
    private List<String> childList;
    private Map<String, List<String>> laptopCollection;
    private View progressBar;
    private ViewController.LoaderController loaderController;
    private AsyncCommunicationTask mAuthTask = null;
    private View layout;
    private JSONObject postData=null;
    private Enumerators.AnnouncementType announcementType;

    public AnnouncementFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AnnouncementFragment newInstance(Enumerators.AnnouncementType type) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        fragment.announcementType= type;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_announcement, container, false);

        progressBar = layout.findViewById(R.id.announcement_progress);
        loaderController = new ViewController.LoaderController(progressBar,getActivity());
        loaderController.showProgress(true);
        mAuthTask = new AsyncCommunicationTask(Constants.apiURL+"getAnnounce", PostDatas.AnnouncementPostDatas.getAnnouncementPostData(announcementType),this);
        mAuthTask.execute((Void) null);

        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        try {
            JSONArray announcements = jsonObject.getJSONArray("details");
            Boolean successBool = (Boolean)jsonObject.get("success");
            JSONObject announcement = announcements.getJSONObject(0);
            String title = (String)announcement.get("title");
            Log.d("title",title);
            if(successBool){
                createAnnouncementList(announcements);
                createCollection(announcements);
                expandableListView = (ExpandableListView) layout.findViewById(R.id.announcementList);
                final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(getActivity(),groupList,dateList,laptopCollection);
                expandableListView.setAdapter(expListAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void createAnnouncementList(JSONArray announcements) {
        groupList = new ArrayList<String>();
        dateList = new ArrayList<>();
        for(int i=0;i<announcements.length();i++){
            try {
                groupList.add(announcements.getJSONObject(i).getString("title"));
                dateList.add(announcements.getJSONObject(i).getString("date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void createCollection(JSONArray announcements) {
        laptopCollection = new LinkedHashMap<String, List<String>>();
        for(int i=0;i<announcements.length();i++){
            try {
                String title = announcements.getJSONObject(i).getString("title");
                String content = announcements.getJSONObject(i).getString("content");
                List<String> contentArray = new ArrayList<>();
                contentArray.add(content);
                laptopCollection.put(title,contentArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }

}
