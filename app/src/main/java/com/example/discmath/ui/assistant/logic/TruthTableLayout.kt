package com.example.discmath.ui.assistant.logic

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.discmath.R
import com.example.set_theory.logic.TruthTable

class TruthTableLayout(private val currentContext: Context, attributeSet: AttributeSet):
    TableLayout(currentContext, attributeSet) {


    fun fillTable(truthTable: TruthTable) {
        fillTable(truthTable, 0)
    }

    fun fillTable(truthTable: TruthTable, verticalPadding: Int) {
        fillTable(truthTable, verticalPadding) {_, _ -> }
    }

    fun fillTable(truthTable: TruthTable, verticalPadding: Int, callback: ((TableRow, Int) -> Unit)) {
        val rowBackground = ContextCompat.getDrawable(currentContext,
            R.drawable.row_background)
        val rowGravity = Gravity.CENTER

        truthTable.symbols.add('F')
        val titleRow = TableRow(context)
        titleRow.background = rowBackground
        for (value in truthTable.symbols) {
            val characterTextView = TextView(context)
            characterTextView.text = value.toString()
            characterTextView.setTextColor(Color.BLACK)
            characterTextView.setTypeface(null, Typeface.BOLD)
            characterTextView.gravity = rowGravity
            titleRow.addView(characterTextView)
        }
        titleRow.setPadding(0, verticalPadding, 0, verticalPadding)
        this.addView(titleRow)

        for ((index, row) in truthTable.rows.withIndex()) {
            val result = row.second
            val currentViewRow = TableRow(context)
            currentViewRow.background = rowBackground
            for (value in row.first) {
                val rowTextView = TextView(context)
                rowTextView.text = value.toInteger().toString()
                rowTextView.setTextColor(Color.BLACK)
                rowTextView.gravity = rowGravity
                currentViewRow.addView(rowTextView)
            }
            val resultTextView = TextView(context)
            resultTextView.setTextColor(Color.BLACK)
            resultTextView.gravity = rowGravity
            resultTextView.text = result.toInteger().toString()
            currentViewRow.addView(resultTextView)
            currentViewRow.setPadding(0, verticalPadding, 0, verticalPadding)
            currentViewRow.setOnClickListener {
                callback(currentViewRow, index)
            }
            this.addView(currentViewRow)
        }
    }

    fun clear() {
        this.removeAllViews()
    }

}