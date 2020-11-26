package com.jama.mpesa_biz_no_detector.ui.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jama.mpesa_biz_no_detector.states.ResultsState

class ResultsViewModel: ViewModel() {

    private val _showProgress = MutableLiveData(false)
    val showProgress = _showProgress

    private val _resultState = MutableLiveData<ResultsState>()
    val resultState = _resultState

    fun showProgress(show: Boolean) {
        _showProgress.value = show
    }

    fun setResultState(resultsState: ResultsState) {
        _resultState.value = resultsState
    }
}