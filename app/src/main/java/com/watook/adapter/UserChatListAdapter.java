package com.watook.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
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
import com.watook.application.MyApplication;
import com.watook.model.UserChat;
import com.watook.util.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Avinash.Kumar on 07-Nov-17.
 */

public class UserChatListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<UserChat> users;

    private static final int FOOTER_VIEW = 1;

    public UserChatListAdapter(Activity activity, List<UserChat> users) {
        this.activity = activity;
        this.users = users;

        Collections.sort(users, Collections.reverseOrder(new Comparator<UserChat>() {
            @Override
            public int compare(UserChat o1, UserChat o2) {
                return o1.getLastModified().compareTo(o2.getLastModified());
            }
        }));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == FOOTER_VIEW) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_footer, parent, false);
            return new UserChatListAdapter.FooterViewHolder(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_default, parent, false);
        return new UserChatListAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof UserChatListAdapter.RecyclerViewHolder) {
                UserChatListAdapter.RecyclerViewHolder vh = (UserChatListAdapter.RecyclerViewHolder) holder;
                vh.bindView(position);

            } else if (holder instanceof UserChatListAdapter.FooterViewHolder) {
                UserChatListAdapter.FooterViewHolder vh = (UserChatListAdapter.FooterViewHolder) holder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (users == null) {
            return 0;
        }

        if (users.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return users.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == users.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        return super.getItemViewType(position);
    }


    public class FooterViewHolder extends UserChatListAdapter.ViewHolder {
        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }


    class RecyclerViewHolder extends UserChatListAdapter.ViewHolder {
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
            final UserChat user = UserChatListAdapter.this.users.get(position);
            tvName.setText(Utils.emptyIfNull(user.getName()));
            tvDist.setText(Utils.emptyIfNull(user.getLastMessage()));
            if (user.getSentById() != Long.parseLong(MyApplication.getInstance().getUserId()))
                tvDist.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.ic_reply_blue_grey_600_18dp), null, null, null);
            else
                tvDist.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.ic_reply_recieved_blue_grey_600_18dp), null, null, null);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChatActivity.startActivity(activity,
                            user.getName(),
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
