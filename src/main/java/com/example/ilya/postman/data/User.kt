package com.example.ilya.postman.data

import android.content.Context

object User {
    private const val PREF_NAME = "UserPrefs"

    fun setId(context: Context, id: Int){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("id", id)
        editor.apply()
    }

    fun getId(context: Context): Int?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt("id", -1)
        return if (id != -1) id
        else null
    }

    fun setName(context: Context, name: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.apply()
    }

    fun getName(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("name", null)
    }

    fun setEmail(context: Context, email: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun getEmail(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("email", null)
    }

    fun setPass(context: Context, pass: String){
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("pass", pass)
        editor.apply()
    }

    fun getPass(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("pass", null)
    }
}