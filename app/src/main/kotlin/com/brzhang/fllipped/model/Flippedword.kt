package com.brzhang.fllipped.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by brzhang on 2017/7/5.
 * Description :
 */

class Flippedword() : Parcelable {
    var sendto: String? = null

    var lat: Double? = null

    var links: List<Link>? = null

    var id: Int? = null

    var ctime: Long? = null

    var distance:Long? = null

    var contents: List<Content>? = null

    var status: Int? = null

    var lng: Double? = null

    constructor(parcel: Parcel) : this() {
        distance = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(distance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Flippedword> {
        override fun createFromParcel(parcel: Parcel): Flippedword {
            return Flippedword(parcel)
        }

        override fun newArray(size: Int): Array<Flippedword?> {
            return arrayOfNulls(size)
        }
    }

}