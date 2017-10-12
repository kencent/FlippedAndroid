package com.brzhang.fllipped;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoollyzhang on 2017/10/12.
 */


public class ContractModel implements Parcelable {

    private String name;
    private String num;

    public ContractModel(String name, String num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }


    @Override
    public String toString() {
        return name + "-" + num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.num);
    }

    public ContractModel() {
    }

    protected ContractModel(Parcel in) {
        this.name = in.readString();
        this.num = in.readString();
    }

    public static final Parcelable.Creator<ContractModel> CREATOR = new Parcelable.Creator<ContractModel>() {
        @Override
        public ContractModel createFromParcel(Parcel source) {
            return new ContractModel(source);
        }

        @Override
        public ContractModel[] newArray(int size) {
            return new ContractModel[size];
        }
    };
}
