package com.example.discmath.ui.assistant.logic

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

const val CNFBundleKey = "CNF"
const val DNFBundleKey = "DNF"

const val TABS_NUMBER = 2

class LogicFormulaResultsAdapter(fragment: Fragment, private val CNF: String,
                                 private val DNF: String):
    FragmentStateAdapter(fragment)  {

    override fun getItemCount(): Int = TABS_NUMBER

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return LogicFormulaGeneralResultsFragment().apply {
                this.arguments = Bundle().apply {
                    this.putString(CNFBundleKey, CNF)
                    this.putString(DNFBundleKey, DNF)
                }
            }
        }
        return LogicFormulaTruthTableFragment()
    }
}