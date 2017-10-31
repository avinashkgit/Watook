package com.watook.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.watook.R;
import com.watook.activity.ChatActivity;
import com.watook.application.GPSTracker;
import com.watook.model.response.UserListResponse;
import com.watook.util.Utils;

import java.util.List;

/**
 * Created by Avinash.Kumar on 24-Oct-17.
 */

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.RecyclerViewHolder> {

    Activity activity;
    List<UserListResponse.UserList> userList;
    GPSTracker gpsTracker;

    public NearByAdapter(Activity activity, List<UserListResponse.UserList> userList) {
        this.activity = activity;
        this.userList = userList;
        gpsTracker = new GPSTracker(activity);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_near_by, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final UserListResponse.UserList user = userList.get(position);
        holder.tvName.setText(Utils.emptyIfNull(user.getFirstName()) + " " + Utils.emptyIfNull(user.getLastName()));
        if (!Utils.isEmpty(String.valueOf(user.getLocation().getLatitude()))
                && !Utils.isEmpty(String.valueOf(user.getLocation().getLongitude()))) {
            holder.tvDist.setText(Utils.getDistance(activity, user.getLocation().getLatitude(), user.getLocation().getLongitude()));
        } else
            holder.tvDist.setText(String.valueOf("NA"));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatActivity.startActivity(activity,
                        user.getEmailId(),
                        String.valueOf(user.getUserId()),
                        user.getFireBaseToken());
            }
        });


        Glide.with(activity).load(Utils.emptyIfNull(user.getProfileImage())).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.ivProfile) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.ivProfile.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDist;
        RelativeLayout rlBack;
        ImageView ivProfile;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.txt_name);
            tvDist = (TextView) itemView.findViewById(R.id.txt_dist);
            rlBack = (RelativeLayout) itemView.findViewById(R.id.rl_profile_back);
            ivProfile = (ImageView) itemView.findViewById(R.id.profile_pic);
        }


    }
}
