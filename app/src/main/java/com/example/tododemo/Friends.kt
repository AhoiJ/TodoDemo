package com.example.tododemo

class Friends {

    companion object Factory {
        fun create(): Friends = Friends()
    }

    var objectId: String? = null
    var friend1: String? = null
    var friend2: String? = null
}