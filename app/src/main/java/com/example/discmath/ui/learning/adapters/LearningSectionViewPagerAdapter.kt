package com.example.discmath.ui.learning.adapters

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.discmath.entity.learning_section.LearningSection

class LearningSectionViewPagerAdapter(fragment: Fragment,
                                      val dataset: Array<LearningSection>,
                                      private val clickCallback: ((View,
                                          LearningSection) -> Unit))
    : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = dataset.size

    override fun createFragment(position: Int): Fragment {
        val section = dataset[position]
        /*
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putString(SECTION_NAME_KEY, section.name)
        }
         */
        return LearningSectionObjectFragment(section, clickCallback)
    }

}
