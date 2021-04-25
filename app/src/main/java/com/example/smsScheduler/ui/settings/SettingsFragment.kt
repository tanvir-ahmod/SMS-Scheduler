package com.example.smsScheduler.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.smsScheduler.base.BaseFragment
import com.example.smsScheduler.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsViewModel, FragmentSettingBinding>() {

    override val mViewModel: SettingsViewModel by viewModels()

    override fun getViewBinding(): FragmentSettingBinding =
        FragmentSettingBinding.inflate(layoutInflater).apply {
            viewModel = mViewModel
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewBinding.autoStartContainer.setOnClickListener {
            gotoAutoStartSetting()
        }
    }
}
