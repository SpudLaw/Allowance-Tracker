package com.cal.allowancetracker.purchase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cal.allowancetracker.data.Transaction
import com.cal.allowancetracker.data.TransactionsRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PurchaseEntryViewModel(private val transactionsRepository: TransactionsRepository): ViewModel() {

    var purchaseUiState by mutableStateOf(PurchaseUiState())
            private set

    fun updateUiState(purchaseDetails: PurchaseDetails) {
        purchaseUiState = PurchaseUiState(purchaseDetails = purchaseDetails, isEntryValid = validateInput(purchaseDetails))
    }

    private fun validateInput(uiState: PurchaseDetails = purchaseUiState.purchaseDetails): Boolean {
        return with(uiState) {
             amount.isNotBlank() && description.isNotBlank()
        }
    }

    suspend fun saveTransaction() {
        if (validateInput()) {
            transactionsRepository.insertTransaction(purchaseUiState.purchaseDetails.toTransaction())
        }
    }
}

data class PurchaseUiState(
    val purchaseDetails: PurchaseDetails = PurchaseDetails(date = Date()),
    val isEntryValid: Boolean = false
)

data class PurchaseDetails(
    val id: Int = 0,
    val amount: String = "",
    val isDeposit: Boolean = false,
    val description: String = "",
    val date: Date
)

fun formatDateToString(date: Date, format: String = "MM/dd/yyyy"): String {
    return try {
        val formatter = SimpleDateFormat(format, Locale.US)
        formatter.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        "Invalid date"
    }
}

fun PurchaseDetails.toTransaction(): Transaction = Transaction(
    id = id,
    amount = amount.toDouble(),
    isDeposit = isDeposit,
    description = description,
    date = date
)

fun Transaction.formattedAmount(): String {
    return NumberFormat.getCurrencyInstance().format(amount)
}

fun Transaction.toTransactionUiState(isEntryValid: Boolean = false): PurchaseUiState = PurchaseUiState(
    purchaseDetails = this.toTransactionDetails(),
    isEntryValid = isEntryValid
)

fun Transaction.toTransactionDetails(): PurchaseDetails = PurchaseDetails(
    id = id,
    amount = amount.toString(),
    isDeposit = isDeposit,
    description = description,
    date = date
)