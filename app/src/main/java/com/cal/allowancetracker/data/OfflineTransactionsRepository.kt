package com.cal.allowancetracker.data

import kotlinx.coroutines.flow.Flow

class OfflineTransactionsRepository(private val transactionDao: TransactionDao): TransactionsRepository {
    override fun getTransaction(id: Int): Flow<Transaction?> = transactionDao.getTransaction(id)

    override suspend fun getBalance(): Flow<Double> = transactionDao.getBalance()

    override suspend fun getPurchases(): Flow<List<Transaction>> = transactionDao.getAllPurchases()

    override suspend fun getTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    override suspend fun insertTransaction(transaction: Transaction) = transactionDao.insert(transaction)

    override suspend fun updateTransaction(transaction: Transaction) = transactionDao.update(transaction)
}