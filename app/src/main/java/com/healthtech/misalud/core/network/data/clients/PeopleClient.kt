package com.healthtech.misalud.core.network.data.clients

import com.healthtech.misalud.core.network.data.requests.PeopleRequests
import com.healthtech.misalud.core.network.data.responses.PeopleResponses
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PeopleClient {
    @GET("/api/v1/people/user")
    suspend fun doGetUser(
        @Header("Authorization") accessToken: String,
        @Query("phoneNumber") phoneNumber: String
    ) : Response<PeopleResponses.GetUserData>

    @GET("/api/v1/people/getRecordDays")
    suspend fun doGetRecordDays(
        @Header("Authorization") accessToken: String,
        @Query("uuid") uuid: String
    ) : Response<PeopleResponses.GetRecordDays>

    @GET("/api/v1/people/meals")
    suspend fun doGetRecords(
        @Header("Authorization") accessToken: String,
        @Query("uuid") uuid: String,
        @Query("range") range: String
    ) : Response<PeopleResponses.GetMealRecords>

    @POST("/api/v1/people/meals")
    suspend fun doAddRecord(
        @Header("Authorization") accessToken: String,
        @Body addRecordRequest: PeopleRequests.PostMealRecord
    ) : Response<PeopleResponses.PostMealRecord>
}