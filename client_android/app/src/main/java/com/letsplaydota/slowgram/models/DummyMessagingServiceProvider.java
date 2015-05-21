package com.letsplaydota.slowgram.models;

import com.letsplaydota.slowgram.models.exceptions.BadConfirmationCodeException;
import com.letsplaydota.slowgram.models.exceptions.BadDialogId;
import com.letsplaydota.slowgram.models.exceptions.BadPhoneNumberException;
import com.letsplaydota.slowgram.models.exceptions.BadTokenException;
import com.letsplaydota.slowgram.models.exceptions.ContactNotRegisteredException;
import com.letsplaydota.slowgram.models.exceptions.ServerUnavailableException;

import java.util.Collection;
import java.util.List;

/**
 * Created by andrew on 29.04.15.
 */
public abstract class DummyMessagingServiceProvider implements  MessagingServiceProvider {
	@Override
	public String authorize(String phoneNumber, String code, String deviceId) throws BadPhoneNumberException, BadConfirmationCodeException, ServerUnavailableException {
		return "1";
	}

	@Override
	public String requestConfirmationCode(String phoneNumber, String deviceId) throws BadPhoneNumberException, ServerUnavailableException {
		return null;
	}

	@Override
	public Collection<Contact> getContactList(String token) throws BadTokenException {
		return null;
	}

	@Override
	public void addContactToContactList(String token, String contactPhoneNumber) throws ContactNotRegisteredException, BadTokenException {

	}

	@Override
	public List<Dialog> getDialogs(String token, int from, int to) throws BadTokenException {
		return null;
	}

	@Override
	public String createDialog(String token, String phoneNumbers) throws BadTokenException, BadPhoneNumberException {
		return null;
	}

	@Override
	public void sendMessage(String token, String dialogId, String caption, String text) throws BadTokenException, BadDialogId {

	}

	@Override
	public List<Message> getMessages(String token, String dialogId, int from, int to) throws BadTokenException, BadDialogId {
		return null;
	}

	@Override
	public void updateUserInformation(String token, String phoneNumber, String avatarURL, String name) {

	}
}