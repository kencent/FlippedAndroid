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

    var commentnum:Int? = null

    constructor(parcel: Parcel) : this() {
        sendto = parcel.readString()
        lat = parcel.readValue(Double::class.java.classLoader) as? Double
        links = parcel.createTypedArrayList(Link.CREATOR)
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        ctime = parcel.readValue(Long::class.java.classLoader) as? Long
        distance = parcel.readValue(Long::class.java.classLoader) as? Long
        contents = parcel.createTypedArrayList(Content)
        status = parcel.readValue(Int::class.java.classLoader) as? Int
        lng = parcel.readValue(Double::class.java.classLoader) as? Double
        commentnum = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sendto)
        parcel.writeValue(lat)
        parcel.writeTypedList(links)
        parcel.writeValue(id)
        parcel.writeValue(ctime)
        parcel.writeValue(distance)
        parcel.writeTypedList(contents)
        parcel.writeValue(status)
        parcel.writeValue(lng)
        parcel.writeValue(commentnum)
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