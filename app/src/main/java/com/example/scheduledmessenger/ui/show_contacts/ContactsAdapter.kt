package com.example.scheduledmessenger.ui.show_contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.example.scheduledmessenger.data.contacts.model.Contact
import com.example.scheduledmessenger.databinding.ItemContactBinding


class ContactsAdapter(private val onItemClicked: (number: String) -> Unit) :
    RecyclerView.Adapter<ContactsAdapter.CameraHolder>() {
    private var contacts: List<Contact> = arrayListOf()

    fun addContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    class CameraHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var generator: ColorGenerator = ColorGenerator.MATERIAL
        fun bindData(contact: Contact, onItemClicked: (number: String) -> Unit) {
            binding.tvName.text = contact.name
            binding.tvPhone.text = contact.phoneNumber

            if (contact.photoUri == null) {
                //Show letter icon
                val letter = contact.name?.substring(0, 1)
                letter?.let {
                    val drawable = TextDrawable.builder()
                        .buildRound(letter, generator.randomColor)

                    binding.ivContactImage.setImageDrawable(drawable)
                }
            } else {
                Glide.with(binding.ivContactImage.context).load(contact.photoUri)
                    .into(binding.ivContactImage)
            }

            binding.rootLayout.setOnClickListener {
                contact.phoneNumber?.let { phoneNumber ->
                    onItemClicked(phoneNumber)
                }

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