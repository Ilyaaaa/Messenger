package com.example.ilya.postman.listsData

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.ilya.postman.R

class SettingsAdapter(
        c: Context,
        private val data: ArrayList<SettingData>,
        layoutResId: Int
    ): CustomArrayAdapter<SettingData>(c, data, layoutResId) {

    override fun getItemId(position: Int): Long {
        return data[position].id
    }

    override fun createView(i: Int, view: View): View {
        view.findViewById<ImageView>(R.id.settings_item_img).setImageResource(data[i].imgResId)
        view.findViewById<TextView>(R.id.settings_item_text).text = data[i].name
        return view
    }
}