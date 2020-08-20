package com.example.scheduledmessenger.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.scheduledmessenger.R
import com.example.scheduledmessenger.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addSmsFragment)
        }
    }
}