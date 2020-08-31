package com.example.scheduledmessenger.ui.setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.scheduledmessenger.base.BaseFragment
import com.example.scheduledmessenger.databinding.FragmentSettingBinding

class SettingFragment : BaseFragment<SettingViewModel, FragmentSettingBinding>() {

    override val mViewModel: SettingViewModel by viewModels()

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
