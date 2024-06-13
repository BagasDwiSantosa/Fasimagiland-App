package com.arvl.fasimagiland.data.retrofit

import com.arvl.fasimagiland.data.response.StoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("1707c3c0-a4d4-4b5f-94ff-9284ef51ba62")
    fun getStories(
    ): Call<StoryResponse>;
}
