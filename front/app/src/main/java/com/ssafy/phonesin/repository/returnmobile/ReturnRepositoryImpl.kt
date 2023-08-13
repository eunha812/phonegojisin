package com.ssafy.phonesin.repository.returnmobile

import com.ssafy.phonesin.model.RentalResponse
import com.ssafy.phonesin.model.Return
import com.ssafy.phonesin.network.service.ApiService
import javax.inject.Inject

class ReturnRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ReturnRepository {

    override suspend fun getRentalList(): RentalResponse {
        return apiService.getRentalList()
    }

    override suspend fun postReturnList(returnList: List<Return>) {
        return apiService.postReturn(returnList)
    }
}