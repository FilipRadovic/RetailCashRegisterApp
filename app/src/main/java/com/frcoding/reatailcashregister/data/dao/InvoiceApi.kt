package com.frcoding.reatailcashregister.data.dao

import com.frcoding.reatailcashregister.data.dto.InvoiceDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InvoiceApi {
    @POST("invoices")
    suspend fun insertInvoices(@Body invoice: InvoiceDto): Response<Unit>

    @GET("invoices/{userId}")
    suspend fun getInvoicesByUserId(@Path("userId") userId: Long): Response<List<InvoiceDto>>
}