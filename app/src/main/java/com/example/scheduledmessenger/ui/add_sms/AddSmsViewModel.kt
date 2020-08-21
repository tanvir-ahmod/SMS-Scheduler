package com.example.scheduledmessenger.ui.add_sms

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scheduledmessenger.base.BaseViewModel
import com.example.scheduledmessenger.data.contacts.ContactsRepository
import com.example.scheduledmessenger.data.contacts.model.Contact
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddSmsViewModel @ViewModelInject constructor() :
    BaseViewModel() {
}