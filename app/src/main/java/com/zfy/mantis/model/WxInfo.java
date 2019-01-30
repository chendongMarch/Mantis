package com.zfy.mantis.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WxInfo implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.openId);
        dest.writeString(this.nickName);
    }

    protected WxInfo(Parcel in) {
        this.openId = in.readLong();
        this.nickName = in.readString();
    }

    public static final Parcelable.Creator<WxInfo> CREATOR = new Parcelable.Creator<WxInfo>() {
        @Override
        public WxInfo createFromParcel(Parcel source) {
            return new WxInfo(source);
        }

        @Override
        public WxInfo[] newArray(int size) {
            return new WxInfo[size];
        }
    };
}