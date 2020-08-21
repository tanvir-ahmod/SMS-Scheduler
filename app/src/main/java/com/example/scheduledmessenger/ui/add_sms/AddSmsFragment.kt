package com.example.scheduledmessenger.ui.add_sms

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentAddSmsBinding
import com.example.scheduledmessenger.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSmsFragment : BaseFragment<AddSmsViewModel, FragmentAddSmsBinding>() {


    private val readContactPermissionCode = 100
    private lateinit var viewDataBinding: FragmentAddSmsBinding

    override val mViewModel: AddSmsViewModel by viewModels()

    override fun getViewBinding(): FragmentAddSmsBinding =
        FragmentAddSmsBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentAddSmsBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.ivShowContacts.setOnClickListener {
            if (isGrantedReadContactPermission()) {
                requestReadContactPermission()
            } else
                gotoContactFragment()
        }
    }

    private fun isGrantedReadContactPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_CONTACTS
    ) != PackageManager.PERMISSION_GRANTED

    private fun requestReadContactPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            readContactPermissionCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == readContactPermissionCode) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                gotoContactFragment()
            } else {
                mViewModel.showErrorMessage.value = "Contact permission required"
            }
        }
    }

    private fun gotoContactFragment() {
        findNavController().navigate(R.id.action_addSmsFragment_to_showContactsFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

}
