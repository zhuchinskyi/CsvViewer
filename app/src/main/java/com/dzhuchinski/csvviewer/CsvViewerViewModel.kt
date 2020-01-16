package com.dzhuchinski.csvviewer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRow
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.Executor

class CsvViewerViewModel(
    private val app: Application,
    private val executor: Executor
) : AndroidViewModel(app) {

    val csvModelLiveData: MutableLiveData<CsvModel> by lazy {
        MutableLiveData<CsvModel>()
    }

    fun parse(inputStream: InputStream) {
        executor.execute {
            val reader = InputStreamReader(inputStream)

            val csvReader = CsvReader()

            val rowItems = mutableListOf<List<String>>()

            csvReader.parse(reader).use { csvParser ->

                var csvRow: CsvRow?

                while (csvParser.nextRow().also { csvRow = it } != null) {
                    rowItems.add(csvRow!!.fields)
                }
            }

            csvModelLiveData.postValue(CsvModel(true, rowItems))
        }
    }
}

data class CsvModel(val hasHeader: Boolean, val rows: List<List<String>>)
