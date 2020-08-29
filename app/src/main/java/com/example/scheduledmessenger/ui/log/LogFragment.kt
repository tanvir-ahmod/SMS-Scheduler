package com.example.scheduledmessenger.ui.log

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentLogBinding
import com.example.scheduledmessenger.ui.timeline.TimelineFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogFragment : BaseFragment<LogViewModel, FragmentLogBinding>() {

    private val logAdapter = LogAdapter(this::onEditClicked)

    override val mViewModel: LogViewModel by viewModels()

    override fun getViewBinding(): FragmentLogBinding = FragmentLogBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.rvLogs.layoutManager = LinearLayoutManager(requireContext())
        mViewBinding.rvLogs.adapter = logAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.logData.observe(viewLifecycleOwner, Observer { logs ->
            logAdapter.addLogData(logs)
            mViewBinding.tvNoDataFound.visibility =
                if (logs.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    private fun onEditClicked(id: Int, isEditable : Boolean) {
        val action = LogFragmentDirections.actionLogFragmentToAddSmsFragment(id, isEditable)
        findNavController().navigate(action)
    }

}
