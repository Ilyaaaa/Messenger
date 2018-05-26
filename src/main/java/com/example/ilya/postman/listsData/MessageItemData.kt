package com.example.ilya.postman.listsData

import java.util.*

data class MessageItemData (
        val id: Long,
        val header: String,
        val text: String,
        val senderId: Int,
        val chatId: Int,
        val sendTime: Date
)