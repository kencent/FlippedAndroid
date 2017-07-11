package com.brzhang.fllipped

import com.brzhang.fllipped.model.Flippedword

/**
 *
 * Created by brzhang on 2017/7/6.
 * Description :
 */
object FlippedHelper {

    fun getText(flippedword: Flippedword?): String {
        if (flippedword == null) {
            return ""
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("text")) {
                return it.text ?: ""
            }
        }
        return ""
    }


    fun getPic(flippedword: Flippedword?): String {
        if (flippedword == null) {
            return ""
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("picture")) {
                return it.text ?: ""
            }
        }
        return ""
    }

    fun getVoice(flippedword: Flippedword?): String {
        if (flippedword == null) {
            return ""
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("audio")) {
                return it.text ?: ""
            }
        }
        return ""
    }

    fun getVideo(flippedword: Flippedword?): String {
        if (flippedword == null) {
            return ""
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("video")) {
                return it.text ?: ""
            }
        }
        return ""
    }

    fun getDistance(flippedword: Flippedword?): String {
        var distance = flippedword?.distance ?: 0
        if (distance > 1000) {
            return String.format("%.1fkm", distance / 1000.0)
        }else{
            return "${distance}m"
        }
    }

}