package com.dzhuchinski.csvviewer

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.argumentCaptor
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executor

@RunWith(MockitoJUnitRunner::class)
class CsvViewerViewModelTest {

    @Mock
    lateinit var mockContext: Application

    @Mock
    lateinit var observer: Observer<CsvModel>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testCsvModelBuilding() {

        val csvViewerViewModel = CsvViewerViewModel(mockContext, CurrentThreadExecutor())

        csvViewerViewModel.csvModelLiveData.observeForever(observer)

        csvViewerViewModel.parse("title1,title2\n1,2".byteInputStream())

        argumentCaptor<CsvModel>().apply {
            verify(observer).onChanged(capture())

            assertEquals(1, allValues.size)
            val actualCsvModel = firstValue

            assertEquals(true, actualCsvModel.hasHeader)

            assertEquals("title1", actualCsvModel.rows[0][0])
            assertEquals("title2", actualCsvModel.rows[0][1])
            assertEquals("1", actualCsvModel.rows[1][0])
            assertEquals("2", actualCsvModel.rows[1][1])
        }

    }

    private class CurrentThreadExecutor : Executor {
        override fun execute(r: Runnable) {
            r.run()
        }
    }
}