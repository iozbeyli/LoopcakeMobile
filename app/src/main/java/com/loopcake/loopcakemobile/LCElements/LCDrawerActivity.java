package com.loopcake.loopcakemobile.LCElements;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AnnouncementFragment;
import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.CourseActivity;
import com.loopcake.loopcakemobile.CourseListFragment;
import com.loopcake.loopcakemobile.LoginActivity;
import com.loopcake.loopcakemobile.ProjectListFragment;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.RepoFragments.RepoCodeFragment;
import com.loopcake.loopcakemobile.RepoListFragment;
import com.loopcake.loopcakemobile.Session;
import com.loopcake.loopcakemobile.SubRepoActivity;
import com.loopcake.loopcakemobile.TwoFactorAuthentication.TwoFactorAuthenticationFragment;

import java.util.ArrayList;

public abstract class LCDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,Communicator {

    AsyncCommunicationTask userDataTask;
    protected Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    protected NavigationView navigationView;
    protected View headerView;
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

        if(Session.loggedin==false){
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }else {
            setContentView(R.layout.activity_main);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

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

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            headerView=navigationView.getHeaderView(0);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new AnnouncementFragment();
            ft.replace(R.id.frame_main, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
            closeSubFabs();
            onCreateFunction();
        }
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(this,CourseActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        if (id == R.id.nav_course) {
            fragment = new CourseListFragment();
        } else if (id == R.id.nav_home) {
            fragment = new AnnouncementFragment();
        } else if (id == R.id.nav_repo) {
            fragment = new RepoListFragment();
            //fragment=new RepoCodeFragment();
        } else if (id == R.id.nav_project) {
            fragment = new ProjectListFragment();
        }else if(id == R.id.nav_auth){
            fragment = new TwoFactorAuthenticationFragment();
        }else{
            fragment = new AnnouncementFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_main, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setDrawerUserInfo(){
        if(Session.user!=null){
            TextView email = (TextView) findViewById(R.id.drawer_user_email);
            if(email!=null){
                email.setText(""+Session.user.email);
            }
            TextView name = (TextView) findViewById(R.id.drawer_user_name);
            if(name!=null){
                name.setText(Session.user.name+" "+Session.user.surname);
            }
        }
    }

}
