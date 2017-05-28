package com.loopcake.loopcakemobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.AsyncCommunication.NotificationHandler;
import com.loopcake.loopcakemobile.CourseFragments.CourseDetailFragment;
import com.loopcake.loopcakemobile.CourseFragments.CourseStudentFragment;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;
import static com.loopcake.loopcakemobile.Enumerators.Enumerators.CourseActions.ADD_STUDENTS;
import static com.loopcake.loopcakemobile.Enumerators.Enumerators.CourseActions.CREATE_ANNOUNCEMENT;
import static com.loopcake.loopcakemobile.Enumerators.Enumerators.CourseActions.CREATE_PROJECT;
import static com.loopcake.loopcakemobile.Enumerators.Enumerators.CourseActions.EDIT_COURSE;

public class CourseActivity extends LCTabbedActivity {

    private static final String TAG = "CourseActivity";

    @Override
    public void onCreateFunction() {


    }

    @Override
    public SectionsPagerAdapter createSectionPagerAdapter(){
        setTitle(Session.selectedCourse.name);
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> pageTitles = new ArrayList<>();
        fragments.add(new CourseDetailFragment());
        AnnouncementFragment announcementFragment = AnnouncementFragment.newInstance(Enumerators.AnnouncementType.COURSE);
        fragments.add(announcementFragment);
        CourseStudentFragment studentListFragment = new CourseStudentFragment();
        fragments.add(studentListFragment);
        pageTitles.add("Details");
        pageTitles.add("Announce");
        pageTitles.add("Students");
        Log.d(TAG, "primary: "+Constants.colorPrimary);
        return new SectionsPagerAdapter(getSupportFragmentManager(),fragments,pageTitles);
    }

    @Override
    public ArrayList<ArrayList<String>> setTextListsForFragments() {
        return createFABTexts(new String[][]{
                {"Edit Course", "Create Project", null},
                {"Create Announcement", null, null},
                {"Add Students", null, null}
        });

    }

    @Override
    public ArrayList<ArrayList<View.OnClickListener>> setListenerListsForFragments() {
        return createFABListeners(new Enumerators.CourseActions[][]{
                {EDIT_COURSE, CREATE_PROJECT, null},
                {CREATE_ANNOUNCEMENT, null, null},
                {ADD_STUDENTS, null, null}
        });
    }

    private View.OnClickListener createFABListener(final Enumerators.CourseActions act){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(act == ADD_STUDENTS){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                    TextView title = new TextView(CourseActivity.this);
                    title.setText("Write Student E-Mails");
                    title.setGravity(Gravity.CENTER);
                    title.setTextColor(Constants.colorPrimary);
                    title.setTypeface(null, BOLD);
                    title.setPadding(50,50,50,50);
                    title.setTextSize(20);

                    builder.setCustomTitle(title);
                    final EditText input = new EditText(CourseActivity.this);
                    input.setHint("Seperate with , for multiple students");
                    builder.setView(input);
                    builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            JSONObject post = new JSONObject();
                            try {
                                post.put("students",input.getText().toString());
                                post.put("courseid",Session.selectedCourse.courseid);
                                if(!post.isNull("students") && !post.getString("students").equals("")) {
                                    AsyncCommunicationTask task = new AsyncCommunicationTask(Constants.apiURL + "/addStudents",
                                            post, new Communicator() {
                                        @Override
                                        public void successfulExecute(JSONObject jsonObject) {
                                            Log.d(TAG, "successfulExecute: "+ jsonObject);
                                            dialog.cancel();
                                        }
                                        @Override
                                        public void failedExecute() {

                                            Log.wtf(TAG, "failedExecution");
                                        }
                                    });
                                    task.execute((Void) null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }else{
                    Intent in = new Intent(CourseActivity.this, SubCourseActivity.class);
                    in.putExtra("fragment", act);
                    startActivity(in);
                }
            }
        };
    }

    private ArrayList<ArrayList<View.OnClickListener>> createFABListeners(Enumerators.CourseActions[][] act){
        ArrayList<ArrayList<View.OnClickListener>> fragmentListenerLists = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {
            ArrayList<View.OnClickListener> listeners = new ArrayList<>();
            for (int j = 0; j < 3 ; j++) {
                if(act[i][j] != null)
                    listeners.add(createFABListener(act[i][j]));
            }
            fragmentListenerLists.add(listeners);
        }
        return fragmentListenerLists;
    }


    private ArrayList<ArrayList<String>> createFABTexts(String[][] lab){
        ArrayList<ArrayList<String>> labelList = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {
            ArrayList<String> labels = new ArrayList<>();
            for (int j = 0; j < 3 ; j++) {
                if(lab[i][j] != null)
                    labels.add(lab[i][j]);
            }
            labelList.add(labels);
        }

        return labelList;
    }



}
