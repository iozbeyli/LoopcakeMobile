package com.loopcake.loopcakemobile;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.LCElements.LCDrawerActivity;
import com.loopcake.loopcakemobile.TwoFactorAuthentication.TwoFactorAuthenticationFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends LCDrawerActivity implements NavigationView.OnNavigationItemSelectedListener,Communicator {

    AsyncCommunicationTask userDataTask;

    @Override
    public void onCreateFunction() {
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
