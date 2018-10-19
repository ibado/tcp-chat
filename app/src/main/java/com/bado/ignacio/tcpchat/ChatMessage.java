package com.bado.ignacio.tcpchat;

class ChatMessage {

    private String mContent;
    private boolean mIsMine;

    ChatMessage(String content, boolean isMine) {
        mContent = content;
        mIsMine = isMine;

    }

    public String getContent() {
        return mContent;
    }

    public boolean isMine() {
        return mIsMine;
    }
}
