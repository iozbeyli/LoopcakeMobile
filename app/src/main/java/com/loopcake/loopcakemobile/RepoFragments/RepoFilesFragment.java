package com.loopcake.loopcakemobile.RepoFragments;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.R;

/**
 * Created by Melih on 23.05.2017.
 */

public class RepoFilesFragment extends LCFragment {
    public RepoFilesFragment(){
        layoutID= R.layout.fragment_repo_files;
    }
    @Override
    public void mainFunction() {
        //AsyncCommunicationTask repoFilesComm = new AsyncCommunicationTask(Constants.getFileListURL)
    }
}
