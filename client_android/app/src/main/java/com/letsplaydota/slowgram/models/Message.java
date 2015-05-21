package com.letsplaydota.slowgram.models;

import android.text.format.Time;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrew on 17.04.15.
 */
public class Message {
    String text;
    String caption;
    String timestamp;

    public void parseFromJson(JSONObject object) {
        try {
            text = object.getString("text");
            caption = object.getString("caption");
            timestamp = object.getString("timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}