package com.loopcake.loopcakemobile.ViewControllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * Created by Melih on 20.05.2017.
 */

public class ViewController {
    public static class LoaderController{
        private View progressBar;
        private Activity activity;
        private View rest;
        public LoaderController(View progressBar,View rest, Activity activity){
            this.progressBar = progressBar;
            this.activity=activity;
            this.rest=rest;
        }
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        public void showProgress(final boolean show) {
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
                rest.setVisibility(show ? View.GONE : View.VISIBLE);
                rest.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rest.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
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
                rest.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
    }
}
