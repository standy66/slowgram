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
import org.json.JSONArray;
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
    private final String GET_CONTACT_LIST_ADDRESS = "/get_contact_list";
    private final String GET_CONTACT_LIST_TOKEN_FIELD = "token";
    private final String GET_CONTACT_LIST_NAME_OF_RETURN_ARRAY = "contacts";
    private final String ADD_CONTACT_TO_CONTACT_LIST_ADDRESS = "/add_contact";
    private final String ADD_CONTACT_TO_CONTACT_LIST_TOKEN_FIELD = "token";
    private final String ADD_CONTACT_TO_CONTACT_LIST_ADD_PHONE_FIELD = "add_contact_phone";
    private final String GET_DIALOG_ADDRESS = "/get_dialog";
    private final String GET_DIALOG_TOKEN_FIELD = "token";
    private final String GET_DIALOG_FROM_FIELD = "from";
    private final String GET_DIALOG_TO_FIELD = "to";
    private final String GET_DIALOG_NAME_OF_RETURN_ARRAY = "dialogs";
    private final String CREATE_DIALOG_ADDRESS = "/create_dialog";
    private final String CREATE_DIALOG_TOKEN_FIELD = "token";
    private final String CREATE_DIALOG_PHONE_NUMBERS_FIELD = "phone_numbers";
    private final String CREATE_DIALOG_DIALOG_ID = "dialogID";
    private final String SEND_MESSAGE_ADDRESS = "/send_message";
    private final String SEND_MESSAGE_TOKEN_FIELD = "token";
    private final String SEND_MESSAGE_DIALOG_ID_FIELD = "dialogID";
    private final String SEND_MESSAGE_CAPTION_FIELD = "caption";
    private final String SEND_MESSAGE_TEXT_FIELD = "text";



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

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost sessionPostGetContactList = new HttpPost(remoteAddress + GET_CONTACT_LIST_ADDRESS);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(GET_CONTACT_LIST_TOKEN_FIELD, token));
        try {
            sessionPostGetContactList.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = httpClient.execute(sessionPostGetContactList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String body = null;
        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collection<Contact> contacts = new ArrayList<Contact>();
        try {
            contacts = (Collection<Contact>) jsonObject.get(GET_CONTACT_LIST_NAME_OF_RETURN_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contacts;
	}

	@Override
	public void addContactToContactList(String token, String contactPhoneNumber) throws ContactNotRegisteredException, BadTokenException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost sessionPostAddContact = new HttpPost(remoteAddress + ADD_CONTACT_TO_CONTACT_LIST_ADDRESS);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(ADD_CONTACT_TO_CONTACT_LIST_TOKEN_FIELD, token));
        nameValuePairs.add(new BasicNameValuePair(ADD_CONTACT_TO_CONTACT_LIST_ADD_PHONE_FIELD, contactPhoneNumber));
        try {
            sessionPostAddContact.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(sessionPostAddContact);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
         * Кажется, это всё, нам не надо ничего от сервера получать
         */
	}

	@Override
	public List<Dialog> getDialogs(String token, int from, int to) throws BadTokenException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost sessionGetDialogs = new HttpPost(remoteAddress + GET_DIALOG_ADDRESS);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(GET_DIALOG_TOKEN_FIELD, token));
        nameValuePairs.add(new BasicNameValuePair(GET_DIALOG_TO_FIELD, String.valueOf(from)));
        nameValuePairs.add(new BasicNameValuePair(GET_DIALOG_FROM_FIELD, String.valueOf(to)));
        try {
            sessionGetDialogs.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = httpClient.execute(sessionGetDialogs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String body = null;
        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Dialog> dialogs = new ArrayList<Dialog>();
        try {
            dialogs = (List<Dialog>) jsonObject.get(GET_DIALOG_NAME_OF_RETURN_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dialogs;
	}

	@Override
	public String createDialog(String token, Collection<String> phoneNumbers) throws BadTokenException, BadPhoneNumberException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost sessionPostCreateDialog = new HttpPost(remoteAddress + CREATE_DIALOG_ADDRESS);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(CREATE_DIALOG_TOKEN_FIELD, token));
        nameValuePairs.add(new BasicNameValuePair(CREATE_DIALOG_PHONE_NUMBERS_FIELD, new Gson().toJson(phoneNumbers)));
        try {
            sessionPostCreateDialog.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpResponse response = null;
        try {
            response = httpClient.execute(sessionPostCreateDialog);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String body = null;
        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String dialogID = null;
        try {
            dialogID = jsonObject.getString(CREATE_DIALOG_DIALOG_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dialogID;
	}

	@Override
	public void sendMessage(String token, String dialogId, String caption, String text) throws BadTokenException, BadDialogId {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost sessionPostSendMessage = new HttpPost(remoteAddress + SEND_MESSAGE_ADDRESS);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(SEND_MESSAGE_TOKEN_FIELD, token));
        nameValuePairs.add(new BasicNameValuePair(SEND_MESSAGE_DIALOG_ID_FIELD, dialogId));
        nameValuePairs.add(new BasicNameValuePair(SEND_MESSAGE_CAPTION_FIELD, caption));
        nameValuePairs.add(new BasicNameValuePair(SEND_MESSAGE_TEXT_FIELD, text));
        try {
            sessionPostSendMessage.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(sessionPostSendMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public List<Message> getMessages(String token, String dialogId, int from, int to) throws BadTokenException, BadDialogId {
		return null;
	}
}
