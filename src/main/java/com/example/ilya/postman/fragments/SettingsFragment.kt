package com.example.ilya.postman.fragments

import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

import com.example.ilya.postman.R

class SettingsFragment : ListFragment() {
    private val userSettingsFragment = UserSettingsFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_tab, container, false)
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        if (v == null) return

        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
        when (id) {
            USER_SETTINGS_ID -> {
                fragmentTransaction.replace(R.id.settings_container, userSettingsFragment)
            }
        }

        fragmentTransaction.commit()
    }

    fun setLoginChangeResult (success: Boolean, error: String) {
        userSettingsFragment.setLoginChangeResult(success, error)
    }

    fun setNameChangeResult (success: Boolean, error: String) {
        userSettingsFragment.setNameChangeResult(success, error)
    }

    fun setSurnameChangeResult (success: Boolean, error: String) {
        userSettingsFragment.setSurnameChangeResult(success, error)
    }

    fun setPassChangeResult (success: Boolean, error: String) {
        userSettingsFragment.setPassChangeResult(success, error)
    }

    companion object {
        const val USER_SETTINGS_ID = 0L
    }
}
