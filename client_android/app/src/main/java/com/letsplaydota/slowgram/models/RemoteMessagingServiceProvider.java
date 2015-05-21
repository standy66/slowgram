package com.letsplaydota.slowgram.models;

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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
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

    private final String REQUEST_CODE_ADDRESS = "/login";
    private final String REQUEST_CODE_PHONE_FILED = "phone";
    private final String REQUEST_CODE_DEVICE_ID_FIELD = "device_id";
    private final String REQUEST_CODE_TOKEN_FIELD = "remember_token";
    private final String GET_CONTACT_LIST_ADDRESS = "/contact_relations";
    private final String GET_CONTACT_LIST_TOKEN_FIELD = "token";
    private final String GET_CONTACT_LIST_NAME_OF_RETURN_ARRAY = "contacts";
    private final String ADD_CONTACT_TO_CONTACT_LIST_ADDRESS = "/contacts";
    private final String ADD_CONTACT_TO_CONTACT_LIST_TOKEN_FIELD = "token";
    private final String ADD_CONTACT_TO_CONTACT_LIST_ADD_PHONE_FIELD = "add_contact_phone";
    private final String GET_DIALOG_ADDRESS = "/conversations";
    private final String GET_DIALOG_TOKEN_FIELD = "token";
    private final String GET_DIALOG_FROM_FIELD = "from";
    private final String GET_DIALOG_TO_FIELD = "to";
    private final String GET_DIALOG_NAME_OF_RETURN_ARRAY = "dialogs";
    private final String CREATE_DIALOG_ADDRESS = "/conversations";
    private final String CREATE_DIALOG_TOKEN_FIELD = "token";
    private final String CREATE_DIALOG_PHONE_NUMBERS_FIELD = "phone_numbers";
    private final String CREATE_DIALOG_DIALOG_ID = "dialogID";
    private final String SEND_MESSAGE_ADDRESS = "/send_message";
    private final String SEND_MESSAGE_TOKEN_FIELD = "token";
    private final String SEND_MESSAGE_DIALOG_ID_FIELD = "dialogID";
    private final String SEND_MESSAGE_CAPTION_FIELD = "caption";
    private final String SEND_MESSAGE_TEXT_FIELD = "text";
    private final String GET_MESSAGE_ADDRESS = "/conversations";
    private final String GET_MESSAGE_TOKEN_FIELD = "token";
    private final String GET_MESSAGE_DIALOG_ID_FIELD = "dialogID";
    private final String GET_MESSAGE_FROM_FIELD = "from";
    private final String GET_MESSAGE_TO_FIELD = "to";
    private final String GET_MESSAGE_RETURNED_MESSAGES = "messages";


    private final HttpRequestType REQUEST_CODE_HTTP_REQUEST_TYPE = HttpRequestType.POST;

    @Override
    public String requestConfirmationCode(String phoneNumber, String deviceId) throws BadPhoneNumberException, ServerUnavailableException {
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
            return jsonObject.getString(REQUEST_CODE_TOKEN_FIELD);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "problem in request";
    }

    @Override
    public Collection<Contact> getContactList(String token) throws BadTokenException {

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet sessionGetContactList = new HttpGet(remoteAddress + GET_CONTACT_LIST_ADDRESS);
        HttpParams params = new BasicHttpParams();
        params.setParameter(REQUEST_CODE_TOKEN_FIELD, token);
        sessionGetContactList.setParams(params);
        HttpResponse response = null;
        try {
            response = httpClient.execute(sessionGetContactList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String body = null;
        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collection<Contact> contacts = new ArrayList<Contact>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject object = null;
            try {
                object = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Contact contact = new Contact();
            contact.parseFromJson(object);
            contacts.add(contact);
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
        HttpGet sessionGetDialogs = new HttpGet(remoteAddress + GET_DIALOG_ADDRESS);
        HttpParams params = new BasicHttpParams();
        params.setParameter(REQUEST_CODE_TOKEN_FIELD, token);
        params.setParameter(GET_DIALOG_TOKEN_FIELD, token);
        params.setParameter(GET_DIALOG_TO_FIELD, String.valueOf(to));
        params.setParameter(GET_DIALOG_FROM_FIELD, String.valueOf(from));
        sessionGetDialogs.setParams(params);

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
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Dialog> dialogs = new ArrayList<Dialog>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject object = null;
            try {
                object = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Dialog dialog = new Dialog();
            dialog.parseJson(object);
            dialogs.add(dialog);
        }
        return dialogs;
    }

    @Override
    public String createDialog(String token, String phoneNumber) throws BadTokenException, BadPhoneNumberException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost sessionPostCreateDialog = new HttpPost(remoteAddress + CREATE_DIALOG_ADDRESS);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(CREATE_DIALOG_TOKEN_FIELD, token));
        nameValuePairs.add(new BasicNameValuePair(CREATE_DIALOG_PHONE_NUMBERS_FIELD, phoneNumber));
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
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet sessionGetMessage = new HttpGet(remoteAddress + GET_MESSAGE_ADDRESS);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        HttpParams params = new BasicHttpParams();
        params.setParameter(GET_MESSAGE_TOKEN_FIELD, token);
        params.setParameter(GET_MESSAGE_DIALOG_ID_FIELD, dialogId);
        params.setParameter(GET_MESSAGE_FROM_FIELD, String.valueOf(from));
        params.setParameter(GET_MESSAGE_TO_FIELD, String.valueOf(to));
        sessionGetMessage.setParams(params);
        HttpResponse response = null;
        try {
            response = httpClient.execute(sessionGetMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String body = null;
        try {
            body = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(body);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Message> messages = null;
        for (int i = 0; i < jsonArray.length(); ++i) {
            JSONObject object = null;
            try {
                object = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.parseFromJson(object);
        }
        return messages;
    }
}
