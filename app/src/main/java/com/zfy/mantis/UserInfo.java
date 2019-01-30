package com.zfy.mantis;

import android.os.Parcel;
import android.os.Parcelable;

import com.zfy.mantis.model.WxInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CreateAt : 2019/1/11
 * Describe :
 *
 * @author chendong
 */
public class UserInfo implements Parcelable {

    private int                 age;
    private String              name;
    private WxInfo              wxInfo;
    private List<WxInfo>        wxInfoList;
    private Map<String, WxInfo> wxInfoMap;


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WxInfo getWxInfo() {
        return wxInfo;
    }

    public void setWxInfo(WxInfo wxInfo) {
        this.wxInfo = wxInfo;
    }

    public List<WxInfo> getWxInfoList() {
        return wxInfoList;
    }

    public void setWxInfoList(List<WxInfo> wxInfoList) {
        this.wxInfoList = wxInfoList;
    }

    public Map<String, WxInfo> getWxInfoMap() {
        return wxInfoMap;
    }

    public void setWxInfoMap(Map<String, WxInfo> wxInfoMap) {
        this.wxInfoMap = wxInfoMap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.age);
        dest.writeString(this.name);
        dest.writeParcelable(this.wxInfo, flags);
        dest.writeList(this.wxInfoList);
        dest.writeInt(this.wxInfoMap.size());
        for (Map.Entry<String, WxInfo> entry : this.wxInfoMap.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.age = in.readInt();
        this.name = in.readString();
        this.wxInfo = in.readParcelable(WxInfo.class.getClassLoader());
        this.wxInfoList = new ArrayList<WxInfo>();
        in.readList(this.wxInfoList, WxInfo.class.getClassLoader());
        int wxInfoMapSize = in.readInt();
        this.wxInfoMap = new HashMap<String, WxInfo>(wxInfoMapSize);
        for (int i = 0; i < wxInfoMapSize; i++) {
            String key = in.readString();
            WxInfo value = in.readParcelable(WxInfo.class.getClassLoader());
            this.wxInfoMap.put(key, value);
        }
    }

    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}
