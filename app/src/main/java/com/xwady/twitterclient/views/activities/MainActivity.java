package com.xwady.twitterclient.views.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.xwady.twitterclient.utils.EndlessScrollListener;
import com.xwady.twitterclient.R;
import com.xwady.twitterclient.adapters.FollowersAdapter;
import com.xwady.twitterclient.models.Follower;
import com.xwady.twitterclient.webservices.FollowersResponse;
import com.xwady.twitterclient.webservices.MyTwitterClient;
import com.xwady.twitterclient.utils.SessionManager;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    //Views
    @BindView(R.id.follower_result)
    RecyclerView rvFollowers;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    //Twitter
    final TwitterSession activeSession = TwitterCore.getInstance()
            .getSessionManager().getActiveSession();
    MyTwitterClient mTwitterClient;

    private long nextCursor = -1;

    private FollowersAdapter mFollowersAdapter;
    private List<Follower> followers = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private Realm mRealm;

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Butter Knife initialization
        ButterKnife.bind(this);
        //Realm get Instance
        mRealm = Realm.getDefaultInstance();
        //Session
        mSessionManager = new SessionManager(this);
        //Twitter Client Initialization
        mTwitterClient = new MyTwitterClient(activeSession);
        //fill Recyclerview
        mLayoutManager = new LinearLayoutManager(this);
        rvFollowers.setLayoutManager(mLayoutManager);
        mFollowersAdapter = new FollowersAdapter(followers, this);
        rvFollowers.setAdapter(mFollowersAdapter);
        //swipe refresh
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.tw__blue_default));
        swipeRefreshLayout.setOnRefreshListener(onSwipe());
        //Call Api
        getFollowers(nextCursor);
        //Get the next page OnScroll
        rvFollowers.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore() {
                getFollowers(nextCursor);
            }
        });
    }

    private void getFollowers(long cursor) {
        int count = 25; // items per page
        mTwitterClient.getCustomService().showFollowers(count, cursor).enqueue(new Callback<FollowersResponse>() {
            @Override
            public void success(Result<FollowersResponse> result) {
                if (null == result.data) {
                    failure(new TwitterException("error"));
                    return;
                }

                if (cursor == -1) { //if it's first page
                    followers.clear();
                }
                nextCursor = result.data.next_cursor; //get next cursor for pagination
                followers.addAll(result.data.users);
                mFollowersAdapter.notifyDataSetChanged();

                //Store Results using Realm
                mRealm.beginTransaction();
                for (Follower follower : result.data.users) {
                    List<Follower> followersList = mRealm.where(Follower.class).equalTo("id", follower.id).findAll();
                    if (followersList.size() == 0) { //if this row wasn't saved before
                        mRealm.insert(follower); //insert to local database
                    }
                }
                mRealm.commitTransaction();
                swipeRefreshLayout.setRefreshing(false); // set Refreshing = false

            }

            @Override
            public void failure(TwitterException exception) {
                if (exception.getCause() instanceof UnknownHostException) {
                    Toasty.error(getApplicationContext(), getString(R.string.no_internet_connection), 500, false).show();
                    followers.addAll(mRealm.copyFromRealm(mRealm.where(Follower.class).findAll()));
                    mFollowersAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toasty.error(getApplicationContext(), exception.getMessage(), 500, true).show();
                }

            }
        });
    }

    public SwipeRefreshLayout.OnRefreshListener onSwipe() {
        return () -> getFollowers(-1);
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
            case R.id.logout:
                showLogoutDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
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
