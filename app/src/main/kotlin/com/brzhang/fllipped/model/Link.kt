package com.brzhang.fllipped.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by brzhang on 2017/7/5.
 * Description :
 */

class Link : Parcelable {
    val rel: String

    val uri: String

    val method: String

    constructor(rel: String, uri: String, method: String) {
        this.rel = rel
        this.uri = uri
        this.method = method
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.rel)
        dest.writeString(this.uri)
        dest.writeString(this.method)
    }

    protected constructor(`in`: Parcel) {
        this.rel = `in`.readString()
        this.uri = `in`.readString()
        this.method = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<Link> = object : Parcelable.Creator<Link> {
            override fun createFromParcel(source: Parcel): Link {
                return Link(source)
            }

            override fun newArray(size: Int): Array<Link?> {
                return arrayOfNulls(size)
            }
        }
    }
}
