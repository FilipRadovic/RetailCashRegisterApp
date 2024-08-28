package com.frcoding.reatailcashregister.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.frcoding.reatailcashregister.models.Invoice
import kotlinx.coroutines.flow.Flow

@Dao
interface InvoiceDao {
    @Insert
    suspend fun insertInvoice(invoice: Invoice)
    @Query("SELECT * FROM invoices WHERE userId = :userId")
    fun getInvoicesByUserId(userId: Int): Flow<List<Invoice>>

}