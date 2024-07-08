package com.example.contact

sealed interface ContactEvent {

    object saveContact : ContactEvent

    data class SetName(val Name : String) : ContactEvent
    data class SetNumber(val Number : String) : ContactEvent

    object ShowDialog : ContactEvent
    object HideDialog : ContactEvent

    data class SortContact(val sortType: sortType) : ContactEvent
    data class DeleteContact(val contact: Contact) : ContactEvent
}