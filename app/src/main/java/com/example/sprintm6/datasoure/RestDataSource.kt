package com.example.sprintm6.datasoure

import com.example.sprintm6.model.ApiResponse
import com.example.sprintm6.util.Constants.Companion.ENDPOINT_LOCATION
import com.example.sprintm6.util.Constants.Companion.ENDPOINT_NAME
import com.example.sprintm6.util.Constants.Companion.ENDPOINT_PICTURE
import retrofit2.http.GET

interface RestDataSource {

    @GET(ENDPOINT_NAME)
    suspend fun getUserName(): ApiResponse

    @GET(ENDPOINT_PICTURE)
    suspend fun getUserPicture(): ApiResponse

    @GET(ENDPOINT_LOCATION)
    suspend fun getUserLocation(): ApiResponse

}