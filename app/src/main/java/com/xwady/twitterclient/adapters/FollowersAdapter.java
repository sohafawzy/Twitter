package com.xwady.twitterclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xwady.twitterclient.R;
import com.xwady.twitterclient.models.Follower;
import com.xwady.twitterclient.views.activities.FollowerDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.BH> {
    private List<Follower> results;
    private Context mContext;

    public FollowersAdapter(List<Follower> results, Context context) {
        this.results = results;
        mContext = context;
    }

    @Override
    public FollowersAdapter.BH onCreateViewHolder(ViewGroup parent, int viewType) {
        View dayView = LayoutInflater.from(mContext).inflate(R.layout.follower_raw, parent, false);
        return new FollowersAdapter.BH(dayView);
    }

    @Override
    public void onBindViewHolder(FollowersAdapter.BH holder, final int position) {
        Glide.with(mContext).load(results.get(position).profile_image_url).into(holder.profileImage);
        holder.tvName.setText(results.get(position).name);
        holder.tvScreenName.setText(String.format("@%s", results.get(position).screen_name));
        holder.tvBio.setText(results.get(position).description);
        holder.itemView.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, FollowerDetailsActivity.class).putExtra("follower",results.get(position))));
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
        @BindView(R.id.bio) TextView tvBio;

        BH(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}