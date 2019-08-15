package com.itfitness.simplechat.beans;

public class MessageBean {
    private boolean isMineMsg;
    private String msg;
    private String oppositeUserName;//对方的用户名（如果发消息的不是自己）
    public MessageBean(boolean isMineMsg, String msg) {
        this.isMineMsg = isMineMsg;
        this.msg = msg;
    }

    public boolean isMineMsg() {
        return isMineMsg;
    }

    public void setMineMsg(boolean mineMsg) {
        isMineMsg = mineMsg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageBean(boolean isMineMsg, String msg, String oppositeUserName) {
        this.isMineMsg = isMineMsg;
        this.msg = msg;
        this.oppositeUserName = oppositeUserName;
    }

    public String getOppositeUserName() {
        return oppositeUserName;
    }

    public void setOppositeUserName(String oppositeUserName) {
        this.oppositeUserName = oppositeUserName;
    }
}
