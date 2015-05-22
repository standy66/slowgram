package com.letsplaydota.slowgram.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Collection;

/**
 * Created by andrew on 17.04.15.
 */
public class Dialog {
    int id;
    Contact sender;
    Contact recipient;
    Message lastMessage;

    public int getId() {
        return id;
    }

    public Contact getSender() {
        return sender;
    }

    public Contact getRecipient() {
        return recipient;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public static Dialog parseJSON(JSONObject object) {
        Dialog res = new Dialog();
        try {
            if (object.has("id"))
                res.id = object.getInt("id");
            if (object.has("sender"))
                res.sender = Contact.parseJSON(object.getJSONObject("sender"));
            if (object.has("recipient"))
                res.recipient = Contact.parseJSON(object.getJSONObject("recipient"));
            if (object.has("last_message"))
                res.lastMessage = Message.parseJSON(object.getJSONObject("last_message"));
            return res;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
