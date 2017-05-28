package com.loopcake.loopcakemobile.TwoFactorAuthentication;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopcake.loopcakemobile.AsyncCommunication.AsyncCommunicationTask;
import com.loopcake.loopcakemobile.AsyncCommunication.Communicator;
import com.loopcake.loopcakemobile.Constants;
import com.loopcake.loopcakemobile.LCFragment.LCFragment;
import com.loopcake.loopcakemobile.PostDatas.TwoFactorPostDatas;
import com.loopcake.loopcakemobile.R;

import org.json.JSONException;
import org.json.JSONObject;

public class TwoFactorAuthenticationFragment extends LCFragment implements Communicator{

    int seconds = 0;
    public TwoFactorAuthenticationFragment() {
        layoutID = R.layout.fragment_two_factor_authentication;
    }

    @Override
    public void mainFunction() {
        Button generateButton = (Button)layout.findViewById(R.id.generatePINButton);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendCodeRequest();
            }
        });
        sendCodeRequest();
        runTimer();
    }

    @Override
    public void successfulExecute(JSONObject jsonObject) {

        TextView pinText = (TextView)layout.findViewById(R.id.pinText);

        try {
            pinText.setText(jsonObject.getString("pin"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failedExecute() {
        TextView pinText = (TextView)layout.findViewById(R.id.pinText);
        pinText.setText("-----");
    }

    private void sendCodeRequest(){
        seconds=0;
        AsyncCommunicationTask generatePINTask = new AsyncCommunicationTask(Constants.apiURL+"generatePIN", TwoFactorPostDatas.getGeneratePINPostData(),TwoFactorAuthenticationFragment.this);
        generatePINTask.execute((Void) null);
    }

    private void runTimer() {
        final ProgressBar progressBar = (ProgressBar) layout.findViewById(R.id.progressBar2);
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(seconds);
                seconds++;
                if(seconds>=25){
                    sendCodeRequest();
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
}
