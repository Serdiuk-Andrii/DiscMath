package com.example.discmath.ui.learning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.discmath.R
import com.example.discmath.databinding.FragmentLearningSectionsBinding
import com.example.discmath.entity.learning_section.LearningSection
import com.example.discmath.entity.learning_section.SECTIONS_COLLECTION_STORAGE_PATH
import com.example.discmath.ui.learning.adapters.LearningSectionViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Keys for intents
const val COLLECTION_PATH_KEY = "collection"
const val SECTION_NAME_KEY = "name"

class SectionsLearningFragment : Fragment() {

    // Firebase
    private var db = Firebase.firestore


    // Views
    private lateinit var sectionsViewPager :ViewPager2
    private lateinit var tabLayout: TabLayout

    private var _binding: FragmentLearningSectionsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLearningSectionsBinding.inflate(inflater, container, false)
        initializeViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db.collection(SECTIONS_COLLECTION_STORAGE_PATH).get().addOnSuccessListener {
                querySnapshot ->
            val sections: List<LearningSection> = querySnapshot.documents.map {
                    documentSnapshot -> LearningSection(documentSnapshot)
            }.sorted()
            sectionsViewPager.adapter = LearningSectionViewPagerAdapter(
                this,
                sections.toTypedArray(), ::navigateToLearningSection)
            TabLayoutMediator(tabLayout, sectionsViewPager) { tab, position ->
                tab.text = sections[position].name
            }.attach()
        }
    }

    private fun initializeViews() {
        sectionsViewPager = binding.learningSectionsPager
        tabLayout = binding.learningSectionsTabLayout
    }

    private fun navigateToLearningSection(view: View, learningSection: LearningSection) {
        val transitionName = getString(R.string.learning_section_transition_name)
        val extras = FragmentNavigatorExtras(view to transitionName)
        val navController = findNavController()
        navController.navigate(
            R.id.specificLearningSectionFragment,
            Bundle().apply {
                putString(SECTION_NAME_KEY, learningSection.name)
                putString(COLLECTION_PATH_KEY, learningSection.getLearningElementsPath())
            },
            null,
            extras
        )
    }

    /*override fun onResume() {
        Toast.makeText(context, "Resuming", Toast.LENGTH_SHORT).show()
        val adapter = LearningItemAdapter(
            viewModel.items.value!!.toTypedArray()
        ) {
            navigateToLearningItem(it)
        }
        val learningItems: RecyclerView = binding.learningSectionsRecyclerView
        learningItems.adapter = adapter
        super.onResume()
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}