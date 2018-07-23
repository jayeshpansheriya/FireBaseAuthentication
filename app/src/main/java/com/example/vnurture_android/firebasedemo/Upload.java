package com.example.vnurture_android.firebasedemo;

public class Upload {
    String mName;
    String mImageurl;

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
}
