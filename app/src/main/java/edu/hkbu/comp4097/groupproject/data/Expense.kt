package edu.hkbu.comp4097.groupproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Expense (
    @PrimaryKey
    val id: Int,
    val year: Int,
    val month: Int,
    val date: Int,
    val amount: Int,
    val category: String
){
}