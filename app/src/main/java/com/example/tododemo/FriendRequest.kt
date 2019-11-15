package com.example.tododemo

class FriendRequest {
    companion object Factory{
        fun create(): FriendRequest = FriendRequest()
    }
    var objId: String? = null
    var requesterEmail: String? = null
    var hopefulFriend: String? = null
    var accepted: Boolean? = false
}