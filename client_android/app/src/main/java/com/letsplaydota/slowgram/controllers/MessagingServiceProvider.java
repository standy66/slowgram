package com.letsplaydota.slowgram.controllers;

import com.letsplaydota.slowgram.controllers.exceptions.BadConfirmationCodeException;
import com.letsplaydota.slowgram.controllers.exceptions.BadPhoneNumberException;
import com.letsplaydota.slowgram.controllers.exceptions.ServerUnavailableException;

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
	void requestConfirmationCode(String phoneNumber, String deviceId)
			throws BadPhoneNumberException,
			ServerUnavailableException;


}
