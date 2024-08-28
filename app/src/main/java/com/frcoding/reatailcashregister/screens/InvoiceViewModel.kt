package com.frcoding.reatailcashregister.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frcoding.reatailcashregister.data.prefs.SessionManager
import com.frcoding.reatailcashregister.models.Invoice
import com.frcoding.reatailcashregister.repository.InvoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class InvoiceViewModel @Inject constructor(
    private val invoiceRepository: InvoiceRepository,
    private val sessionManager: SessionManager
): ViewModel() {

    private val _invoices = MutableStateFlow<List<Invoice>>(emptyList())
    val invoices: StateFlow<List<Invoice>> = _invoices.asStateFlow()

    init {
        fetchInvoices()
    }

    fun addInvoice(userId: Int, totalPrice: Double, paymentMethod: String) {
        viewModelScope.launch {
            val invoice = Invoice(
                userId = userId,
                totalPrice = totalPrice,
                paymentMethod = paymentMethod
            )
            invoiceRepository.insertInvoice(invoice)
        }
    }

    private fun fetchInvoices() {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: return@launch
            invoiceRepository.getInvoiceByUserId(userId)
                .collect { invoiceList ->
                    _invoices.value = invoiceList
                }
        }
    }

}