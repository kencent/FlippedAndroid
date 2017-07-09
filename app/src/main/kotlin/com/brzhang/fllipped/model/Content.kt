package com.brzhang.fllipped.model

import android.os.Parcel
import android.os.Parcelable

/**
 *
 * Created by brzhang on 2017/7/5.
 * Description :
 */

class Content() : Parcelable {
    enum class Type(val type:String){
        TEXT("text"),
        PICUTRE("picture"),
        VIDEO("video"),
        AUDIO("audio")

    }
    var link: String? = null

    var type: String? = null

    var text: String? = null

    constructor(parcel: Parcel) : this() {
        link = parcel.readString()
        type = parcel.readString()
        text = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(link)
        parcel.writeString(type)
        parcel.writeString(text)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Content> {
        override fun createFromParcel(parcel: Parcel): Content {
            return Content(parcel)
        }

        override fun newArray(size: Int): Array<Content?> {
            return arrayOfNulls(size)
        }
    }

}