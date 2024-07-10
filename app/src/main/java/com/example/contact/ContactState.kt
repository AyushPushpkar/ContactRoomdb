package com.example.contact

data class ContactState(
    val contacts : List<Contact> = emptyList(),
    val name : String ="" ,
    val number : String = "" ,
    val isAddingContact : Boolean =  false ,
    val sortType: sortType = com.example.contact.sortType.NAME ,
)
