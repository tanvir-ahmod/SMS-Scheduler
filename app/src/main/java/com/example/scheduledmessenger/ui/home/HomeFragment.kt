package com.example.scheduledmessenger.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentHomeBinding
import com.example.scheduledmessenger.ui.adapter.EventAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    private val upcomingEventAdapter = EventAdapter()

    override val mViewModel: HomeViewModel by viewModels()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext())
        mViewBinding.rvUpcomingEvents.adapter = upcomingEventAdapter
        mViewBinding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addSmsFragment)
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

}