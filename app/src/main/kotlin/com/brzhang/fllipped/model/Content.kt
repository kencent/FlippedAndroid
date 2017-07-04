package com.brzhang.fllipped.model

import android.os.Parcel
import android.os.Parcelable

/**
 *
 * Created by brzhang on 2017/7/5.
 * Description :
 */

class Content : Parcelable {
    val link: String

    val type: String

    val text: String

    constructor(link: String, type: String, text: String) {
        this.link = link
        this.type = type
        this.text = text
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.link)
        dest.writeString(this.type)
        dest.writeString(this.text)
    }

    protected constructor(`in`: Parcel) {
        this.link = `in`.readString()
        this.type = `in`.readString()
        this.text = `in`.readString()
    }

    companion object {

        val CREATOR: Parcelable.Creator<Content> = object : Parcelable.Creator<Content> {
            override fun createFromParcel(source: Parcel): Content {
                return Content(source)
            }

            override fun newArray(size: Int): Array<Content?> {
                return arrayOfNulls(size)
            }
        }
    }
}