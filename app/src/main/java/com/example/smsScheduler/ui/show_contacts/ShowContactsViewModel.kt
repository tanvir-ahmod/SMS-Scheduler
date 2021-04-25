package com.example.smsScheduler.ui.show_contacts

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smsScheduler.base.BaseViewModel
import com.example.smsScheduler.data.contacts.ContactsRepository
import com.example.smsScheduler.data.contacts.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class ShowContactsViewModel @ViewModelInject constructor(private val contactsRepository: ContactsRepository) :
    BaseViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts
    private val query = MutableStateFlow("")

    init {
        getContactsFromText("")
    }

    fun getContactsFromText(text: String) {
        query.value = text
        viewModelScope.launch {
            query.debounce(300)
                .flatMapLatest { query ->
                    contactsRepository.getContactsByName(query)
                }
                .flowOn(Dispatchers.IO)
                .collect { results ->
                    _contacts.value = results
                }
        }
    }

}