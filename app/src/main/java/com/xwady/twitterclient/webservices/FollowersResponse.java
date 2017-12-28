package com.xwady.twitterclient.webservices;

import com.xwady.twitterclient.models.Follower;

import java.util.List;


public class FollowersResponse {
    public List<Follower> users;
    public long next_cursor,previous_cursor;
}
