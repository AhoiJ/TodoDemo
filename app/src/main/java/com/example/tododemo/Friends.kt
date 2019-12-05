package com.example.tododemo

class Friends {

    companion object Factory {
        fun create(): Friends = Friends()
    }
    var friends: MutableList<String>? = null


     fun clear(){

        friends!!.clear()
    }

}