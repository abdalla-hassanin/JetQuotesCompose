package com.abdalla.jetquotescompose.utils

sealed class Screen(val route: String){
    object Home : Screen("quotes")
    object Details : Screen("details")
    object Favourites : Screen("favourites")
}