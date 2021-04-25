package com.example.smsScheduler.ui.show_contacts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smsScheduler.R
import com.example.smsScheduler.base.BaseFragment
import com.example.smsScheduler.databinding.FragmentShowContactsBinding
import com.example.smsScheduler.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview


@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class ShowContactsFragment : BaseFragment<ShowContactsViewModel, FragmentShowContactsBinding>() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    private val contactsAdapter = ContactsAdapter(this::onItemClicked)
    override val mViewModel: ShowContactsViewModel by viewModels()

    override fun getViewBinding(): FragmentShowContactsBinding =
        FragmentShowContactsBinding.inflate(layoutInflater)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //Make sure you have this line of code.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        mViewBinding.rvContacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactsAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpObservers()
    }

    private fun setUpObservers() {
        mViewModel.contacts.observe(viewLifecycleOwner, Observer { contacts ->
            contactsAdapter.addContacts(contacts)
        })
    }

    private fun onItemClicked(contactNumber: String) {
        sharedViewModel.contactNumber.value = contactNumber
        findNavController().navigateUp()
    }

    override fun onDestroy() {
        hideKeyboard(requireContext(), mViewBinding.root)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.show_contact_menu, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = searchViewItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mViewModel.getContactsFromText(newText.toString())
                return false
            }
        })
        return super.onCreateOptionsMenu(menu, inflater)
    }
}
