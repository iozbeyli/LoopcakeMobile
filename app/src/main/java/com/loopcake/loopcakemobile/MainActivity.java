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
import com.loopcake.loopcakemobile.AsyncCommunication.ImageDownloaderTask;
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

    @Override
    public void successfulExecute(JSONObject jsonObject) {
        JSONArray userDetailArray = null;
        try {
            userDetailArray = jsonObject.getJSONArray("details");
            JSONObject userDetails = userDetailArray.getJSONObject(0);
            Log.d("User",userDetails.toString());
            String email = (String)userDetails.get("email");
            String name = (String)userDetails.get("name");
            String surname = (String)userDetails.get("surname");
            String type = (String)userDetails.get("type");
            String universityID = (String)userDetails.get("universityID");
            String photoID = (String)userDetails.get("photo");
            String id = (String)userDetails.get("_id");
            User user = new User(id,name,surname,email,type,photoID,universityID);
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
            TextView email = (TextView) headerView.findViewById(R.id.drawer_user_email);
            email.setText(Session.user.email);
            TextView name = (TextView) headerView.findViewById(R.id.drawer_user_name);
            name.setText(Session.user.name+" "+Session.user.surname);
            ImageView iv = (ImageView) headerView.findViewById(R.id.drawer_user_photo);
            ImageDownloaderTask task = new ImageDownloaderTask(iv, Session.user.photoID,getBaseContext());
            task.execute((String[]) null);
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
