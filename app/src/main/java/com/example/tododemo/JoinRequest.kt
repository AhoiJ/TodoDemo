package com.example.tododemo

import java.io.Serializable

class JoinRequest: Serializable {
    companion object Factory{
        fun create(): ToDoList = ToDoList()
    }
    var todoTitle:  String? = null
    var receiverEmail: String? = null
    var todoObjId: String? = null
}