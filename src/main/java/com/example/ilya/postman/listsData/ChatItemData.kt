package com.example.ilya.postman.listsData

import android.graphics.drawable.Drawable
import com.minibugdev.drawablebadge.DrawableBadge

data class ChatItemData(
        val id: Long,
        val image: Drawable,
        val unreadMsgBudge: DrawableBadge,
        var unreadMsgCount: Int,
        val header: String,
        var lastMessage: String,
        val ownerId: Int,
        val usersId: ArrayList<Int>
)