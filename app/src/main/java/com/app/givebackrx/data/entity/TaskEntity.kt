package com.app.givebackrx.data.entity

data class TaskEntity(
    val auth:Auth,
    val count: Int,
    val `data`: TaskData,
    val message: String,
    val success: Boolean
)

data class TaskData(
    val my_tasks: List<MyTaskItem>
)

data class MyTaskItem(
    val assigned_on: String,
    val description: String,
    val due_date: String,
    val task_id: String,
    val task_name: String,
    var task_status: String
)