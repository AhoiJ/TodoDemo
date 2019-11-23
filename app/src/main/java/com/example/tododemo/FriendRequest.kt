package com.example.tododemo

class FriendRequest {
    companion object Factory{
        fun create(): FriendRequest = FriendRequest()
    }
    var objId: String? = null
    var requesterEmail: String? = null
    var hopefulFriendEmail: String? = null
    // possibly a worthless variable
    var accepted: Boolean? = false
}