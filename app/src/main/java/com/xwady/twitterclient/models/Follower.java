package com.xwady.twitterclient.models;

import java.io.Serializable;

import io.realm.RealmObject;

public class Follower extends RealmObject implements Serializable  {
    public long id;
    public String id_str;
    public String name,screen_name,profile_image_url,description, profile_banner_url;


}
