package com.example.contact

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.contact.databinding.ActivityMainBinding
import com.example.contact.databinding.ContactupdateBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RvAdapter
    private lateinit var addContactDialog: AlertDialog

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contacts.db"
        ).build()
    }

    private val viewModel by viewModels<ContactViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ContactViewModel(db.dao) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        // Set up RecyclerView
        setupRecyclerView()

        // Set up UI components
        setupUI()

        // Observe ViewModel state
        observeViewModel()

        binding.fab.setOnClickListener {
            viewModel.onEvent(ContactEvent.ShowDialog)
        }

        binding.radioButton1.setOnClickListener {
            viewModel.onEvent(ContactEvent.SortContact(sortType.NAME))
        }

        binding.radioButton2.setOnClickListener {
            viewModel.onEvent(ContactEvent.SortContact(sortType.PHONE_NUMBER))
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                adapter.updateContacts(state.contacts)
                if (state.isAddingContact) {
                    if (!this@MainActivity::addContactDialog.isInitialized || !addContactDialog.isShowing) {
                        addContactDialog = showAddContactDialog()
                        Log.d("MainActivity", "Dialog shown")
                    }
                } else {
                    if (this@MainActivity::addContactDialog.isInitialized && addContactDialog.isShowing) {
                        addContactDialog.dismiss()
                        Log.d("MainActivity", "Dialog dismissed")
                    }
                    Log.d("MainActivity", "Dialog not shown: isAddingContact = ${state.isAddingContact}")
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recview
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RvAdapter(this, emptyList()) { contact ->
            viewModel.onEvent(ContactEvent.DeleteContact(contact))
        }
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                Snackbar.make(binding.root, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .setAnchorView(R.id.fab).show()
            }
        }
        return true
    }

    private fun setupUI() {
        setSupportActionBar(binding.toolbar)

        // Handle FAB click
        binding.fab.setOnClickListener {
            viewModel.onEvent(ContactEvent.ShowDialog)
        }

        // Handle radio button clicks
        binding.radioButton1.setOnClickListener {
            viewModel.onEvent(ContactEvent.SortContact(sortType.NAME))
        }

        binding.radioButton2.setOnClickListener {
            viewModel.onEvent(ContactEvent.SortContact(sortType.PHONE_NUMBER))
        }
    }

    private fun showAddContactDialog(): AlertDialog {
        val dialogBinding = ContactupdateBinding.inflate(LayoutInflater.from(this@MainActivity))

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setTitle("Add contact")
            .setIcon(R.drawable.baseline_edit_square_24)
            .setCancelable(false) // Make the dialog non-cancellable
            .create()

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialogback)

        dialogBinding.saveupbtn.setOnClickListener {
            val name = dialogBinding.updatetitle.text.toString().trim()
            val phoneNumber = dialogBinding.updatedes.text.toString().trim()

            if (name.isEmpty() || phoneNumber.isEmpty()) {
                Snackbar.make(binding.root, "Please enter both name and phone number", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.onEvent(ContactEvent.SetName(name))
            viewModel.onEvent(ContactEvent.SetNumber(phoneNumber))
            viewModel.onEvent(ContactEvent.saveContact)
            dialog.dismiss()
        }

        dialogBinding.cancelbtn.setOnClickListener {
            dialog.dismiss() // Dismiss dialog on cancel
            viewModel.onEvent(ContactEvent.HideDialog)
        }

        dialog.show()
        return dialog
    }
}
