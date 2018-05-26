package com.example.ilya.postman.listsData

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.ilya.postman.R
import com.example.ilya.postman.data.User
import com.makeramen.roundedimageview.RoundedImageView
import java.text.SimpleDateFormat
import java.util.*

class MessageItemAdapter(c: Context,
                         private val data: ArrayList<MessageItemData>,
                         layoutResId: Int
    ): CustomArrayAdapter<MessageItemData>(c, data, layoutResId){
    private val timeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
    private val shortTimeFormat = SimpleDateFormat("dd.MM HH:mm")
    private val timeFormatWithoutDate = SimpleDateFormat("HH:mm")


    override fun getItemId(p0: Int): Long {
        return data[p0].id
    }

    override fun createView(i:Int, view: View): View {
        val imageView = view.findViewById<RoundedImageView>(R.id.msg_item_img)
        val headerField = view.findViewById<TextView>(R.id.msg_item_header)
        val textField = view.findViewById<TextView>(R.id.msg_item_text)
        val timeField = view.findViewById<TextView>(R.id.msg_item_time)
        headerField.text = data[i].header
        textField.text = data[i].text

        val currentDate = Calendar.getInstance(TimeZone.getDefault())
        val msgDate = Calendar.getInstance(TimeZone.getDefault())
        currentDate.time = Date()
        msgDate.time = data[i].sendTime

        if (currentDate.get(Calendar.YEAR) != msgDate.get(Calendar.YEAR))
            timeField.text = timeFormat.format(msgDate.time)
        else if (currentDate.get(Calendar.DAY_OF_MONTH) != msgDate.get(Calendar.DAY_OF_MONTH))
            timeField.text = shortTimeFormat.format(msgDate.time)
        else timeField.text = timeFormatWithoutDate.format(msgDate.time)

        if (data[i].senderId == User.getId(context)) {
            (view.findViewById<RelativeLayout>(R.id.msg_container).layoutParams as LinearLayout.LayoutParams)
                    .setMargins(context.resources.getDimension(R.dimen.msg_margin).toInt(), 0, 0, 0)

            val imageParams = imageView.layoutParams as RelativeLayout.LayoutParams
            val headerParams = headerField.layoutParams as RelativeLayout.LayoutParams
            val textParams = textField.layoutParams as RelativeLayout.LayoutParams
            val timeParams = timeField.layoutParams as RelativeLayout.LayoutParams

            imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)

            timeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
            timeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT)

            headerParams.addRule(RelativeLayout.RIGHT_OF, 0)
            headerParams.addRule(RelativeLayout.LEFT_OF, R.id.msg_item_img)

            textParams.addRule(RelativeLayout.RIGHT_OF, 0)
            textParams.addRule(RelativeLayout.LEFT_OF, R.id.msg_item_img)
        }

        return view
    }
}