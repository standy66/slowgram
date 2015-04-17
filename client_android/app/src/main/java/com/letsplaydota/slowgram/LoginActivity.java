package com.letsplaydota.slowgram;

import android.accounts.AccountManager;

import android.app.Activity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.games.internal.api.NotificationsImpl;
import com.google.android.gms.plus.Account;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************
 * In order for Google+ sign in to work with your app, you must first go to:
 * https://developers.google.com/+/mobile/android/getting-started#step_1_enable_the_google_api
 * and follow the steps in "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends Activity {
    //request code used by google account picker
    protected final static int REQUEST_CODE_PICK_ACCOUNT_GOOGLE = 42;
    protected final static int REQUEST_CODE_PICK_ACCOUNT_FACEBOOK = 43;
    protected final static int REQUEST_CODE_PICK_ACCOUNT_VK = 43;

    private final static String G_PLUS_SCOPE =
            "oauth2:https://www.googleapis.com/auth/plus.me";
    private final static String USERINFO_SCOPE =
            "https://www.googleapis.com/auth/userinfo.profile";
    private final static String EMAIL_SCOPE =
            "https://www.googleapis.com/auth/userinfo.email";
    private final static String SCOPES = G_PLUS_SCOPE + " " + USERINFO_SCOPE + " " + EMAIL_SCOPE;

    private final static String SERVER = "188.166.109.92:3000";

    public final static String PREF_OAUTH_PROVIDER = "OUATH_PROVIDER";
    public final static String PREF_OAUTH_ID = "OAUTH_ID";

    protected enum OAuthProvider {
        Google,
        VK,
        Facebook
    }

    public TextView progressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        progressTextView = (TextView) findViewById(R.id.progress_text_view);
        if (!preferences.getString(PREF_OAUTH_ID, "-1").equals("-1")) {
            OAuthProvider provider = OAuthProvider.valueOf(preferences.getString(PREF_OAUTH_PROVIDER, "-1"));
            String oauthId = preferences.getString(PREF_OAUTH_ID, "-1");
            new GetTokenTask(this, oauthId, SCOPES, OAuthProvider.Google).execute();
            progressTextView.setText("in progress");
        }
    }



    public void pickUserAccountGoogle(View v) {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT_GOOGLE);
    }



    public class GetTokenTask extends AsyncTask<Void, Void, Void> {
        Activity mActivity;
        String mScope;
        String mOAuthId;
        OAuthProvider mProvider;

        GetTokenTask(Activity activity, String oAuthId, String scope, OAuthProvider provider) {
            this.mActivity = activity;
            this.mScope = scope;
            this.mOAuthId = oAuthId;
            this.mProvider = provider;
        }

        /**
         * Executes the asynchronous job. This runs when you call execute()
         * on the AsyncTask instance.
         */
        @Override
        protected Void doInBackground(Void... params) {
            String token = oAuthFetchToken();
            if (token != null) {
                HttpClient httpClient = new DefaultHttpClient();
                 HttpPost httpPost = new HttpPost("http://" + SERVER + "/session/create");
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
                nameValuePair.add(new BasicNameValuePair("oauth_provider", mProvider.toString()));
                nameValuePair.add(new BasicNameValuePair("oauth_token", mOAuthId.toString()));
                nameValuePair.add(new BasicNameValuePair("phone", "123123123123"));

                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }

                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    // write response to log
                    Log.d("Http Post Response:", response.toString());
                } catch (ClientProtocolException e) {
                    // Log exception
                    e.printStackTrace();
                } catch (IOException e) {
                    // Log exception
                    e.printStackTrace();
                }

            }
            return null;
        }


        protected String oAuthFetchToken() {
            switch (mProvider) {
                case Google:
                    try {
                        String token = GoogleAuthUtil.getToken(mActivity, mOAuthId, mScope);
                        return token;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UserRecoverableAuthException e) {
                        mActivity.startActivityForResult(
                                e.getIntent(),
                                REQUEST_CODE_PICK_ACCOUNT_GOOGLE);
                    } catch (GoogleAuthException e) {
                        e.printStackTrace();
                    }


                    break;
                default: throw new RuntimeException(mProvider + " support not implemented yet");
            }
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT_GOOGLE) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                SharedPreferences preferences = getPreferences(MODE_PRIVATE);
                preferences.edit().putString(PREF_OAUTH_PROVIDER, OAuthProvider.Google.toString())
                        .putString(PREF_OAUTH_ID, email).commit();


                new GetTokenTask(this, email, SCOPES, OAuthProvider.Google).execute();
                progressTextView.setText("in progress");
                // With the account name acquired, go get the auth token

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.pick_account, Toast.LENGTH_SHORT).show();
            }
        }

    }
}

