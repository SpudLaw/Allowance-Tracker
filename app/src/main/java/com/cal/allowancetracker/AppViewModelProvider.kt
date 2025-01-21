package com.cal.allowancetracker

import HomeViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cal.allowancetracker.purchase.PurchaseEditViewModel
import com.cal.allowancetracker.purchase.PurchaseEntryViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            PurchaseEditViewModel(allowanceTrackerApplication().container.transactionsRepository, this.createSavedStateHandle())
        }
        initializer {
            PurchaseEntryViewModel(allowanceTrackerApplication().container.transactionsRepository)
        }
        initializer {
            HomeViewModel(allowanceTrackerApplication().container.transactionsRepository)
        }
    }
}

fun CreationExtras.allowanceTrackerApplication(): AllowanceTrackerApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AllowanceTrackerApplication)