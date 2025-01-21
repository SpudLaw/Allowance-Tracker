package com.cal.allowancetracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Update
    suspend fun update(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE isDeposit = 0 ORDER BY date ASC")
    fun getAllPurchases(): Flow<List<Transaction>>

    @Query("SELECT * from transactions WHERE id = (:id)")
    fun getTransaction(id: Int): Flow<Transaction>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query(
        """
    SELECT 
        IFNULL(SUM(CASE WHEN isDeposit = 1 THEN amount ELSE 0 END), 0) 
        - 
        IFNULL(SUM(CASE WHEN isDeposit = 0 THEN amount ELSE 0 END), 0) 
        AS balance
    FROM transactions
    """
    )
    fun getBalance(): Flow<Double>
}