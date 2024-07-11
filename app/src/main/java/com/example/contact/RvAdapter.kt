package com.example.contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.emreesen.sntoast.SnToast
import com.emreesen.sntoast.Type
import com.example.contact.databinding.ContactupdateBinding
import com.example.contact.databinding.DeletedialogBinding
import com.example.contact.databinding.RvItemBinding

class RvAdapter(var context: Context, var contactList: List<Contact> ,private val onDeleteClick: (Contact) -> Unit ,private val onUpdateClick: (Contact) -> Unit ) :
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
            showDeleteConfirmationDialog(contact)
        }

        holder.binding.updatebtn.setOnClickListener {
            onUpdateClick(contact)
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

          // Apply wobble animation to the root view of each item
//        AnimationUtils.wobbleAnimation(holder.binding.root)


    }

    private fun showDeleteConfirmationDialog(contact: Contact) : AlertDialog {
        val dialogBinding = DeletedialogBinding.inflate(LayoutInflater.from(context))

        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .setTitle("Confirm Delete")
            .setIcon(R.drawable.baseline_delete_24)
            .setCancelable(false)
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialogback)

        dialogBinding.textView3.setText("Are you sure you want to delete ${contact.Name}?")

        dialogBinding.dltbtn.setOnClickListener {
            onDeleteClick(contact)
            dialog.dismiss()

            showDeleteToast("Contact deleted successfully")
        }

        dialogBinding.cancelbtnn.setOnClickListener {
            dialog.dismiss() // Dismiss dialog on cancel
        }

        dialog.show()
        return dialog

    }

    fun updateContacts(newContacts: List<Contact>) {
        contactList = newContacts
        notifyDataSetChanged()
    }

    private fun showDeleteToast(message: String) {
        SnToast.Builder()
            .context(context)
            .type(Type.ERROR)
            .message(message)
            .animation(true)
            .duration(3000)
            .build();
    }
}