package com.abdalla.jetquotescompose.data.local

import androidx.room.*
import com.abdalla.jetquotescompose.data.Quote
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM favourites")
    fun getAllFavourites(): Flow<List<Quote>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(quote: Quote)

    @Delete
    suspend fun delete(quote: Quote)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavourite(quote: Quote)


}