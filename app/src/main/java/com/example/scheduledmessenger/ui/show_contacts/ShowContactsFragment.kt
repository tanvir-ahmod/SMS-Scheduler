package com.example.scheduledmessenger.ui.show_contacts

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentShowContactsBinding
import com.example.scheduledmessenger.ui.MainViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        mViewBinding.rvContacts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = contactsAdapter
        }

        mViewBinding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mViewModel.getContactsFromText(p0.toString())
            }

        })
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


}
