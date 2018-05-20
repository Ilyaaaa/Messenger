package com.example.ilya.postman.listsData

import android.graphics.drawable.Drawable

data class ChatItemData(
        val id: Long,
        val image: Drawable,
        val header: String,
        val lastMessage: String,
        val ownerId: Int,
        val usersId: ArrayList<Int>
)