package com.example.smsScheduler.ui.timeline

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smsScheduler.R
import com.example.smsScheduler.base.BaseFragment
import com.example.smsScheduler.databinding.FragmentTimelineBinding
import com.example.smsScheduler.ui.MainViewModel
import com.example.smsScheduler.ui.adapter.EventAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TimelineFragment : BaseFragment<TimelineViewModel, FragmentTimelineBinding>() {

    private val sharedViewModel: MainViewModel by activityViewModels()

    override val mViewModel: TimelineViewModel by viewModels()

    private val timelineAdapter =
        EventAdapter(this::onEditClicked, this::onDeleteClicked)

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
            mViewBinding.tvNoDataFound.visibility =
                if (events.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun onEditClicked(id: Int) {
        val action = TimelineFragmentDirections.actionTimelineFragmentToAddSmsFragment(id)
        findNavController().navigate(action)
    }


    private fun onDeleteClicked(eventId: Int) {

        AlertDialog.Builder(requireContext())
            .setTitle(requireContext().resources.getString(R.string.delete_entry_title))
            .setMessage(requireContext().resources.getString(R.string.delete_entry_message))
            .setPositiveButton(
                requireContext().resources.getString(R.string.delete_yes)
            ) { _, _ ->
                sharedViewModel.deleteEvent(eventId)
            }
            .setNegativeButton(
                requireContext().resources.getString(R.string.delete_no),
                null
            )
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()

    }
}