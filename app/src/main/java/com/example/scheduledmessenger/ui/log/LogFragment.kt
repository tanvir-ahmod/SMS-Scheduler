package com.example.scheduledmessenger.ui.log

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentLogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogFragment : BaseFragment<LogViewModel, FragmentLogBinding>() {

    private val logAdapter = LogAdapter()

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
        })
    }

}
