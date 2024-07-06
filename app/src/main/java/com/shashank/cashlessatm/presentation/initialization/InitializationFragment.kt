package com.shashank.cashlessatm.presentation.initialization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shashank.cashlessatm.databinding.FragmentInitializationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitializationFragment : Fragment() {

    private var _binding: FragmentInitializationBinding? = null
    private val binding: FragmentInitializationBinding get() = _binding!!

    private val viewModel: CoreViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInitializationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnInit.setOnClickListener {
                viewModel.initialize()
            }
        }

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}