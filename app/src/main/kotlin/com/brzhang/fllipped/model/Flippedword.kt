package com.brzhang.fllipped.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by brzhang on 2017/7/5.
 * Description :
 */

class Flippedword() : Parcelable {
    val sendto: String? = null

    val lat: Double? = null

    val links: List<Link>? = null

    val id: Int? = null

    val ctime: Long? = null

    val contents: List<Content>? = null

    val status: Int? = null

    val lng: Double? = null

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

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