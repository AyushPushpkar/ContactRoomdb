package com.example.contact

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// entiity - table structure
@Entity() // default table name = class name
data class Contact(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("Id")
    val Id : Int = 0 ,

    @ColumnInfo("Name")
    val Name : String ,

    @ColumnInfo("PhoneNumber")
    val Number : String
)
