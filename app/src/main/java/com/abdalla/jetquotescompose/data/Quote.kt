package com.abdalla.jetquotescompose.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class Quote(

    @NonNull
    @PrimaryKey
    val text: String = "",

    val author: String? = ""
)