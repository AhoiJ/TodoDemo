package com.example.tododemo

class FriendRequest {
    companion object Factory{
        fun create(): FriendRequest = FriendRequest()
    }
    var objId: String? = null
    var requesterEmail: String? = null
    var hopefulFriendEmail: String? = null
    // possibly a worthless variable // AhoEdits: not worthless
    var accepted: Boolean? = null // true: Accepted, False: denied

    fun toMap(): Map<String, Any?> {

        return mapOf(
            "requesterEmail" to requesterEmail
        )
    }
}