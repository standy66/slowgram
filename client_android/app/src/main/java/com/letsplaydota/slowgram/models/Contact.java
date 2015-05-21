package com.letsplaydota.slowgram.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.net.URI;

/**
 * Created by andrew on 17.04.15.
 */
public class Contact {
    String firstName;
    String lastName;
    String profilePictures;
    String phoneNumber;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePictures() {
        return profilePictures;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void parseFromJson(JSONObject object) {
        try {
            firstName = object.getString("firstName");
            lastName = object.getString("lastName");
            phoneNumber = object.getString("phoneNumber");
            profilePictures = object.getString("ProfilePictures");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
