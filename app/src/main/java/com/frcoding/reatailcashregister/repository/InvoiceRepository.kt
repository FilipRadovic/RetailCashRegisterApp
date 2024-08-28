package com.frcoding.reatailcashregister.repository

import com.frcoding.reatailcashregister.data.dao.InvoiceDao
import com.frcoding.reatailcashregister.models.Invoice
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InvoiceRepository @Inject constructor(
    private val invoiceDao: InvoiceDao
) {
    suspend fun insertInvoice(invoice: Invoice) {
        invoiceDao.insertInvoice(invoice)
    }

    fun getInvoiceByUserId(userId: Int): Flow<List<Invoice>> {
        return invoiceDao.getInvoicesByUserId(userId)
    }
}