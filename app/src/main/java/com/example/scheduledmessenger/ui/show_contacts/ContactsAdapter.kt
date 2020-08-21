package com.example.scheduledmessenger.ui.show_contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledmessenger.data.contacts.model.Contact
import com.example.scheduledmessenger.databinding.ItemContactBinding


class ContactsAdapter(private val onItemClicked: () -> Unit) :
    RecyclerView.Adapter<ContactsAdapter.CameraHolder>() {

    private var contacts: List<Contact> = arrayListOf()

    fun addContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    class CameraHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(contact: Contact, onItemClicked: () -> Unit) {
            binding.tvName.text = contact.name
            binding.tvPhone.text = contact.phoneNumber

            binding.rootLayout.setOnClickListener {
                onItemClicked()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraHolder {

        val binding =
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CameraHolder(binding)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: CameraHolder, position: Int) {
        holder.bindData(contacts[position], onItemClicked)
    }
}