package com.brzhang.fllipped

import com.brzhang.fllipped.model.Flippedword

/**
 *
 * Created by brzhang on 2017/7/6.
 * Description :
 */
object FlippedHelper {
    fun  hasPic(flippedword: Flippedword?): Boolean {
        if (flippedword == null){
            return false
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("picture")){
                return true
            }
        }
        return false
    }

    fun hasVoice(flippedword: Flippedword?): Boolean {
        if (flippedword == null){
            return false
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("audio")){
                return true
            }
        }
        return false
    }

    fun hasVideo(flippedword: Flippedword?): Boolean {
        if (flippedword == null){
            return false
        }

        flippedword.contents?.forEach { it ->
            if (it.type.equals("video")){
                return true
            }
        }
        return false
    }

}