package com.example.scheduledmessenger.ui.add_sms

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentAddSmsBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddSmsFragment : BaseFragment<AddSmsViewModel, FragmentAddSmsBinding>() {


    private val readContactPermissionCode = 100

    override val mViewModel: AddSmsViewModel by viewModels()

    override fun getViewBinding(): FragmentAddSmsBinding =
        FragmentAddSmsBinding.inflate(layoutInflater).apply {
            viewModel = mViewModel
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpObservers()
    }

    private fun setUpObservers() {
        mViewModel.receiverNumbers.observe(viewLifecycleOwner, Observer { numbers ->

            mViewBinding.chipGroupNumbers.removeAllViews()

            for ((position, number) in numbers.withIndex()) {
                val chip = layoutInflater.inflate(
                    R.layout.chip_view,
                    mViewBinding.chipGroupNumbers,
                    false
                ) as Chip
                chip.text = number
                chip.setOnCloseIconClickListener {
                    mViewModel.removeNumber(position)
                }
                mViewBinding.chipGroupNumbers.addView(chip)
            }
        })

        mViewModel.showDatePicker.observe(
            viewLifecycleOwner, Observer { date ->
                date?.let {
                    showDatePicker(date)
                }
            })

        mViewModel.showTimePicker.observe(
            viewLifecycleOwner, Observer { date ->
                date?.let {
                    showTimePicker(date)
                }
            })

    }

    private fun initListeners() {

        mViewBinding.ivShowContacts.setOnClickListener {
            if (isGrantedReadContactPermission()) {
                requestReadContactPermission()
            } else
                gotoContactFragment()
        }
    }

    private fun showDatePicker(date: Calendar) {
        val mDatePicker = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                mViewModel.changeDate(selectedYear, selectedMonth, selectedDay)
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
        mDatePicker.show()
    }

    private fun showTimePicker(date: Calendar) {
        val mDatePicker = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                mViewModel.changeTime(selectedHour, selectedMinute)
            },
            date.get(Calendar.HOUR_OF_DAY),
            date.get(Calendar.MINUTE),
            false
        )
        mDatePicker.show()
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
