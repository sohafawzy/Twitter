package com.xwady.twitterclient.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwady.twitterclient.R;
import com.xwady.twitterclient.models.Tweet;
import com.xwady.twitterclient.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.BH> {
    private List<Tweet> results;
    private Context mContext;

    public TweetsAdapter(List<Tweet> results, Context context) {
        this.results = results;
        mContext = context;
    }

    @Override
    public TweetsAdapter.BH onCreateViewHolder(ViewGroup parent, int viewType) {
        View dayView = LayoutInflater.from(mContext).inflate(R.layout.tweet_raw, parent, false);
        return new TweetsAdapter.BH(dayView);
    }

    @Override
    public void onBindViewHolder(TweetsAdapter.BH holder, final int position) {
        Glide.with(mContext).load(results.get(position).user.profile_image_url).fitCenter().into(holder.profileImage);
        holder.tvName.setText(results.get(position).user.name);
        holder.tvScreenName.setText("@"+results.get(position).user.name);
        holder.tvTime.setText(Util.getRelativeTimeAgo(results.get(position).created_at));
        holder.tvTweet.setText(results.get(position).text);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    static class BH extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.name) TextView tvName;
        @BindView(R.id.screen_name) TextView tvScreenName;
        @BindView(R.id.time) TextView tvTime;
        @BindView(R.id.tweet) TextView tvTweet;

        BH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}