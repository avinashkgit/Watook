package com.watook.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.watook.R;
import com.watook.application.MyApplication;
import com.watook.model.Chat;
import com.watook.util.Utils;

import java.util.List;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Chat> mChats;

    public ChatRecyclerAdapter(List<Chat> chats) {
        mChats = chats;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    public Chat getLastChatElement() {
        return mChats.get(mChats.size()-1);
    }

    public void deleteAll() {
        mChats.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                MyApplication.getInstance().getUserId())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);
        if (position == 0) {
            myChatViewHolder.txtDate.setVisibility(View.VISIBLE);
            myChatViewHolder.txtDate.setText(Utils.getUserFriendlyDate(chat.timestamp));
        }
//        else {
//            myChatViewHolder.txtDate.setVisibility(View.GONE);
//        }
        else if (position >= 1 && Utils.compareDate(mChats.get(position).timestamp, mChats.get(position - 1).timestamp) != 0) {
            myChatViewHolder.txtDate.setVisibility(View.VISIBLE);
            myChatViewHolder.txtDate.setText(Utils.getUserFriendlyDate(chat.timestamp));
        } else {
            myChatViewHolder.txtDate.setVisibility(View.GONE);
        }

        myChatViewHolder.txtChatMessage.setText(chat.message);
        myChatViewHolder.txtTime.setText(Utils.timeInMillsToTime(chat.timestamp));
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);
        if (position == 0) {
            otherChatViewHolder.txtDate.setVisibility(View.VISIBLE);
            otherChatViewHolder.txtDate.setText(Utils.getUserFriendlyDate(chat.timestamp));
        }
//        else {
//            otherChatViewHolder.txtDate.setVisibility(View.GONE);
//        }
        else if ( position >= 1 && Utils.compareDate(mChats.get(position).timestamp, mChats.get(position - 1).timestamp) != 0) {
            otherChatViewHolder.txtDate.setVisibility(View.VISIBLE);
            otherChatViewHolder.txtDate.setText(Utils.getUserFriendlyDate(chat.timestamp));
        } else {
            otherChatViewHolder.txtDate.setVisibility(View.GONE);
        }
        otherChatViewHolder.txtChatMessage.setText(chat.message);
        otherChatViewHolder.txtTime.setText(Utils.timeInMillsToTime(chat.timestamp));
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mChats.get(position).senderUid.equals(MyApplication.getInstance().getUserId()))
            return VIEW_TYPE_ME;
        else
            return VIEW_TYPE_OTHER;
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtTime, txtDate;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtTime = (TextView) itemView.findViewById(R.id.txt_time);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtTime, txtDate;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtTime = (TextView) itemView.findViewById(R.id.txt_time);
        }
    }
}
