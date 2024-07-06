package com.shashank.cashlessatm.domain

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseViewModel: ViewModel() {

    private val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)
    val ioScope = uiScope.coroutineContext + Dispatchers.IO

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}