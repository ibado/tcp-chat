package com.bado.ignacio.tcpchat;

class ChatMessage {

    private String mContent;
    private boolean mIsSeen = false;

    public ChatMessage(String mContent) {
        this.mContent = mContent;
    }
}
