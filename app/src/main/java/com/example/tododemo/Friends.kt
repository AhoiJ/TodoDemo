package com.example.tododemo

class Friends {

    companion object Factory {
        fun create(): Friends = Friends()
    }
    var friends: List<String>? = null
}