package com.example.scheduledmessenger.ui.timeline

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentTimelineBinding
import com.example.scheduledmessenger.ui.adapter.EventAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimelineFragment : BaseFragment<TimelineViewModel, FragmentTimelineBinding>() {
    override val mViewModel: TimelineViewModel by viewModels()

    private val timelineAdapter =
        EventAdapter()

    override fun getViewBinding(): FragmentTimelineBinding =
        FragmentTimelineBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.rvTimeline.layoutManager = LinearLayoutManager(requireContext())
        mViewBinding.rvTimeline.adapter = timelineAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.timeLineData.observe(viewLifecycleOwner, Observer { events ->
            timelineAdapter.addTimeLineData(events)
        })

    }

}