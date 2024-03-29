package com.example.newsapi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapi.databinding.FragmentNewsListBinding

class NewsListFragment : Fragment(), NewsListAdapter.OnItemClickListener {

    private var _binding: FragmentNewsListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val newsListViewModel: NewsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = NewsListAdapter(emptyList(), this) // pass in `this` as listener for NewsListAdapter
        binding.newsRecyclerView.adapter = adapter

        // Initialize Spinner
        val categorySpinner = binding.categorySpinner
        val categoryList = listOf("Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology") // Example category list
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = spinnerAdapter

        // Spinner item selection listener
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categoryList[position]
                newsListViewModel.fetchNewsByCategory(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Observe changes to the newsList LiveData
        newsListViewModel.newsList.observe(viewLifecycleOwner) { newsList ->
            adapter.updateNewsList(newsList)
        }

        return binding.root
    }

    override fun onItemClick(news: News) {
        val bundle = Bundle().apply {
            // Pass individual parameters
            putString("title", news.title)
            putString("description", news.description)
            putString("content", news.content)
            putString("imageUrl", news.urlToImage)
        }
        // Navigate to NewsDetailFragment using NavController
        findNavController().navigate(
            R.id.action_newsListFragment_to_fragmentNewsDetail,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
