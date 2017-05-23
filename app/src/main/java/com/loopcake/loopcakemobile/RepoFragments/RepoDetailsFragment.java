package com.loopcake.loopcakemobile.RepoFragments;


import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.R;

/**
 * Created by Melih on 23.05.2017.
 */

public class RepoDetailsFragment extends LCFragment {

    public RepoDetailsFragment(){
        layoutID = R.layout.fragment_repo_details;
    }

    @Override
    public void mainFunction() {
        RepoStudentListFragment fragment = new RepoStudentListFragment();
        FragmentTransaction ft =getChildFragmentManager().beginTransaction();
        ft.replace(R.id.repo_students_frame, fragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
