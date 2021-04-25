package com.example.smsScheduler.base

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar


abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {
    protected abstract val mViewModel: VM

    protected lateinit var mViewBinding: VB

    abstract fun getViewBinding(): VB

    private var loader = Loader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = getViewBinding()
        mViewModel.showLoader.observe(this, Observer { isShow ->
            if (isShow) {
                try {
                    loader.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                loader.isCancelable = false
                loader.show(supportFragmentManager, "loader")
            } else {
                try {
                    loader.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        mViewModel.showMessage.observe(this, Observer { message ->
            if (!message.isNullOrEmpty()) {
                Snackbar.make(mViewBinding.root, message, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    fun showSnackBar(message: String) {
        runOnUiThread {
            Snackbar.make(mViewBinding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    protected fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mViewBinding.root.windowToken, 0)
    }

}