package com.xwady.twitterclient.webservices;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.xwady.twitterclient.models.Tweet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MyTwitterClient extends TwitterApiClient {
    public MyTwitterClient(TwitterSession session) {
        super(session);
    }

    public CustomService getCustomService() {
        return getService(CustomService.class);
    }


    public interface CustomService {
        @GET("/1.1/followers/list.json")
        Call<FollowersResponse> showFollowers(@Query("count") long count,@Query("cursor") long cursor);

        @GET("1.1/statuses/user_timeline.json")
        Call<List<Tweet>> showTweets(@Query("count") long count, @Query("user_id") long userId);
    }
}