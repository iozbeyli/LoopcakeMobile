package com.loopcake.loopcakemobile.RepoFragments;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.R;

/**
 * Created by Melih on 22.05.2017.
 */

public class RepoCodeFragment extends LCFragment {

    WebView webView;
    JavascriptInterface JSInterface;

    public RepoCodeFragment(){
        layoutID = R.layout.fragment_repo_code;
    }

    @Override
    public void mainFunction() {
        webView = (WebView) layout.findViewById(R.id.repo_code_web_view);

    }
}
