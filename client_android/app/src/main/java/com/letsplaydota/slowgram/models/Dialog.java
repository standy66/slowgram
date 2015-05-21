package com.letsplaydota.slowgram.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Collection;

/**
 * Created by andrew on 17.04.15.
 */
public class Dialog {
    String dialogId;
    Contact creator;
    Contact participant;
    String dialogPicture;

    public void parseJson(JSONObject object) {
        try {
            dialogId = object.getString("dialogId");
            creator = (new Contact());
            creator.parseFromJson(object.getJSONObject("creator"));
            participant = new Contact();
            participant.parseFromJson(object.getJSONObject("participant"));
            dialogPicture = object.getString("dialogPicture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
