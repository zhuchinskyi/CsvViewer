package com.dzhuchinski.csvviewer

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.concurrent.Executors


class MainActivity : FragmentActivity() {
    var FILE_NAME = "issues.csv"

    private val csvViewModel by lazy {
        ViewModelProviders.of(
            this,
            CsvViewerViewModelFactory(application, Executors.newSingleThreadExecutor())
        ).get(CsvViewerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpTableView()

        csvViewModel.csvModelLiveData.observe(this, Observer {
            var startIndex = 0

            if (it.hasHeader) {
                view_table_layout.addView(buildHeader(it.rows[startIndex++]))
            }

            for (i in startIndex until it.rows.size) {
                view_table_layout.addView(buildRow(it.rows[i]))
            }
        })

        try {
            val csvInputStream = assets.open(FILE_NAME)
            csvViewModel.parse(csvInputStream)
        } catch (e: IOException) {
            Toast.makeText(this, "Cannot open file: $FILE_NAME", Toast.LENGTH_LONG).show()
        }
    }

    private fun buildHeader(rowItems: List<String>) =
        buildRow(rowItems, Color.BLACK, Color.WHITE, 300)

    private fun buildRow(
        rowItems: List<String>,
        bgColor: Int = Color.WHITE,
        textColor: Int = Color.BLACK,
        rowHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    ): TableRow = buildTableRow(bgColor, rowHeight).apply {
        rowItems.forEach {
            addView(buildTextView(it, textColor))
        }
    }

    private fun buildTextView(item: String, textColor: Int): TextView = TextView(this).apply {
        layoutParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        text = item
        setTextColor(textColor)
        gravity = Gravity.CENTER
    }

    private fun buildTableRow(bgColor: Int, rowHeight: Int): TableRow = TableRow(this).apply {
        setBackgroundColor(bgColor)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            rowHeight
        )
    }

    private fun setUpTableView() {
        view_table_layout.isStretchAllColumns = true
        view_table_layout.isShrinkAllColumns = true
    }
}
