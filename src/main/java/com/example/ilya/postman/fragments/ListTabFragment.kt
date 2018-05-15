package com.example.ilya.postman.fragments

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView

import com.example.ilya.postman.R

class ListTabFragment: ListFragment() {
    private lateinit var emptyTextView: TextView
    lateinit var onItemClick: (position: Int) -> Unit


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_list_tab, container, false)
        emptyTextView = view.findViewById(android.R.id.empty)

        val data = arguments
        emptyTextView.text = data.getString("emptyText")

        return view
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        if (v != null) onItemClick(position)
    }


}
