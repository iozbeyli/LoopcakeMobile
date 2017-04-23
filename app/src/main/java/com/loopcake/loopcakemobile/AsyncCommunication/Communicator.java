package com.loopcake.loopcakemobile.AsyncCommunication;

import org.json.JSONObject;

/**
 * Created by Melih on 23.04.2017.
 */

public interface Communicator {
    void successfulExecute(JSONObject jsonObject);
    void failedExecute();
}
