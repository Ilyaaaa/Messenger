package com.example.ilya.postman.listsData

import android.content.Context
import android.graphics.drawable.VectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.ilya.postman.R

class ItemAdapter(
        c: Context,
        private val data: ArrayList<ItemData>,
        layoutResId: Int,
        var itemActionImageResId: Int
    ): ArrayAdapter<ItemData>(c,layoutResId){
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun add(`object`: ItemData?) {
        if (`object` != null) {
            data.add(`object`)
            notifyDataSetChanged()
        }
    }

    override fun addAll(collection: MutableCollection<out ItemData>?) {
        data.addAll(collection!!)
        notifyDataSetChanged()
    }

    override fun remove(`object`: ItemData?) {
        data.remove(`object`)
        notifyDataSetChanged()
    }

    fun removeAll(collection: Collection<ItemData>){
        data.removeAll(collection)
        notifyDataSetChanged()
    }

    override fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun contains(item: ItemData): Boolean{
        return data.contains(item)
    }

    override fun getItemId(p0: Int): Long {
        return data[p0].id
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): ItemData {
        return data[p0]
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1
        if (view == null) view = inflater.inflate(R.layout.list_item, p2, false)

        val item = data[p0]
        view!!.findViewById<TextView>(R.id.item_header).text = item.header
        view.findViewById<TextView>(R.id.item_text).text = item.text
        view.findViewById<ImageView>(R.id.item_action_img).setImageResource(itemActionImageResId)

        return view
    }
}