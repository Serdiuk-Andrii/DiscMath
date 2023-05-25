package com.example.discmath.ui.assistant.logic

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

const val CNF_BUNDLE_KEY = "CNF"
const val DNF_BUNDLE_KEY = "DNF"
const val LOGIC_FORMULA_CLASS_STRING_RESOURCE_ID_BUNDLE_KEY = "STRING_CLASS"

const val TABS_NUMBER = 2

class LogicFormulaResultsAdapter(fragment: Fragment, private val CNF: String,
                                 private val DNF: String, private val formulaClassStringId: Int):
    FragmentStateAdapter(fragment)  {

    override fun getItemCount(): Int = TABS_NUMBER

    override fun createFragment(position: Int): Fragment {
        if (position == 0) {
            return LogicFormulaGeneralResultsFragment().apply {
                this.arguments = Bundle().apply {
                    this.putString(CNF_BUNDLE_KEY, CNF)
                    this.putString(DNF_BUNDLE_KEY, DNF)
                    this.putInt(LOGIC_FORMULA_CLASS_STRING_RESOURCE_ID_BUNDLE_KEY,
                        formulaClassStringId)
                }
            }
        }
        return LogicFormulaTruthTableFragment()
    }
}