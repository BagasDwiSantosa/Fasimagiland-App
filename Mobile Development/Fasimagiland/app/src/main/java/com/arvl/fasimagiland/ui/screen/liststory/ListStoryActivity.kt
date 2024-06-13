package com.arvl.fasimagiland.ui.screen.liststory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arvl.fasimagiland.databinding.ActivityListStoryBinding
import com.arvl.fasimagiland.ui.screen.canvas.CanvasActivity

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var viewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupViewModel()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        storyAdapter = StoryAdapter(emptyList()) { story ->
            val intent = Intent(this, CanvasActivity::class.java)
            intent.putExtra("FIRST_SENTENCE", story.story?.firstOrNull())
            startActivity(intent)
        }
        binding.recyclerView.adapter = storyAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(StoryViewModel::class.java)
        viewModel.fetchStories()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this, { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.storyList.observe(this, { stories ->
            storyAdapter.submitList(stories)
        })
    }
}
