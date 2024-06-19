package com.bangkit.coldswiftapps.data.remote

import com.bangkit.coldswiftapps.data.remote.response.AllTicketResponse
import com.bangkit.coldswiftapps.data.remote.response.AllTicketResponseItem
import com.bangkit.coldswiftapps.data.remote.response.BuyTiketResponse
import com.bangkit.coldswiftapps.data.remote.response.DetailEventResponse
import com.bangkit.coldswiftapps.data.remote.response.ListEventResponse
import com.bangkit.coldswiftapps.data.remote.response.LoginResponse
import com.bangkit.coldswiftapps.data.remote.response.ProfileResponse
import com.bangkit.coldswiftapps.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("api/users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("events")
    fun getAllEvent(): Call<List<ListEventResponse>>

    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: String,
    ): DetailEventResponse

    @POST("events/{id}/purchase")
    suspend fun purchaseEvent(
        @Path("id") id: String,
    ): BuyTiketResponse

    @GET("mytickets")
    fun getAllTicket(): Call<List<AllTicketResponseItem>>

    @Multipart
    @POST("api/users/register")
    suspend fun register(
        @Part ktp: MultipartBody.Part,
        @Part photo: MultipartBody.Part,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
    ) : RegisterResponse


    @GET("api/users/profile")
    suspend fun getProfile() : ProfileResponse

}