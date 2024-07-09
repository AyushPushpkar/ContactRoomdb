package com.example.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.RvItemBinding

class RvAdapter(var context: Context, var contactList: List<Contact> ,private val onDeleteClick: (Contact) -> Unit) :
    RecyclerView.Adapter<RvAdapter.viewHolder>() {

    inner class viewHolder(var binding: RvItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var view = RvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        val contact = contactList[position]
        holder.binding.tvname.text = contact.Name
        holder.binding.textView.text = contact.Number

        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(contact)
        }
    }

    fun updateContacts(newContacts: List<Contact>) {
        contactList = newContacts
        notifyDataSetChanged()
    }
}