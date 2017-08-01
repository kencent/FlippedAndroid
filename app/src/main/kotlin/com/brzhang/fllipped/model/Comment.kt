package com.brzhang.fllipped.model


/**
 *
 * Created by brzhang on 2017/8/1.
 * Description :
 */
class Comment {
    var id: Int? = null
    var uid: String? = null
    var ctime: Long? = null
    var contents: ArrayList<Content>? = null
    var floor: Int? = null
    var links: ArrayList<Link>? = null
}


