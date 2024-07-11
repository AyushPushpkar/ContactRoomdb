package com.example.contact

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(private val dao: ContactDAO , private val context: Context) : ViewModel() {

    private val _state = MutableStateFlow(ContactState())
    private val _sortType = MutableStateFlow(sortType.NAME)
    private val _contacts = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                com.example.contact.sortType.NAME -> dao.getContactOrderedByFirstName()
                com.example.contact.sortType.PHONE_NUMBER -> dao.getContactOrderedByPhoneNumber()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _contacts, _sortType) { state, contacts, sortType ->
        state.copy(
            sortType = sortType,
            contacts = contacts
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ContactState())

    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.DeleteContact -> {
                viewModelScope.launch {
                    dao.deleteContact(event.contact)
                }
            }


            ContactEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = false
                    )
                }
            }

            is ContactEvent.SetName -> {
                _state.update {
                    it.copy(
                        name = event.Name
                    )
                }
            }

            is ContactEvent.SetNumber -> {
                _state.update {
                    it.copy(
                        number = event.Number
                    )
                }
            }

            is ContactEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingContact = true
                    )
                }
            }

            is ContactEvent.SortContact -> {
                _sortType.value = event.sortType
            }

            ContactEvent.saveContact -> {
                val firstName = _state.value.name
                val phoneNumber = _state.value.number

                if (firstName.isBlank() || phoneNumber.isBlank()) {
                    return
                }
                // Check for duplicate name
                viewModelScope.launch {
                    val existingName = dao.getContactByName(firstName)
                    if (existingName != null) {
                        // Show toast for duplicate name
                        Toast.makeText(context, "Contact already exists !", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // Check for duplicate phone number
                    val existingNumber = dao.getContactByPhoneNumber(phoneNumber)
                    if (existingNumber != null) {
                        // Show toast for duplicate phone number
                        Toast.makeText(context, "Phone number $phoneNumber already in use", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val contact = Contact(Name = firstName, Number = phoneNumber)
                    viewModelScope.launch {
                        dao.upsertContact(contact)
                        _state.update { it.copy(isAddingContact = false, name = "", number = "") }
                    }
                }
            }

            ContactEvent.HideUpdateDialog -> {
                _state.update {
                    it.copy(
                        isUpdatingContact = false,
                        contactToUpdate = null
                    )
                }
            }

            is ContactEvent.UpdateContact -> {
                val updatedContact = event.updatedContact

                if (updatedContact.Name.isBlank() || updatedContact.Number.isBlank()) {
                    return
                }

                // Check for duplicate name (optional)
                viewModelScope.launch {
                    val existingName = dao.getContactByName(updatedContact.Name)
                    if (existingName != null && existingName.Id != updatedContact.Id) {
                        // Show toast for duplicate name
                        Toast.makeText(context, "Contact with this name already exists!", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // Check for duplicate phone number (optional)
                    val existingNumber = dao.getContactByPhoneNumber(updatedContact.Number)
                    if (existingNumber != null && existingNumber.Id != updatedContact.Id) {
                        // Show toast for duplicate phone number
                        Toast.makeText(context, "Phone number ${updatedContact.Number} already in use", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // Update contact in database
                    dao.upsertContact(updatedContact)

                    // Update state to reflect changes
                    _state.update { currentState ->
                        val updatedContacts = currentState.contacts.map { contact ->
                            if (contact.Id == updatedContact.Id) updatedContact else contact
                        }
                        currentState.copy(
                            contacts = updatedContacts,
                            isUpdatingContact = false
                        )
                    }
                }
            }
            is ContactEvent.ShowUpdateDialog -> {
                _state.update {
                    it.copy(
                        isUpdatingContact = true,
                        contactToUpdate = event.contact ,
                        name = event.contact.Name,
                        number = event.contact.Number
                    )
                }
            }
        }
    }

}
