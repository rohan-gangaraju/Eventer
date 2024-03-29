package com.eventer.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.eventer.MainActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by rg on 30-May-15.
 */
public class GetTokenTask extends AsyncTask<Void,Void,String> {
    Activity mActivity;
    String mScope;
    String mEmail;

    public GetTokenTask(Activity activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
    }

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected String doInBackground(Void... params) {
        try {
            String token = fetchToken();
            if (token != null) {
                return token;
                // Use the token to access the user's Google data.
            }
        } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
            Log.d("GetUsernameTask::dIB", "IOException");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String output) {
        if (this.mActivity instanceof MainActivity) {
            ((MainActivity)mActivity).setToken(output);
        }
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String fetchToken() throws IOException {
        try {
            return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (UserRecoverableAuthException userRecoverableException) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            Log.d("GetUsernameTask::dIB", "UserRecoverableAuthException");
            ((MainActivity)mActivity).handleException(userRecoverableException);
        } catch (GoogleAuthException fatalException) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.
            Log.d("GetUsernameTask::dIB", "GoogleAuthException");
        }
        return null;
    }
}
