package com.loopcake.loopcakemobile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Enumerators.Enumerators;
import com.loopcake.loopcakemobile.ListContents.CourseContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,CourseListFragment.OnListFragmentInteractionListener,Communicator {

    AsyncCommunicationTask userDataTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Session.loggedin==false){
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }else {
            setContentView(R.layout.activity_main);
            if(Session.user==null){
                JSONObject postData = new JSONObject();
                try {
                    postData.put("operation","1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                userDataTask = new AsyncCommunicationTask(Constants.apiURL+"user",postData,this);
                userDataTask.execute((Void)null);
            }else{
                setDrawerUserInfo();
            }

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            Fragment fragment = AnnouncementFragment.newInstance(Enumerators.AnnouncementType.STUDENT);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_main, fragment, "visible_fragment");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
        } else if (id == R.id.nav_project) {
            fragment = new ProjectListFragment();
        }/* else if (id == R.id.nav_auth) {

        } else if (id == R.id.nav_send) {

        }*/
        else{
            fragment = new AnnouncementFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_main, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(CourseContent.CourseItem item) {
        Log.d("item","clicked");
        Session.selectedID = item.details;
        Intent intent = new Intent(this,CourseActivity.class);
        startActivity(intent);
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        JSONArray userDetailArray = null;
        try {
            userDetailArray = jsonObject.getJSONArray("details");
            JSONObject userDetails = userDetailArray.getJSONObject(0);
            String email = (String)userDetails.get("email");
            Log.d("email",email);
            String name = (String)userDetails.get("name");
            Log.d("name",name);
            String surname = (String)userDetails.get("surname");
            Log.d("surname",surname);
            String type = (String)userDetails.get("type");
            Log.d("type",type);
            String universityID = (String)userDetails.get("universityID");
            Log.d("university",universityID);
            String photoID = (String)userDetails.get("photo");
            Log.d("photo",photoID);
            User user = new User(name,surname,email,type,photoID,universityID);
            Session.user=user;
            setDrawerUserInfo();
            GetUserImage userImage = new GetUserImage(photoID);
            userImage.execute((Void)null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failedExecute() {

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
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            Log.d("image error",e.toString());
            return null;
        }
    }
    public void setUserImage(Drawable userImage){
        ImageView iv = (ImageView) findViewById(R.id.drawer_user_photo);
        iv.setImageDrawable(userImage);
    }

    public class GetUserImage extends AsyncTask<Void,Void,Boolean>{
        String photoID;
        public GetUserImage(String photoID){
            this.photoID=photoID;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            Drawable userImage = LoadImageFromWebOperations(Constants.apiURL+"download?_id="+photoID);
            //setUserImage(userImage);
            return null;
        }
    }
}
