package com.bado.ignacio.tcpchat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder>{

    private List<ChatMessage> mDataSet;

    ChatMessageAdapter(List<ChatMessage> list) {
        mDataSet = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_message_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage item = mDataSet.get(position);
        holder.setMessage(item.getContent());
        holder.setIsMine(item.isMine());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvMessage;

        ViewHolder(View itemView) {
            super(itemView);
            mTvMessage = itemView.findViewById(R.id.tv_message);
        }

        void setMessage(String message) {
            mTvMessage.setText(message);
        }

        void setIsMine(boolean isMine) {
            if (isMine) {
                mTvMessage.setGravity(Gravity.END);
            }
        }
    }
}
