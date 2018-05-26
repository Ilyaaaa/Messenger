package com.example.ilya.postman.listsData

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.ilya.postman.R
import com.makeramen.roundedimageview.RoundedImageView

class ChatItemAdapter (c: Context,
        private val data: ArrayList<ChatItemData>,
        layoutResId: Int
    ): CustomArrayAdapter<ChatItemData>(c, data, layoutResId){
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItemId(p0: Int): Long {
        return data[p0].id
    }

    override fun createView(i: Int, view: View): View {
        val item = data[i]
        view.findViewById<ImageView>(R.id.chat_item_budge).setImageDrawable(item.unreadMsgBudge.get(item.unreadMsgCount))
        view.findViewById<RoundedImageView>(R.id.chat_item_img).setImageDrawable(item.image)
        view.findViewById<TextView>(R.id.chat_item_header).text = item.header
        view.findViewById<TextView>(R.id.chat_item_last_msg).text = item.lastMessage

        return view
    }
}