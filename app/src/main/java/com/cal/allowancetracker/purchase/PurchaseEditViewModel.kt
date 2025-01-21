package com.cal.allowancetracker.purchase

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cal.allowancetracker.data.Transaction
import com.cal.allowancetracker.data.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PurchaseEditViewModel(
    private val transactionsRepository: TransactionsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    var purchaseUiState by mutableStateOf(PurchaseUiState())

    var purchaseId: Int = checkNotNull(savedStateHandle[PurchaseEditDestination.purchaseIdArg])

    init {
        if (purchaseId != 0) {
            getTransaction()
        } else {
            Log.e("PurchaseEditViewModel", "Purchase ID is null. Cannot load transaction.")
        }
    }

    fun updateUiState(purchaseDetails: PurchaseDetails) {
        purchaseUiState = PurchaseUiState(purchaseDetails, isEntryValid = validateInput(purchaseDetails))
    }

    private fun validateInput(uiState: PurchaseDetails = purchaseUiState.purchaseDetails): Boolean {
        return with(uiState) {
            amount.isNotBlank() && description.isNotBlank()
        }
    }

    private fun getTransaction() {
        viewModelScope.launch {
            transactionsRepository.getTransaction(purchaseId).collectLatest {
                it?.let {
                    purchaseUiState = PurchaseUiState(PurchaseDetails(id = it.id, amount = it.amount.toString(), description = it.description, isDeposit = it.isDeposit, date = it.date ))
                }
            }
        }
    }

    suspend fun saveTransaction() {
        if (validateInput()) {
            transactionsRepository.updateTransaction(purchaseUiState.purchaseDetails.toTransaction())
        }
    }
}