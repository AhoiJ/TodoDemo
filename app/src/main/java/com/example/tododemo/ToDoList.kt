package com.example.tododemo

class ToDoList {
    companion object Factory{
        fun create(): ToDoList = ToDoList()
    }
    var objId: String? = null
    var creatorId: String? = null
    var title: String? = null
    // done boolean should be on each task, may need to implement "subObject" 
    var tasks: List<String>? = null
    var done: Boolean? = false
    var startTime: String? = null

}
