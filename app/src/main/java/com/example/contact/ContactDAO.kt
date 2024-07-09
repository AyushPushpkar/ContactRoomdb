package com.example.contact

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

//room access data using dao
@Dao
interface ContactDAO {

    @Upsert
    suspend fun upsertContact(contact: Contact) // replaces if already exist

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contact ORDER BY Name ASC")
    fun getContactOrderedByFirstName() : Flow<List<Contact>>

    @Query("SELECT * FROM contact ORDER BY PhoneNumber ASC")
    fun getContactOrderedByPhoneNumber() : Flow<List<Contact>>


}