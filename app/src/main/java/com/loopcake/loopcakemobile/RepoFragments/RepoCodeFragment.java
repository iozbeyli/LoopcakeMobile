package com.loopcake.loopcakemobile.RepoFragments;

import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.loopcake.loopcakemobile.LCDatabase.LCDatabaseHelper;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.R;
import com.loopcake.loopcakemobile.Session;

/**
 * Created by Melih on 22.05.2017.
 */

public class RepoCodeFragment extends LCFragment {

    private WebView webView;
    private String code = "// It starts out in plain text mode,\n#  use the control below to load and apply a mode\n'you'll see the highlighting of' this text /*change*/.";
    private String filename="x.java";
    public RepoCodeFragment(){
        layoutID = R.layout.fragment_repo_code;
    }

    public RepoCodeFragment newInstance(String filename,String code){
        RepoCodeFragment instance = new RepoCodeFragment();
        instance.code=code;
        instance.filename=filename;
        return instance;
    }

    @Override
    public void mainFunction() {
        code = Session.selectedFile.code;
        filename = Session.selectedFile.name;
        webView = (WebView) layout.findViewById(R.id.repo_code_web_view);
        webView.setWebChromeClient(new WebChromeClient() {});
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "Android");
        webView.loadUrl("file:///android_asset/repoCodeEditor.html");
    }

    public class JavaScriptInterface {
        Context mContext;
        JavaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public String getFromAndroid() {
            return code;
        }
        @JavascriptInterface
        public String getFilename() {
            return filename;
        }
    }

}
