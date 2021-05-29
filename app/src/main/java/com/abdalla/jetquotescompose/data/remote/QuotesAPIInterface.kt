package com.abdalla.jetquotescompose.data.remote

import com.abdalla.jetquotescompose.data.Quote
import retrofit2.Response
import retrofit2.http.GET

interface QuotesAPIInterface {

    @GET("quotes")
    suspend fun queryAll() : Response<ArrayList<Quote>>
}