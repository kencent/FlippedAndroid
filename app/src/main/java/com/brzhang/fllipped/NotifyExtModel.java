package com.brzhang.fllipped;

import android.os.Parcel;
import android.os.Parcelable;


public class NotifyExtModel implements Parcelable {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    public NotifyExtModel() {
    }

    protected NotifyExtModel(Parcel in) {
        this.url = in.readString();
    }

    public static final Parcelable.Creator<NotifyExtModel> CREATOR = new Parcelable.Creator<NotifyExtModel>() {
        @Override
        public NotifyExtModel createFromParcel(Parcel source) {
            return new NotifyExtModel(source);
        }

        @Override
        public NotifyExtModel[] newArray(int size) {
            return new NotifyExtModel[size];
        }
    };
}
