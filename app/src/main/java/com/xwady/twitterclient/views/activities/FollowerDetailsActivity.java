package com.xwady.twitterclient.views.activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.xwady.twitterclient.R;
import com.xwady.twitterclient.adapters.TweetsAdapter;
import com.xwady.twitterclient.models.Follower;
import com.xwady.twitterclient.models.Tweet;
import com.xwady.twitterclient.webservices.MyTwitterClient;
import com.xwady.twitterclient.utils.SessionManager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class FollowerDetailsActivity extends AppCompatActivity {
    //Views
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.screen_name)
    TextView tvScreenName;
    @BindView(R.id.tweets)
    RecyclerView rvTweets;

    private Follower mFollower;
    private LinearLayoutManager linearLayoutManager;
    private TweetsAdapter mTweetsAdapter;
    private List<Tweet> tweets = new ArrayList<>();
    //Session
    private SessionManager mSessionManager;
    //Twitter
    final TwitterSession activeSession = TwitterCore.getInstance()
            .getSessionManager().getActiveSession();
    MyTwitterClient mTwitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followe_details);
        ButterKnife.bind(this);
        //toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //session
        mSessionManager = new SessionManager(this);
        //twitter client initialization
        mTwitterClient = new MyTwitterClient(activeSession);
        //get Followr Object
        if (getIntent().hasExtra("follower")) {
            mFollower = (Follower) getIntent().getSerializableExtra("follower");
        }
        //set follower name as toolbar title
        if (getSupportActionBar() != null){
        getSupportActionBar().setTitle(mFollower.name);}

        //set Follower Data
        if (mFollower.profile_banner_url != null) { //check if banner is null
            Glide.with(this).load(mFollower.profile_banner_url).fitCenter().into(background);
        }
        Glide.with(this).load(mFollower.profile_image_url).fitCenter().into(profileImage);
        tvName.setText(mFollower.name);
        tvScreenName.setText("@" + mFollower.screen_name);
        //fill recyclerview
        linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        mTweetsAdapter = new TweetsAdapter(tweets, this);
        rvTweets.setAdapter(mTweetsAdapter);
        //call Api
        getTweets();
    }

    private void getTweets() {
        mTwitterClient.getCustomService().showTweets(10, mFollower.id).enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                if (null == result.data) { //if data is null
                    failure(new TwitterException("error"));
                    return;
                }
                tweets.clear();
                tweets.addAll(result.data);
                mTweetsAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(TwitterException exception) {
                if (exception.getCause() instanceof UnknownHostException ) {
                    Toasty.error(getApplicationContext(), getString(R.string.no_internet_connection), 500, false).show();
                } else {
                    Toasty.error(getApplicationContext(), exception.getMessage(), 500, true).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //if press back arrow
                finish();
                break;
            case R.id.logout: //if press logout
                showLogoutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLogoutDialog(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton("Yes", (dialog, which) -> mSessionManager.logoutUser(LoginActivity.class))
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
