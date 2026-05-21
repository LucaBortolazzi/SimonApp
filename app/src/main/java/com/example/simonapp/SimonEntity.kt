package com.example.simonapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
class SimonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,   //aggiunto valore di default
    @ColumnInfo(name = "sequence") val sequence: String,
    @ColumnInfo(name = "errorIndex") val errorIndex: Int,   //indice del primo errore nella sequenza
    @ColumnInfo(name = "maxCorrectLength") val maxCorrectLength: Int
)