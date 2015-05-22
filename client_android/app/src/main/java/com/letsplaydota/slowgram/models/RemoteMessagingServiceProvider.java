package com.letsplaydota.slowgram.models;

import android.net.Uri;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by andrew on 29.04.15.
 */

public class RemoteMessagingServiceProvider implements MessagingServiceProvider {

    enum HTTPMethod {
        POST,
        GET,
        PUT,
        UPDATE
    }

    private String buildQueryString(NameValuePair[] pairs) {
        StringBuilder sb = new StringBuilder("?");
        for (int i = 0; i < pairs.length; i++) {
            sb.append(String.format("%s=%s", pairs[i].getName(), pairs[i].getValue()));
            if (i != pairs.length) {
                sb.append("&");
            }
        }
        return sb.toString();
    }

    private HttpResponse query(String address, HTTPMethod method, NameValuePair[] params) throws IOException{
        HttpClient client = new DefaultHttpClient();
        switch (method) {
            case GET:
                HttpGet httpGetRequest = new HttpGet(address + buildQueryString(params));
                httpGetRequest.setHeader("Accept", "application/json");
                return client.execute(httpGetRequest);
            case POST:
                HttpPost httpPostRequest = new HttpPost(address);
                httpPostRequest.setHeader("Accept", "application/json");
                httpPostRequest.setEntity(new UrlEncodedFormEntity(Arrays.asList(params)));
                return client.execute(httpPostRequest);
            case PUT:
            case UPDATE:
        }
        throw new UnsupportedOperationException("unsupported for now");
    }

    private String remoteAddress;

    public RemoteMessagingServiceProvider(String ip, int port) {
        remoteAddress = "http://" + ip + ":" + port;
    }

    @Override
    public String authorize(String phoneNumber, String code, String deviceId) throws BadPhoneNumberException, BadConfirmationCodeException, ServerUnavailableException {
        return null;
    }

    private final String REMEMBER_TOKEN_FIELD = "remember_token";


    private final String REQUEST_CODE_ADDRESS = "/login";
    private final String REQUEST_CODE_PHONE_FILED = "phone";
    private final String REQUEST_CODE_DEVICE_ID_FIELD = "device_id";
    private final String REQUEST_CODE_TOKEN_FIELD = "remember_token";
    private final String GET_CONTACT_LIST_ADDRESS = "/contact_relations";
    private final String GET_CONTACT_LIST_NAME_OF_RETURN_ARRAY = "contacts";
    private final String ADD_CONTACT_TO_CONTACT_LIST_ADDRESS = "/users";
    private final String ADD_CONTACT_TO_CONTACT_LIST_ADD_PHONE_FIELD = "add_contact_phone";
    private final String GET_DIALOG_ADDRESS = "/conversations";
    private final String GET_DIALOG_FROM_FIELD = "from";
    private final String GET_DIALOG_TO_FIELD = "to";
    private final String GET_DIALOG_NAME_OF_RETURN_ARRAY = "dialogs";
    private final String CREATE_DIALOG_ADDRESS = "/conversations";
    private final String CREATE_DIALOG_PHONE_NUMBERS_FIELD = "recipient_phone";
    private final String CREATE_DIALOG_DIALOG_ID = "dialogID";
    private final String SEND_MESSAGE_ADDRESS = "/messages";
    private final String SEND_MESSAGE_DIALOG_ID_FIELD = "conversation_id";
    private final String SEND_MESSAGE_CAPTION_FIELD = "title";
    private final String SEND_MESSAGE_TEXT_FIELD = "body";
    private final String GET_MESSAGE_ADDRESS = "/conversations";
    private final String GET_MESSAGE_DIALOG_ID_FIELD = "id";
    private final String GET_MESSAGE_FROM_FIELD = "from";
    private final String GET_MESSAGE_TO_FIELD = "to";
    private final String SEND_MESSAGE_RECIPIENT_PHONE = "recipient_phone";
    private final String GET_MESSAGE_RETURNED_MESSAGES = "messages";
    private final String UPDATE_USER_INFORMATIONS_ADDRESS = "/users";
    private final String UPDATE_USER_INFORMATIONS_PHONE_NUMBER_FIELD = "phoneNumber";
    private final String UPDATE_USER_INFORMATIONS_AVATAR_URL_FIELD = "avatarURL";
    private final String UPDATE_USER_INFORMATIONS_NAME_FIELD = "name";

