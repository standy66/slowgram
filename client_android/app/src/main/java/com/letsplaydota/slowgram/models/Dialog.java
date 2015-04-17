package com.letsplaydota.slowgram.models;

import java.net.URI;
import java.util.Collection;

/**
 * Created by andrew on 17.04.15.
 */
public class Dialog {
	String dialogId;
	Contact creator;
	Collection<Contact> participants;
	URI dialogPicture;

}
