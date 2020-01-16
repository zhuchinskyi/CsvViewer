package com.dzhuchinski.csvviewer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.ExecutorService

class CsvViewerViewModelFactory(
    private val app: Application,
    private val executorService: ExecutorService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CsvViewerViewModel::class.java)) {
            return CsvViewerViewModel(app, executorService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}