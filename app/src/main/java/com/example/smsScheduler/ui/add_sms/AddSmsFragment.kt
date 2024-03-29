package com.example.smsScheduler.ui.add_sms

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.smsScheduler.R
import com.example.smsScheduler.base.BaseFragment
import com.example.smsScheduler.databinding.FragmentAddSmsBinding
import com.example.smsScheduler.ui.MainViewModel
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

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {

        mViewBinding.tvTo.setOnTouchListener(OnTouchListener { _, event ->
            val drawableRight = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (mViewBinding.tvTo.compoundDrawables[drawableRight] != null)
                    if (event.rawX >= mViewBinding.tvTo.right - mViewBinding.tvTo.compoundDrawables[drawableRight].bounds.width()) {
                        if (isGrantedReadContactPermission()) {
                            requestReadContactPermission()
                        } else
                            gotoContactFragment()
                        return@OnTouchListener true
                    }
            }
            false
        })
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
        setUpObservers()
    }

    private fun setUpObservers() {
        mViewModel.actionBarText.observe(viewLifecycleOwner, { title ->
            (activity as AppCompatActivity?)!!.supportActionBar?.title = title
        })

        mViewModel.receiverNumbers.observe(viewLifecycleOwner, { numbers ->
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
            viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let { date ->
                    showDatePicker(date)
                }
            })

        mViewModel.showTimePicker.observe(
            viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let { time ->
                    showTimePicker(time)
                }
            })

        mViewModel.popBack.observe(
            viewLifecycleOwner, {
                it.getContentIfNotHandled()?.let {
                    hideKeyboard(requireContext(), mViewBinding.root)
                    findNavController().navigateUp()
                }
            })

        sharedViewModel.contactNumber.observe(viewLifecycleOwner, { phoneNumber ->
            mViewModel.etReceiverNumber.set(phoneNumber)
        })

        mViewModel.availableSims.observe(viewLifecycleOwner, { sims ->
            mViewBinding.llSimInfoContainer.removeAllViews()
            for (sim in sims) {
                mViewBinding.llSimInfoContainer.addView(sim)
            }
        })
        mViewModel.isEditable.observe(viewLifecycleOwner, { isEditable ->
            if (isEditable) {
                mViewBinding.tvTo.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_contact,
                    0
                )

            } else {
                mViewBinding.tvTo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
        })
    }

    private fun showDatePicker(date: Calendar) {
        val mDatePicker = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                mViewModel.changeDate(selectedYear, selectedMonth, selectedDay)
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        )
        mDatePicker.show()
    }

    private fun showTimePicker(date: Calendar) {
        val mTimePicker = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                mViewModel.changeTime(selectedHour, selectedMinute)
            },
            date.get(Calendar.HOUR_OF_DAY),
            date.get(Calendar.MINUTE),
            false
        )
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
        menu.findItem(R.id.delete)?.isVisible = mViewModel.eventId != 0
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                mViewModel.addSMS()
                true
            }
            R.id.delete -> {
                onDeleteClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        sharedViewModel.contactNumber.value = ""
        super.onDestroy()
    }

    private fun onDeleteClicked() {
        AlertDialog.Builder(requireContext())
            .setTitle(requireContext().resources.getString(R.string.delete_entry_title))
            .setMessage(requireContext().resources.getString(R.string.delete_entry_message))
            .setPositiveButton(
                requireContext().resources.getString(R.string.delete_yes)
            ) { _, _ ->
                mViewModel.deleteEvent()
            }
            .setNegativeButton(
                requireContext().resources.getString(R.string.delete_no),
                null
            )
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}
