package com.zfy.mantis.model;

public class WxInfo {
    private long   openId;
    private String nickName;


    public long getOpenId() {
        return openId;
    }

    public void setOpenId(long openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public WxInfo(long openId, String nickName) {
        this.openId = openId;
        this.nickName = nickName;
    }


    @Override
    public String toString() {
        return "WxInfo{" +
                "openId=" + openId +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}