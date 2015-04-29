package com.letsplaydota.slowgram.models;

import com.letsplaydota.slowgram.R;
import com.letsplaydota.slowgram.models.exceptions.BadConfirmationCodeException;
import com.letsplaydota.slowgram.models.exceptions.BadDialogId;
import com.letsplaydota.slowgram.models.exceptions.BadPhoneNumberException;
import com.letsplaydota.slowgram.models.exceptions.BadTokenException;
import com.letsplaydota.slowgram.models.exceptions.ContactNotRegisteredException;
import com.letsplaydota.slowgram.models.exceptions.ServerUnavailableException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by andrew on 29.04.15.
 */

enum HttpRequestType {
	GET,
	POST,
	DELETE,
	UPDATE
};

public class RemoteMessagingServiceProvider implements MessagingServiceProvider {

	private String remoteAddress;

	public RemoteMessagingServiceProvider(String ip, int port) {
		remoteAddress = "http://" + ip + ":" + port;
	}

	@Override
	public String authorize(String phoneNumber, String code, String deviceId) throws BadPhoneNumberException, BadConfirmationCodeException, ServerUnavailableException {
		return null;
	}

	private final String REQUEST_CODE_ADDRESS = "/request_code";
	private final String REQUEST_CODE_PHONE_FILED = "phone";
	private final String REQUEST_CODE_DEVICE_ID_FIELD = "device_id";
	private final String REQUEST_CODE_TOKEN_FIELD = "remember_token";
	private final HttpRequestType REQUEST_CODE_HTTP_REQUEST_TYPE = HttpRequestType.POST;

	@Override
	public void requestConfirmationCode(String phoneNumber, String deviceId) throws BadPhoneNumberException, ServerUnavailableException {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost sessionPostRequest = new HttpPost(remoteAddress + REQUEST_CODE_ADDRESS);
			List<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair(REQUEST_CODE_PHONE_FILED, phoneNumber));
			nameValuePairs.add(new BasicNameValuePair(REQUEST_CODE_DEVICE_ID_FIELD, deviceId));
			sessionPostRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpClient.execute(sessionPostRequest);

			String body = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(body);
			String token = jsonObject.getString(REQUEST_CODE_TOKEN_FIELD);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public String createDialog(String token, Collection<String> phoneNumbers) throws BadTokenException, BadPhoneNumberException {
		return null;
	}

	@Override
	public void sendMessage(String token, String dialogId, String caption, String text) throws BadTokenException, BadDialogId {

	}

	@Override
	public List<Message> getMessages(String token, String dialogId, int from, int to) throws BadTokenException, BadDialogId {
		return null;
	}
}
