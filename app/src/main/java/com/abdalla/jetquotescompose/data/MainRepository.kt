package com.abdalla.jetquotescompose.data

import com.abdalla.jetquotescompose.data.local.FavouritesDao
import com.abdalla.jetquotescompose.data.remote.QuotesAPIBuilder
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class MainRepository(private val favouritesDao: FavouritesDao) {

    fun getAllFavourites(): Flow<List<Quote>> = favouritesDao.getAllFavourites()


    suspend fun insertFavourite(quote: Quote) = favouritesDao.insertFavourite(quote)
    suspend fun updateFavourite(quote: Quote) = favouritesDao.updateFavourite(quote)
    suspend fun deleteFavourite(quote: Quote) = favouritesDao.delete(quote = quote)


    suspend fun getQuotes(): Response<ArrayList<Quote>> {
        return QuotesAPIBuilder.retrofit.queryAll()
    }

}