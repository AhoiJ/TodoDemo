package com.example.tododemo

class Friends {

    companion object Factory {
        fun create(): Friends = Friends()
    }

    var objectId: String? = null
    var friendName: String? = null
}