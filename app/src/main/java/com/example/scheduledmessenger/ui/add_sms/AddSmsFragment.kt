package com.example.scheduledmessenger.ui.add_sms

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentAddSmsBinding
import com.example.scheduledmessenger.ui.MainViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class AddSmsFragment : BaseFragment<AddSmsViewModel, FragmentAddSmsBinding>() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    private val readContactPermissionCode = 100
    private val readPhoneStatePermissionCode = 200

    private val args: AddSmsFragmentArgs by navArgs()

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
        showAvailableSims()
        initListeners()
    }

    private fun showAvailableSims() {
        if (isGrantedReadPhoneStatePermission()) {
            requestReadPhoneStatePermission()
        } else
            mViewModel.showSimCards()
    }

    private fun initListeners() {
        mViewBinding.ivShowContacts.setOnClickListener {
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

    private fun isGrantedReadPhoneStatePermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_PHONE_STATE
    ) != PackageManager.PERMISSION_GRANTED

    private fun requestReadContactPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            readContactPermissionCode
        )
    }

    private fun requestReadPhoneStatePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            readPhoneStatePermissionCode
        )
    }

    private fun gotoContactFragment() {
        findNavController().navigate(R.id.action_addSmsFragment_to_showContactsFragment)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.setEventId(args.eventId)
        mViewModel.setIsEditable(args.isEditable)
        setUpObservers()
    }

    private fun setUpObservers() {
        mViewModel.actionBarText.observe(viewLifecycleOwner, Observer { title ->
            (activity as AppCompatActivity?)!!.supportActionBar?.title = title
        })

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

        mViewModel.popBack.observe(
            viewLifecycleOwner, Observer { date ->
                date?.let { isPopBack ->
                    if (isPopBack) {
                        hideKeyboard(requireContext(), mViewBinding.root)
                        findNavController().navigateUp()
                    }
                }
            })

        sharedViewModel.contactNumber.observe(viewLifecycleOwner, Observer { phoneNumber ->
            mViewModel.etReceiverNumber.set(phoneNumber)
        })

        mViewModel.availableSims.observe(viewLifecycleOwner, Observer { sims ->
            mViewBinding.llSimInfoContainer.removeAllViews()
            for (sim in sims) {
                mViewBinding.llSimInfoContainer.addView(sim)
            }
        })
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

        mDatePicker.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { _, which ->
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                mViewModel.hideDatePicker()
            }
        }
        mDatePicker.show()
    }

    private fun showTimePicker(date: Calendar) {
        val mTimePicker = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                mViewModel.changeTime(selectedHour, selectedMinute)
            },
            date.get(Calendar.HOUR_OF_DAY),
            date.get(Calendar.MINUTE),
            false
        )

        mTimePicker.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            getString(R.string.cancel)
        ) { _, which ->
            if (which == DialogInterface.BUTTON_NEGATIVE) {
                mViewModel.hideTimePicker()
            }
        }
        mTimePicker.show()
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
                mViewModel.showMessage.value = "Contact permission required"
            }
        } else if (requestCode == readPhoneStatePermissionCode) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                mViewModel.showSimCards()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                mViewModel.addSMS()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        sharedViewModel.contactNumber.value = ""
        super.onDestroy()
    }
}
