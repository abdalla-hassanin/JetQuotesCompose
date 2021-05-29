package com.abdalla.jetquotescompose

import androidx.lifecycle.*
import com.abdalla.jetquotescompose.data.MainRepository
import com.abdalla.jetquotescompose.data.Quote
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: MainRepository) : ViewModel() {


    // get all favourites
    val getAllFavourites: LiveData<List<Quote>> = repository.getAllFavourites().asLiveData()


    // insert favourite
    fun insertFavourite(quote: Quote) = viewModelScope.launch {
        repository.insertFavourite(quote)
    }

    // update favourite
    fun updateFavourite(quote: Quote) = viewModelScope.launch {
        repository.updateFavourite(quote)
    }

    // delete favourite
    fun deleteFavourite(quote: Quote) = viewModelScope.launch {
        repository.deleteFavourite(quote)
    }

    val quotesResponse: MutableLiveData<Response<ArrayList<Quote>>> = MutableLiveData()

    fun fetchQuotes() {
        viewModelScope.launch {
            quotesResponse.value = repository.getQuotes()
        }
    }

}

class MainViewModelFactory(private val repository: MainRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
