package com.letsplaydota.slowgram;

import android.accounts.AccountManager;

import android.app.Activity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.games.internal.api.NotificationsImpl;
import com.google.android.gms.plus.Account;

import java.io.IOException;


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

    public final static String PREF_OAUTH_PROVIDER = "OUATH_PROVIDER";
    public final static String PREF_OAUTH_ID = "OAUTH_ID";

    protected enum OAuthProvider {
        Google,
        VK,
        Facebook
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        if (!preferences.getString(PREF_OAUTH_ID, "-1").equals("-1")) {
            OAuthProvider provider = OAuthProvider.valueOf(preferences.getString(PREF_OAUTH_PROVIDER, "-1"));
            String oauthId = preferences.getString(PREF_OAUTH_ID, "-1");
            oAuthGetToken(provider, oauthId);

        }

    }



    protected void pickUserAccountGoogle() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT_GOOGLE);
    }

    protected void oAuthGetToken(OAuthProvider provider, String ouathId) {
        switch (provider) {
            case Google:
                try {
                    String token = GoogleAuthUtil.getToken(this, ouathId, SCOPES);
                    Toast.makeText(this, token, Toast.LENGTH_LONG);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (GoogleAuthException e) {
                    e.printStackTrace();
                }


                break;
            default: throw new RuntimeException(provider + " support not implemented yet");
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
                oAuthGetToken(OAuthProvider.Google, email);
                // With the account name acquired, go get the auth token

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.pick_account, Toast.LENGTH_SHORT).show();
            }
        }

    }
}

