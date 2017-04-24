package com.loopcake.loopcakemobile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnnouncementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnnouncementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnnouncementFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ExpandableListView expandableListView;
    private List<String> groupList;
    private List<String> dateList;
    private List<String> childList;
    private Map<String, List<String>> laptopCollection;
    private View progressBar;
    private UserLoginTask mAuthTask = null;
    private View layout;
    private JSONObject postData=null;
    public AnnouncementFragment() {
        // Required empty public constructor
    }

    public void prepareForCourse(){
        postData = new JSONObject();
        try {
            postData.put("course",Session.selectedID);
            postData.put("operation","1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnnouncementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnnouncementFragment newInstance(String param1, String param2) {
        AnnouncementFragment fragment = new AnnouncementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        layout = inflater.inflate(R.layout.fragment_announcement, container, false);

        progressBar = layout.findViewById(R.id.announcement_progress);
        mAuthTask = new UserLoginTask();
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
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }


    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        String resultsToDisplay = "";
        UserLoginTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String apiURL = "http://207.154.203.163:8000/api/getAnnounce";

            InputStream in = null;
            try {
                URL url = new URL(apiURL);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.addRequestProperty("Authorization", "Bearer " + Session.token);
                urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

                System.out.print(urlConnection.toString());
                if(postData==null){
                    postData=new JSONObject();
                    postData.put("operation","2");
                }
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(postData.toString());
                out.close();

                urlConnection.connect();

                in = new BufferedInputStream(urlConnection.getInputStream());
                // Simulate network access.
                //Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println(e.getMessage());

                return false;
            }

            resultsToDisplay = getStringFromInputStream(in);
            //to [convert][1] byte stream to a string
            System.out.print(resultsToDisplay);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            System.out.println(resultsToDisplay);

            try {
                JSONObject jsonObject = new JSONObject(resultsToDisplay);
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

                    /*Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                    startActivity(intent);*/
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }*/
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();

        }
    }

}
