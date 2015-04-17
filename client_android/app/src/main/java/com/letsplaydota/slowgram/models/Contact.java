package com.letsplaydota.slowgram.models;

import java.net.URI;

/**
 * Created by andrew on 17.04.15.
 */
public class Contact {
	String firstName;
	String lastName;
	URI[] profilePictures;
	String phoneNumber;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public URI[] getProfilePictures() {
		return profilePictures;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
}
