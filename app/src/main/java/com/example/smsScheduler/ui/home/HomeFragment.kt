package com.example.smsScheduler.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smsScheduler.R
import com.example.smsScheduler.base.BaseFragment
import com.example.smsScheduler.databinding.FragmentHomeBinding
import com.example.smsScheduler.ui.MainViewModel
import com.example.smsScheduler.ui.adapter.EventAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val upcomingEventAdapter = EventAdapter(this::onLaunchButtonClicked)

    private val sendSmsPermissionCode = 100
    private val readContactPermissionCode = 200

    override val mViewModel: HomeViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext())
        mViewBinding.rvUpcomingEvents.adapter = upcomingEventAdapter
        mViewBinding.btnAdd.setOnClickListener {
            if (!isGrantedSendSMSPermission()) {
                requestSendSmsPermission()
            } else
                gotoAddSmsFragment()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpObservers()
    }

    private fun setUpObservers() {
        mViewModel.totalSms.observe(viewLifecycleOwner, { totalMessage ->
            mViewBinding.tvTotalSms.text = totalMessage.toString()
        })
        mViewModel.totalSentSms.observe(viewLifecycleOwner, { totalSent ->
            mViewBinding.tvSentSms.text = totalSent.toString()
        })
        mViewModel.totalPendingSms.observe(viewLifecycleOwner, { totalPending ->
            mViewBinding.tvPendingSms.text = totalPending.toString()
        })
        mViewModel.totalFailedSms.observe(viewLifecycleOwner, { totalFailed ->
            mViewBinding.tvFailedSms.text = totalFailed.toString()
        })
        mViewModel.upcomingEvents.observe(viewLifecycleOwner, { events ->
            upcomingEventAdapter.addTimeLineData(events)
            mViewBinding.tvNoDataFound.visibility =
                if (events.isEmpty()) View.VISIBLE else View.GONE
        })
        mViewModel.hasUpcomingEvents.observe(viewLifecycleOwner, { hasEvents ->
            if (hasEvents) {
                if (!isGrantedReadContactPermission()) {
                    requestReadContactPermission()
                } else {
                    mViewModel.getUpcomingEvents()
                }
            } else {
                mViewBinding.tvNoDataFound.visibility = View.VISIBLE
            }
        })
    }

    private fun isGrantedSendSMSPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.SEND_SMS
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestSendSmsPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.SEND_SMS),
            sendSmsPermissionCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == sendSmsPermissionCode) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                gotoAddSmsFragment()
            } else {
                mViewModel.showMessage.value = "Send message permission required"
            }
        }

        if (requestCode == readContactPermissionCode) {
            if (grantResults.isNotEmpty()
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                mViewModel.getUpcomingEvents()
            } else {
                mViewModel.showMessage.value = "Read permission required to show upcoming events!"
            }
        }
    }

    private fun gotoAddSmsFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_addSmsFragment)
    }

    private fun onLaunchButtonClicked(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToAddSmsFragment(id)
        findNavController().navigate(action)
    }


    private fun isGrantedReadContactPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestReadContactPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            readContactPermissionCode
        )
    }


}