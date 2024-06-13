package com.arvl.fasimagiland.ui.screen.liststory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arvl.fasimagiland.data.response.StoriesItem
import com.arvl.fasimagiland.databinding.ItemStoryBinding

class StoryAdapter(private var storyList: List<StoriesItem>, private val onItemClick: (StoriesItem) -> Unit) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    fun submitList(newList: List<StoriesItem>) {
        storyList = newList
        notifyDataSetChanged()
    }

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoriesItem) {
            binding.tvStoryTitle.text = story.title
            binding.tvStoryDifficulty.text = story.difficulty
            itemView.setOnClickListener {
                onItemClick(story)
            }
        }
    }
}
