package com.loopcake.loopcakemobile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.ListContents.CourseContent;
import com.loopcake.loopcakemobile.ListContents.CourseContent.CourseItem;

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
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CourseListFragment extends Fragment implements Communicator{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private View layout;
    private CourseListTask courseListTask;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CourseListFragment newInstance(int columnCount) {
        CourseListFragment fragment = new CourseListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_item_list, container, false);

        courseListTask = new CourseListTask();
        courseListTask.execute((Void) null);
        JSONObject loginData = new JSONObject();
        try {
            loginData.put("operation","3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsyncCommunicationTask asyncCommunicationTask = new AsyncCommunicationTask("http://207.154.203.163:8000/api/course",loginData,this);
        asyncCommunicationTask.execute((Void) null);
        // Set the adapter
       /* if (layout instanceof RecyclerView) {
            Context context = layout.getContext();
            RecyclerView recyclerView = (RecyclerView) layout;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(CourseContent.ITEMS, mListener));
        }*/
        return layout;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(CourseItem item);
    }

    public class CourseListTask extends AsyncTask<Void, Void, Boolean> {

        String resultsToDisplay = "";
        CourseListTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String apiURL = "http://207.154.203.163:8000/api/course";

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
                JSONObject loginData = new JSONObject();
                loginData.put("operation","3");
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(loginData.toString());
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
            courseListTask = null;
            //showProgress(false);
            System.out.println(resultsToDisplay);

            try {
                JSONObject jsonObject = new JSONObject(resultsToDisplay);

                Boolean successBool = (Boolean)jsonObject.get("success");

                if(successBool){
                    JSONArray courses = jsonObject.getJSONArray("details");
                    fillCourseList(courses);

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
            courseListTask = null;
          //  showProgress(false);
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

    public void fillCourseList(JSONArray courses){
        CourseContent.clearItems();
        for(int i=0;i<courses.length();i++){
            JSONObject course = null;
            try {
                course = courses.getJSONObject(i);
                String courseTitle = course.getString("name");
                String courseID = course.getString("_id");
                CourseContent.addItem(new CourseItem(""+i,courseTitle,courseID));
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        if (layout instanceof RecyclerView) {
            Context context = layout.getContext();
            RecyclerView recyclerView = (RecyclerView) layout;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(CourseContent.ITEMS, mListener));
        }
    }

    public void successfulExecute(JSONObject jsonObject){
        Log.d("Communicator","Succesfully Execute");
        Boolean successBool = null;
        try {
            successBool = (Boolean)jsonObject.get("success");
            if(successBool){
            JSONArray courses = jsonObject.getJSONArray("details");
            fillCourseList(courses);
        }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void failedExecute(){

    }
}
