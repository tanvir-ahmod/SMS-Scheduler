package com.example.scheduledmessenger.ui.add_sms

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.databinding.FragmentAddSmsBinding
import com.example.scheduledmessenger.databinding.FragmentHomeBinding

class AddSmsFragment : Fragment(R.layout.fragment_add_sms) {
    private lateinit var viewDataBinding: FragmentAddSmsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentAddSmsBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
