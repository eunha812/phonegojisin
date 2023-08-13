package com.ssafy.phonesin.network.service


import com.ssafy.phonesin.model.Address
import com.ssafy.phonesin.model.AgentAddress
import com.ssafy.phonesin.model.Donation
import com.ssafy.phonesin.model.ErrorResponse
import com.ssafy.phonesin.model.PhotoResponse
import com.ssafy.phonesin.model.RentalBody
import com.ssafy.phonesin.model.RentalCountResponse
import com.ssafy.phonesin.model.RentalResponse
import com.ssafy.phonesin.model.Return
import com.ssafy.phonesin.network.NetworkResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {
    @Multipart
    @POST("/ytwok/apply")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): NetworkResponse<PhotoResponse, ErrorResponse>


    @GET("/rental/apply/list")
    suspend fun getRentalList(): RentalResponse

    @GET("/address/list")
    suspend fun getAddressList(): NetworkResponse<List<Address>, ErrorResponse>

    @GET("/address/list/samsung")
    suspend fun getAgentAddressList(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): NetworkResponse<List<AgentAddress>, ErrorResponse>

    @GET("/address/list/samsung/search")
    suspend fun getSearchAgentAddressList(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("search") search: String
    ): NetworkResponse<List<AgentAddress>, ErrorResponse>

    @GET("/rental/apply/count")
    suspend fun getPossibleRentalCount(): NetworkResponse<RentalCountResponse, ErrorResponse>


    @POST("/back/apply")
    suspend fun postReturn(@Body backDtos: List<Return>)

    @POST("/rental/apply")
    suspend fun postRental(@Body rentalApplylistDto: List<RentalBody>)


    @POST("/donation/apply")
    suspend fun uploadDonation(@Body donationRequestDto: Donation): NetworkResponse<String, ErrorResponse>

    @GET("donation/king")
    suspend fun getKing(): NetworkResponse<PhotoResponse, ErrorResponse>

}