    @Override
    public String requestConfirmationCode(String phoneNumber, String deviceId) throws BadPhoneNumberException, ServerUnavailableException {
        try {
            HttpResponse response = query(remoteAddress + REQUEST_CODE_ADDRESS, HTTPMethod.POST, new NameValuePair[] {
                    new BasicNameValuePair(REQUEST_CODE_PHONE_FILED, phoneNumber),
                    new BasicNameValuePair(REQUEST_CODE_DEVICE_ID_FIELD, deviceId)
            });

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
    public void updateUserInformation(String token, String phoneNumber, String avatarURL, String name) {
        try {
            HttpResponse response = query(remoteAddress + UPDATE_USER_INFORMATIONS_ADDRESS, HTTPMethod.POST, new NameValuePair[] {
                    new BasicNameValuePair(REMEMBER_TOKEN_FIELD, token),
                    new BasicNameValuePair(UPDATE_USER_INFORMATIONS_NAME_FIELD, name),
                    new BasicNameValuePair(UPDATE_USER_INFORMATIONS_AVATAR_URL_FIELD, avatarURL),
                    new BasicNameValuePair(UPDATE_USER_INFORMATIONS_PHONE_NUMBER_FIELD, phoneNumber)
            });
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contact> getContactList(String token) throws BadTokenException {
        try {
            HttpResponse response = query(remoteAddress + GET_CONTACT_LIST_ADDRESS, HTTPMethod.GET, new NameValuePair[] {
                    new BasicNameValuePair(REMEMBER_TOKEN_FIELD, token),
            });
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

            List<Contact> contacts = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject object = null;
                try {
                    object = jsonArray.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Contact contact = Contact.parseJSON(object);
                contacts.add(contact);
            }

            return contacts;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addContactToContactList(String token, String contactPhoneNumber) throws ContactNotRegisteredException, BadTokenException {
        try {
            HttpResponse response = query(remoteAddress + ADD_CONTACT_TO_CONTACT_LIST_ADDRESS, HTTPMethod.POST, new NameValuePair[] {
                    new BasicNameValuePair(REMEMBER_TOKEN_FIELD, token),
                    new BasicNameValuePair(ADD_CONTACT_TO_CONTACT_LIST_ADD_PHONE_FIELD, contactPhoneNumber)
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Dialog> getDialogs(String token, int from, int to) throws BadTokenException {
        try {
            HttpResponse response = query(remoteAddress + GET_DIALOG_ADDRESS, HTTPMethod.GET, new NameValuePair[]{
                    new BasicNameValuePair(REMEMBER_TOKEN_FIELD, token),
            });

            String body = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONArray(body);

            List<Dialog> dialogs = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject object = jsonArray.getJSONObject(i);
                Dialog dialog = Dialog.parseJSON(object);
                dialogs.add(dialog);
            }
            return dialogs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Dialog createDialog(String token, String phoneNumber) throws BadTokenException, BadPhoneNumberException {

        try {
            HttpResponse response = query(remoteAddress + CREATE_DIALOG_ADDRESS, HTTPMethod.POST, new NameValuePair[]{
                    new BasicNameValuePair(REMEMBER_TOKEN_FIELD, token),
                    new BasicNameValuePair(CREATE_DIALOG_PHONE_NUMBERS_FIELD, phoneNumber),
            });

            String body = EntityUtils.toString(response.getEntity());
            JSONObject object = new JSONObject(body);
            return Dialog.parseJSON(object);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void sendMessage(String token, int dialogId, String caption, String text) throws BadTokenException, BadDialogId {
        try {
            HttpResponse response = query(remoteAddress + SEND_MESSAGE_ADDRESS, HTTPMethod.POST, new NameValuePair[] {
                    new BasicNameValuePair(REMEMBER_TOKEN_FIELD, token),
                    new BasicNameValuePair(SEND_MESSAGE_DIALOG_ID_FIELD, String.valueOf(dialogId)),
                    new BasicNameValuePair(SEND_MESSAGE_CAPTION_FIELD, caption),
                    new BasicNameValuePair(SEND_MESSAGE_TEXT_FIELD, text)
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> getMessages(String token, int dialogId, int from, int to) throws BadTokenException, BadDialogId {
        try {
            HttpResponse response = query(remoteAddress + GET_MESSAGE_ADDRESS + "/" + String.valueOf(dialogId), HTTPMethod.GET, new NameValuePair[] {
                    new BasicNameValuePair(REMEMBER_TOKEN_FIELD, token)
            });
            String body = EntityUtils.toString(response.getEntity());
            JSONArray jsonArray = new JSONObject(body).getJSONArray("messages");
            List<Message> messages = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject object = jsonArray.getJSONObject(i);
                messages.add(Message.parseJSON(object));
            }
            return messages;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    /*@Override
    public void sendMessageByPhone(String token, String phone, String caption, String text) throws BadTokenException, BadDialogId {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost sessionPostSendMessage = new HttpPost(remoteAddress + SEND_MESSAGE_ADDRESS);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair(SEND_MESSAGE_TOKEN_FIELD, token));
        nameValuePairs.add(new BasicNameValuePair(SEND_MESSAGE_RECIPIENT_PHONE, phone));
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

    public List<Message> getMessagesByPhone(String token, String phone, int from, int to) throws BadTokenException, BadDialogId {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet sessionGetMessage = new HttpGet(remoteAddress + GET_MESSAGE_ADDRESS);
        HttpParams params = new BasicHttpParams();
        params.setParameter(GET_MESSAGE_TOKEN_FIELD, token);
        params.setParameter(GET_MESSAGE_DIALOG_ID_FIELD, phone);
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
    }*/
}
