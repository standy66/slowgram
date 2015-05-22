package com.letsplaydota.slowgram.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.net.URI;

/**
 * Created by andrew on 17.04.15.
 */
public class Contact {
    int id;
    String name;
    String phone;
    String avatar;

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public static Contact parseJSON(JSONObject object) {
        Contact res = new Contact();
        try {
            if (object.has("name"))
                res.name = object.getString("name");
            if (object.has("id"))
                res.id = object.getInt("id");
            if (object.has("phone"))
                res.phone = object.getString("phone");
            if (object.has("avatar"))
                res.avatar = object.getString("avatar");
            return res;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
