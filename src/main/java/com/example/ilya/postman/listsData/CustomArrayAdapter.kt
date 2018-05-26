package com.example.ilya.postman.listsData

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

abstract class CustomArrayAdapter<T>(c: Context,
    private val data: ArrayList<T>,
    private val layoutResId: Int
    ): ArrayAdapter<T>(c,layoutResId){
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun add(item: T?) {
        if (item != null) {
            data.add(item)
            notifyDataSetChanged()
        }
    }

    fun add(i: Int, item: T?) {
        if (item != null) {
            data.add(i, item)
            notifyDataSetChanged()
        }
    }

    override fun addAll(collection: MutableCollection<out T>?) {
        data.addAll(collection!!)
        notifyDataSetChanged()
    }

    override fun remove(`object`: T?) {
        data.remove(`object`)
        notifyDataSetChanged()
    }

    fun removeAll(collection: Collection<T>){
        data.removeAll(collection)
        notifyDataSetChanged()
    }

    override fun clear() {
        data.clear()
        notifyDataSetChanged()
    }

    fun contains(item: T): Boolean{
        return data.contains(item)
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(p0: Int): T {
        return data[p0]
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view = p1
        if (view == null) view = inflater.inflate(layoutResId, p2, false)

        return createView(p0, view!!)
    }

    abstract fun createView(i: Int, view: View): View
}