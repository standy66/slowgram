package com.letsplaydota.slowgram.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by andrew on 17.04.15.
 */
public class Message {
    private int id;
    private String body;
    private String title;
    private String deliviredAt;
    private boolean own;

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public String getDeliviredAt() {
        return deliviredAt;
    }

    public boolean isOwn() {
        return own;
    }

    public String getTitle() {
        return title;
    }

    public static Message parseJSON(JSONObject object) {
        Message res = new Message();
        try {
            if (object.has("id"))
                res.id = object.getInt("id");
            if (object.has("body"))
                res.body = object.getString("body");
            if (object.has("title"))
                res.title = object.getString("title");
            if (object.has("delivered_at"))
                res.deliviredAt = object.getString("delivered_at");
            if (object.has("own"))
                res.own = object.getBoolean("own");
            return res;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return title + " " + body;
    }
}