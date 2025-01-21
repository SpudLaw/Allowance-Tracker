package com.cal.allowancetracker.data

import java.util.Date
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "transactions")
@TypeConverters(Converters::class)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val isDeposit: Boolean,
    val description: String,
    val date: Date
)