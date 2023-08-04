package com.ssafy.phonesin.repository.donation

import com.ssafy.phonesin.model.Donation
import com.ssafy.phonesin.model.ErrorResponse
import com.ssafy.phonesin.network.NetworkResponse

interface DonationRepository {
    suspend fun uploadDonation(donation: Donation): NetworkResponse<String, ErrorResponse>
}