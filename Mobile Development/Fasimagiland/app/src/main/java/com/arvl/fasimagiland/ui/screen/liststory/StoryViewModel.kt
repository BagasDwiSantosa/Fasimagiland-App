package com.arvl.fasimagiland.ui.screen.liststory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arvl.fasimagiland.data.response.StoriesItem
import com.arvl.fasimagiland.data.response.StoryResponse
import com.arvl.fasimagiland.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {

    private val _storyList = MutableLiveData<List<StoriesItem>>()
    val storyList: LiveData<List<StoriesItem>> = _storyList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchStories() {
        _isLoading.value = true
        val apiService = ApiConfig.getApiService()
        apiService.getStories().enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.stories?.let {
                        _storyList.value = it.filterNotNull()
                    }
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                // Handle failure
            }
        })
    }
}
