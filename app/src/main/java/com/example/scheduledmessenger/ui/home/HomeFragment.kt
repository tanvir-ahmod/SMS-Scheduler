package com.example.scheduledmessenger.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentHomeBinding
import com.example.scheduledmessenger.ui.adapter.EventAdapter
import com.example.scheduledmessenger.ui.timeline.TimelineFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val upcomingEventAdapter = EventAdapter(this::onEditClicked)

    private val sendSmsPermissionCode = 100

    override val mViewModel: HomeViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext())
        mViewBinding.rvUpcomingEvents.adapter = upcomingEventAdapter
        mViewBinding.btnAdd.setOnClickListener {
            if (isGrantedSendSMSPermission()) {
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
        mViewModel.totalSms.observe(viewLifecycleOwner, Observer { totalMessage ->
            mViewBinding.tvTotalSms.text = totalMessage.toString()
        })
        mViewModel.totalSentSms.observe(viewLifecycleOwner, Observer { totalSent ->
            mViewBinding.tvSentSms.text = totalSent.toString()
        })
        mViewModel.totalPendingSms.observe(viewLifecycleOwner, Observer { totalPending ->
            mViewBinding.tvPendingSms.text = totalPending.toString()
        })
        mViewModel.totalFailedSms.observe(viewLifecycleOwner, Observer { totalFailed ->
            mViewBinding.tvFailedSms.text = totalFailed.toString()
        })

        mViewModel.upcomingEvents.observe(viewLifecycleOwner, Observer { events ->
            upcomingEventAdapter.addTimeLineData(events)
        })

    }

    private fun isGrantedSendSMSPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.SEND_SMS
    ) != PackageManager.PERMISSION_GRANTED

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
    }

    private fun gotoAddSmsFragment() {
        findNavController().navigate(R.id.action_homeFragment_to_addSmsFragment)
    }

    private fun onEditClicked(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToAddSmsFragment(id)
        findNavController().navigate(action)
    }
}