package com.loopcake.loopcakemobile;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.CourseFragments.CourseDetailFragment;
import com.loopcake.loopcakemobile.CourseFragments.CourseStudentFragment;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;
import com.loopcake.loopcakemobile.TabbedActivities.SubFabController;

import java.util.ArrayList;

public abstract class LCTabbedActivity extends AppCompatActivity{


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    protected FloatingActionButton fabMain;
    private ArrayList<FloatingActionButton> subFabs;
    private ArrayList<TextView> subFabTexts;
    private ArrayList<View> subFabLayouts;
    private int numberOfFabs=0;
    private View subFabsView;
    private boolean fabsOpen=false;
    protected int selectedTab;
    protected SubFabController subFabController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onCreate(savedInstanceState);
        subFabs = new ArrayList<FloatingActionButton>();
        subFabTexts = new ArrayList<>();
        subFabLayouts = new ArrayList<>();
        setContentView(R.layout.activity_tabbed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickMainFab();
            }
        });

        subFabs.add ((FloatingActionButton) findViewById(R.id.fabSub1));
        subFabs.add ((FloatingActionButton) findViewById(R.id.fabSub2));
        subFabs.add((FloatingActionButton) findViewById(R.id.fabSub3));
        subFabTexts.add((TextView) findViewById(R.id.fabSub1Text));
        subFabTexts.add((TextView) findViewById(R.id.fabSub2Text));
        subFabTexts.add((TextView) findViewById(R.id.fabSub3Text));
        subFabLayouts.add(findViewById(R.id.fabSub1Layout));
        subFabLayouts.add(findViewById(R.id.fabSub2Layout));
        subFabLayouts.add(findViewById(R.id.fabSub3Layout));
        subFabsView = findViewById(R.id.fabSubs);
        subFabController = new SubFabController(subFabs,subFabTexts,subFabLayouts,subFabsView);
        subFabController.setTextListForFragments(setTextListsForFragments());
        subFabController.setListenerListForFragments(setListenerListsForFragments());
        subFabController.setSubFabs(0);
        setListenerListsForFragments();
        if(!(this instanceof Communicator)){
            setTabView();
        }
        onCreateFunction();


    }

    public abstract void onCreateFunction();

   /* private void closeSubFabs(){
        subFabController.closeSubFabs();
    }

    private void openSubFabs(){
        subFabController.openSubFabs();
    }*/

    /*public void setSubFabs(ArrayList<ArrayList<String>> texts,ArrayList<ArrayList<View.OnClickListener>> listeners){
        closeSubFabs();
        numberOfFabs=texts.size();
        for(int i = 0;i<texts.size();i++){
            subFabTexts[i].setText(texts.get(i));
            subFabs[i].setOnClickListener(listeners.get(i));
        }
    }*/

    /*private void openSub(int index){
        subFabLayouts[index].setVisibility(View.VISIBLE);
    }*/

    private void clickMainFab(){
        subFabController.openCloseFabs();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public abstract SectionsPagerAdapter createSectionPagerAdapter();

    public void setTabView(){
        mSectionsPagerAdapter = createSectionPagerAdapter();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                selectedTab = tab.getPosition();
                subFabController.setSubFabs(selectedTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public abstract ArrayList<ArrayList<String>> setTextListsForFragments();

    public abstract ArrayList<ArrayList<View.OnClickListener>> setListenerListsForFragments();

}
