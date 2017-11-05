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

public class NearByAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<UserListResponse.UserList> userList;

    private static final int FOOTER_VIEW = 1;

    public NearByAdapter(Activity activity, List<UserListResponse.UserList> userList) {
        this.activity = activity;
        this.userList = userList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == FOOTER_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_footer, parent, false);
            return new FooterViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_near_by, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof RecyclerViewHolder) {
                RecyclerViewHolder vh = (RecyclerViewHolder) holder;
                vh.bindView(position);

            } else if (holder instanceof FooterViewHolder) {
                FooterViewHolder vh = (FooterViewHolder) holder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (userList == null) {
            return 0;
        }

        if (userList.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return userList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == userList.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }


    public class FooterViewHolder extends ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    class RecyclerViewHolder extends ViewHolder {
        RecyclerViewHolder(View itemView) {
            super(itemView);

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define elements of a row here
        TextView tvName, tvDist;
        RelativeLayout rlBack;
        ImageView ivProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            // Find view by ID and initialize here
            tvName = (TextView) itemView.findViewById(R.id.txt_name);
            tvDist = (TextView) itemView.findViewById(R.id.txt_dist);
            rlBack = (RelativeLayout) itemView.findViewById(R.id.rl_profile_back);
            ivProfile = (ImageView) itemView.findViewById(R.id.profile_pic);
        }

        public void bindView(int position) {
            final UserListResponse.UserList user = userList.get(position);
            tvName.setText(Utils.emptyIfNull(user.getFirstName()) + " " + Utils.emptyIfNull(user.getLastName()));
            if (!Utils.isEmpty(String.valueOf(user.getLocation().getLatitude()))
                    && !Utils.isEmpty(String.valueOf(user.getLocation().getLongitude()))) {
                tvDist.setText(Utils.getDistance(activity, user.getLocation().getLatitude(), user.getLocation().getLongitude()));
            } else
                tvDist.setText(String.valueOf("NA"));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.startActivity(activity,
                            user.getFirstName() + " " + user.getLastName(),
                            String.valueOf(user.getUserId()),
                            user.getFireBaseToken());
                }
            });


            Glide.with(activity).load(Utils.emptyIfNull(user.getProfileImage())).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ivProfile.setImageDrawable(circularBitmapDrawable);
                }
            });
        }
    }
}
