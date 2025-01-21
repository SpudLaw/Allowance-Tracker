package com.cal.allowancetracker.data

import android.content.Context

interface AppContainer {
    val transactionsRepository: TransactionsRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val transactionsRepository: TransactionsRepository by lazy {
        OfflineTransactionsRepository(TransactionDatabase.getDatabase(context).transactionDao())
    }
}