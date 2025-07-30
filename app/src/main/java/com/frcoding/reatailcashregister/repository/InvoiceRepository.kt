package com.frcoding.reatailcashregister.repository

import com.frcoding.reatailcashregister.data.dao.InvoiceApi
import com.frcoding.reatailcashregister.data.mappers.toInvoice
import com.frcoding.reatailcashregister.data.mappers.toInvoiceDto
import com.frcoding.reatailcashregister.models.Invoice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class InvoiceRepository @Inject constructor(
    private val invoiceApi: InvoiceApi
) {
    suspend fun insertInvoice(invoice: Invoice) = invoiceApi.insertInvoices(invoice.toInvoiceDto())

    suspend fun getInvoiceByUserId(userId: Long): Flow<List<Invoice>> = flow {
        val response = invoiceApi.getInvoicesByUserId(userId)
        if (response.isSuccessful) {
            val invoices = response.body()?.map { it.toInvoice() } ?: emptyList()
            emit(invoices)
        } else {
            throw HttpException(response)
        }
    }
}