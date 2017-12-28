package com.xwady.twitterclient.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.xwady.twitterclient.R;
import com.xwady.twitterclient.models.User;
import com.xwady.twitterclient.utils.SessionManager;

import java.net.UnknownHostException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    //Views
    @BindView(R.id.twitter_login_button)
    TwitterLoginButton twitterLoginButton;

    private com.xwady.twitterclient.utils.SessionManager sessionManager;

    TwitterSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Butter Knife
        ButterKnife.bind(this);
        //Session
        sessionManager=new SessionManager(this);
        //Twitter Configuration
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        //when Login call back
        //if success create the session  and navigate to Main activity
        //if fail show the error
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                User user = new User(session.getUserName(),authToken.token,authToken.secret,session.getUserId());
                user.setAccountStatus(User.AccountStatus.LOGGED_IN);
                sessionManager.createLoginSession(user);
                Intent i = new Intent(new Intent(LoginActivity.this,MainActivity.class));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

            @Override
            public void failure(TwitterException exception) {
                if (exception.getCause() instanceof UnknownHostException) {
                    Toasty.error(getApplicationContext(), getString(R.string.no_internet_connection), 500, false).show();
                } else {
                    Toasty.error(getApplicationContext(), exception.getMessage(), 500, true).show();
                }
            }
        });



    }

    @OnClick(R.id.sign_in_btn)
    public void onSignInClick(View v){
        twitterLoginButton.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode,resultCode,data);
    }

    //prevent back to splashscreen
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
