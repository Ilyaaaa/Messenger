package com.example.ilya.postman.listsData

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.ilya.postman.R

class ItemAdapter(
        c: Context,
        private val data: ArrayList<ItemData>,
        layoutResId: Int,
        var itemActionImageResId: Int
    ): CustomArrayAdapter<ItemData>(c, data, layoutResId){
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getItemId(p0: Int): Long {
        return data[p0].id
    }

    override fun createView(i: Int, view: View): View {
        val item = data[i]
        view.findViewById<TextView>(R.id.item_header).text = item.header
        view.findViewById<TextView>(R.id.item_text).text = item.text
        view.findViewById<ImageView>(R.id.item_action_img).setImageResource(itemActionImageResId)

        return view
    }
}