package com.example.ilya.postman.listsData

import java.util.*

data class MessageItemData (
        val id: Long,
        val text: String,
        val senderId: Int,
        val chatId: Int,
        val sendTime: Date
)