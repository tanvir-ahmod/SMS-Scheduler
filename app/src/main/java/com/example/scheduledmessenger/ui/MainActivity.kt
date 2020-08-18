package com.example.scheduledmessenger.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.base.BaseActivity
import com.example.scheduledmessenger.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val mViewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mViewBinding.root)

        mViewModel.totalSms.observe(this, Observer { totalSms ->
            Log.d("Total SMS", "total : $totalSms")
        })
    }
}