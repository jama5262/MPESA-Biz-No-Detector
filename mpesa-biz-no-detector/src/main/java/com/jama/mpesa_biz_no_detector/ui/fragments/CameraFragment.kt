package com.jama.mpesa_biz_no_detector.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jama.mpesa_biz_no_detector.R
import kotlinx.android.synthetic.main.fragment_camera.view.*

class CameraFragment : Fragment() {

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_camera, container, false)

        rootView.buttonSuccess.setOnClickListener {
            navigateToSuccess()
        }

        rootView.buttoFail.setOnClickListener {
            navigateToFail()
        }

        return rootView
    }

    private fun navigateToSuccess() {
        findNavController().navigate(R.id.action_cameraFragment_to_successFragment)
    }

    private fun navigateToFail() {
        findNavController().navigate(R.id.action_cameraFragment_to_failFragment)
    }
}