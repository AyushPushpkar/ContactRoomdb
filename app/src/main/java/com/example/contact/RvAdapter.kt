package com.example.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.databinding.RvItemBinding

class RvAdapter(var context: Context, var contactList: List<Contact>) :
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
        holder.binding.tvname.text = contactList[position].Name
        holder.binding.textView.text = contactList[position].Number
    }
}