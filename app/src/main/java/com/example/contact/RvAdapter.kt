package com.example.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.contact.databinding.RvItemBinding

class RvAdapter(var context: Context, var contactList: List<Contact> ,private val onDeleteClick: (Contact) -> Unit ) :
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

        // Configure Glide for instant loading
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.spidey) // Placeholder image while loading
            .error(R.drawable.spidey) // Error image if loading fails
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) // Cache both original & resized image
            .override(500, 500) // Resize image to improve load time and memory usage

        // Load image using Glide
        Glide.with(context)
            .load("https://picsum.photos/id/${contact.Id}/500/500")
            .apply(requestOptions)
            .into(holder.binding.imageView2)


    }

    fun updateContacts(newContacts: List<Contact>) {
        contactList = newContacts
        notifyDataSetChanged()
    }
}