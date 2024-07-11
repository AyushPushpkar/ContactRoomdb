package com.example.contact

sealed interface ContactEvent {

    object saveContact : ContactEvent

    data class SetName(val Name : String) : ContactEvent
    data class SetNumber(val Number : String) : ContactEvent

    data object ShowDialog : ContactEvent
    data object HideDialog : ContactEvent

    data class SortContact(val sortType: sortType) : ContactEvent
    data class DeleteContact(val contact: Contact) : ContactEvent

    data class ShowUpdateDialog(val contact: Contact) : ContactEvent
    data object HideUpdateDialog : ContactEvent

    data class UpdateContact(val updatedContact: Contact) : ContactEvent

}