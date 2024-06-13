package com.arvl.fasimagiland.data.response

import com.google.gson.annotations.SerializedName

data class StoryResponse(

	@field:SerializedName("stories")
	val stories: List<StoriesItem?>? = null
)

data class StoriesItem(

	@field:SerializedName("difficulty")
	val difficulty: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("story")
	val story: List<String?>? = null
)
