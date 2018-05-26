package com.example.ilya.postman.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBase(context: Context): SQLiteOpenHelper(context, "db", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL(Queries.CREATE_USERS_TABLE.query)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    private enum class TableNames(val table: String){
        USERS_TABLE("users")
    }

    private enum class Queries(val query: String){
        CREATE_USERS_TABLE("""
            create table ${TableNames.USERS_TABLE.table}(
                id integer primary key,
                email text unique not null,
                login text unique not null,
                name text,
                name2 text
            )
            """.trimIndent()
        )
    }
}