package com.example.tododemo

class ToDoList {
    companion object Factory{
        fun create(): ToDoList = ToDoList()
    }
    var objId: String? = null
    var creatorId: String? = null
    var memberId: List<String>? = null
    var title: String? = null
    var tasks: List<String>? = null
    var taskDone: List<Int>? = null
    var done: Boolean? = false
    var startTime: String? = null

}
