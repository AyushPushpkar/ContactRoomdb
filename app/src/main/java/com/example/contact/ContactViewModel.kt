package com.example.contact

import android.util.Log
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
class ContactViewModel(private val dao: ContactDAO) : ViewModel() {

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

                val contact = Contact(Name = firstName, Number = phoneNumber)
                viewModelScope.launch {
                    dao.upsertContact(contact)
                    _state.update { it.copy(isAddingContact = false, name = "", number = "") }
                }
            }
        }
    }
}
