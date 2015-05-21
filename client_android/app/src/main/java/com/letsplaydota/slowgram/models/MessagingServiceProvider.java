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
 * Created by andrew on 17.04.15.
 */
public interface MessagingServiceProvider {
	/**
	 * Authorize on server
	 *
	 * @param phoneNumber user's phone number
	 * @param code        confirmation code sent via SMS
	 * @param deviceId    device unique id
	 * @return token used for further server interactions
	 */
	String authorize(String phoneNumber, String code, String deviceId)
			throws BadPhoneNumberException,
			BadConfirmationCodeException,
			ServerUnavailableException;

	/**
	 * Requests server to send confirmation code via SMS
	 *
	 * @param phoneNumber user's phone number the code would be sent to
	 * @param deviceId    device unique id
	 */

    String requestConfirmationCode(String phoneNumber, String deviceId) throws BadPhoneNumberException, ServerUnavailableException;

    /**
	 * gets a list of contacts
	 * @param token session token obtained by subsequent call to authorize()
	 * @return Collection of contacts
	 */
	Collection<Contact> getContactList(String token)
			throws BadTokenException;

	/**
	 * add contact to your contact list by phone number
	 * @param token session token obtained by session token obtained by subsequent call to authorize()
	 * @param contactPhoneNumber contact's phone number
	 */
	void addContactToContactList(String token, String contactPhoneNumber)
			throws ContactNotRegisteredException,
			BadTokenException;


	/**
	 * gets list of last dialogs
	 * @param token session token obtained by session token obtained by subsequent call to authorize()
	 * @param from number of staring dialog ordered by last message time
	 * @param to number of ending dialog ordered by last
	 * @return list of dialogs
	 */
	List<Dialog> getDialogs(String token, int from, int to)
		throws BadTokenException;

	/**
	 * creates a dialog with given participants
	 * @param token session token obtained by session token obtained by subsequent call to authorize()
	 * @param phoneNumber phone nunmber  participant
	 * @return unique dialog id
	 */
	String createDialog(String token, String phoneNumber)
		throws BadTokenException,
			BadPhoneNumberException;

	/**
	 * sends message to the given dialog
	 * @param token session token obtained by session token obtained by subsequent call to authorize()
	 * @param dialogId unique dialog id
	 * @param caption message caption
	 * @param text message body
	 */
	void sendMessage(String token, String dialogId, String caption, String text)
		throws BadTokenException,
			BadDialogId;

	/**
	 * retrieves messages from the given dialog
	 * @param token session token obtained by session token obtained by subsequent call to authorize()
	 * @param dialogId unique dialog id
	 * @param from number of staring message ordered by sent time
	 * @param to number of ending message ordered by sent time
	 * @return list of messages
	 */
	List<Message> getMessages(String token, String dialogId, int from, int to)
		throws BadTokenException,
			BadDialogId;


}
