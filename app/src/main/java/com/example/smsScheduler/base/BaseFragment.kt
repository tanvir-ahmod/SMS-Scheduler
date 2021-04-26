package com.example.smsScheduler.base

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar


abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {
    protected abstract val mViewModel: VM

    protected lateinit var mViewBinding: VB

    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = getViewBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return mViewBinding.root
    }

    protected fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun gotoAutoStartSetting(){
        val intent = Intent()
        intent.component = ComponentName(
            "com.miui.securitycenter",
            "com.miui.permcenter.autostart.AutoStartManagementActivity"
        )
        startActivity(intent)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.showMessage.observe(requireActivity(), { message ->
            if (!message.isNullOrEmpty()) {
                Snackbar.make(mViewBinding.root, message, Snackbar.LENGTH_LONG).show()
            }
        })
    }
}