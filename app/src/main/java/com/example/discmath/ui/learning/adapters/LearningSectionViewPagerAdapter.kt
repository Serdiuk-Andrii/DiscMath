package com.example.discmath.ui.learning.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.entity.learning_section.SECTIONS_COLLECTION_STORAGE_PATH

class LearningSectionViewPagerAdapter(fragment: Fragment, val dataset: Array<LearningSection>):
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = dataset.size

    override fun createFragment(position: Int): Fragment {
        val section = dataset[position]
        val fragment = LearningSectionObjectFragment()
        fragment.arguments = Bundle().apply {
            putString(SECTION_NAME_KEY, section.name)
            putString(SECTION_DESCRIPTION_KEY, section.description)
            putString(SECTION_IMAGE_URL_KEY, section.backgroundImageUrl)
            putString(SECTIONS_COLLECTION_STORAGE_PATH, section.collectionPath)
        }
        return fragment
    }

}
