package com.cal.allowancetracker.data

import kotlinx.coroutines.flow.Flow

interface TransactionsRepository {
    fun getTransaction(id: Int): Flow<Transaction?>

    suspend fun getPurchases(): Flow<List<Transaction>>

    suspend fun getTransactions(): Flow<List<Transaction>>

    suspend fun getBalance(): Flow<Double>

    suspend fun insertTransaction(transaction: Transaction)

    suspend fun updateTransaction(transaction: Transaction)
}