package com.loopcake.loopcakemobile;

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
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.CourseFragments.CourseDetailFragment;
import com.loopcake.loopcakemobile.CourseFragments.CourseStudentFragment;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.TabbedActivities.SectionsPagerAdapter;

import java.util.ArrayList;

public abstract class LCTabbedActivity extends AppCompatActivity{


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    protected FloatingActionButton fabMain;
    private FloatingActionButton[] subFabs;
    private TextView[] subFabTexts;
    private View[] subFabLayouts;
    private int numberOfFabs=0;
    private boolean fabsOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subFabs = new FloatingActionButton[3];
        subFabTexts = new TextView[3];
        subFabLayouts = new View[3];
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

        subFabs[0] = (FloatingActionButton) findViewById(R.id.fabSub1);
        subFabs[1] = (FloatingActionButton) findViewById(R.id.fabSub2);
        subFabs[2] = (FloatingActionButton) findViewById(R.id.fabSub3);
        subFabTexts[0] = (TextView) findViewById(R.id.fabSub1Text);
        subFabTexts[1] = (TextView) findViewById(R.id.fabSub2Text);
        subFabTexts[2] = (TextView) findViewById(R.id.fabSub3Text);
        subFabLayouts[0] =  findViewById(R.id.fabSub1Layout);
        subFabLayouts[1] =  findViewById(R.id.fabSub2Layout);
        subFabLayouts[2] =  findViewById(R.id.fabSub3Layout);
        if(!(this instanceof Communicator)){
            setTabView();
        }
        onCreateFunction();


    }

    public abstract void onCreateFunction();

    private void closeSubFabs(){
        for(int i=0;i<subFabs.length;i++){
            subFabLayouts[i].setVisibility(View.VISIBLE);
        }
        View subFabsView = findViewById(R.id.fabSubs);
        subFabsView.setVisibility(View.GONE);
    }

    private void openSubFabs(){
        for(int i=0;i<subFabs.length;i++){
            subFabLayouts[i].setVisibility(View.GONE);
        }
        View subFabsView = findViewById(R.id.fabSubs);
        subFabsView.setVisibility(View.VISIBLE);
    }

    public void setSubFabs(ArrayList<String> texts,ArrayList<View.OnClickListener> listeners){
        closeSubFabs();
        numberOfFabs=texts.size();
        for(int i = 0;i<texts.size();i++){
            subFabTexts[i].setText(texts.get(i));
            subFabs[i].setOnClickListener(listeners.get(i));
        }
    }

    private void openSub(int index){
        subFabLayouts[index].setVisibility(View.VISIBLE);
    }

    private void clickMainFab(){
        if(!fabsOpen){
            openSubFabs();
            for(int i=0;i<numberOfFabs;i++){
                openSub(i);
            }
        }else{
            closeSubFabs();
        }
        fabsOpen=!fabsOpen;
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
    }

}
