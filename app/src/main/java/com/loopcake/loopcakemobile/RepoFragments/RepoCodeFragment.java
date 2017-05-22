package com.loopcake.loopcakemobile.RepoFragments;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

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
        webView.getSettings().setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new WebAppInterface(getActivity()), "android");
        webView.loadUrl("file:///android_asset/repoCodeEditor.html");
    }

    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
