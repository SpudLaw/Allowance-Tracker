package com.cal.allowancetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class TransactionDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var Instance: TransactionDatabase? = null

        fun getDatabase(context: Context): TransactionDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.

            val converters = Converters()

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TransactionDatabase::class.java, "transaction_database")
                    .addTypeConverter(converters)
                    .build()
                    .also { Instance = it }
            }
        }
    }

}