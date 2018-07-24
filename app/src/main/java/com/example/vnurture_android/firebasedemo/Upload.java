package com.example.vnurture_android.firebasedemo;

import com.google.firebase.database.Exclude;

public class Upload {
    String mName;
    String mImageurl;
    String mKey;

    public Upload(String mName, String mImageurl) {
        if (mName.trim().equals("")){
            mName="No Name";
        }
        this.mName = mName;
        this.mImageurl = mImageurl;
    }

    public Upload() {
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmImageurl() {
        return mImageurl;
    }

    public void setmImageurl(String mImageurl) {
        this.mImageurl = mImageurl;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
