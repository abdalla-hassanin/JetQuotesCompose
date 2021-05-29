package com.abdalla.jetquotescompose

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.abdalla.jetquotescompose.data.MainApplication
import com.abdalla.jetquotescompose.ui.screen.DetailScreen
import com.abdalla.jetquotescompose.ui.screen.FavouritesScreen
import com.abdalla.jetquotescompose.ui.screen.HomeScreen
import com.abdalla.jetquotescompose.ui.theme.JetQuotesComposeTheme
import com.abdalla.jetquotescompose.utils.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as MainApplication).repository)
    }
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    @RequiresApi(Build.VERSION_CODES.M)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // check UI mode
            val darkMode by uiMode.collectAsState(initial = isSystemInDarkTheme())

            // set UI mode accordingly
            val buttonTheme: () -> Unit = {
                lifecycleScope.launch {
                    saveToDataStore(!darkMode)
                }
            }

            JetQuotesComposeTheme(darkMode) {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(MaterialTheme.colors.primaryVariant)
                MyApp(mainViewModel, buttonTheme  )
            }

        }
        // observe theme change
        observeUITheme()
    }

    // used to get the data from datastore
    private val uiMode: Flow<Boolean>
        get() = dataStore.data.map { preferences ->
            val uiMode = preferences[booleanPreferencesKey("ui_mode")] ?: false
            uiMode
        }

    // used to save the ui preference to datastore
    private suspend fun saveToDataStore(isNightMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("ui_mode")] = isNightMode
        }


    }

    private fun observeUITheme() {
        lifecycleScope.launchWhenStarted {
            uiMode.collect {
                val mode = when (it) {
                    true -> AppCompatDelegate.MODE_NIGHT_YES
                    false -> AppCompatDelegate.MODE_NIGHT_NO
                }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.M)
@ExperimentalMaterialApi
@Composable
fun MyApp(viewModel: MainViewModel, buttonTheme: () -> Unit) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                navController,
                buttonTheme
            )
        }
        composable(
            "${Screen.Details.route}/{quote}/{author}",
            arguments = listOf(
                navArgument("quote") { type = NavType.StringType },
                navArgument("author") { type = NavType.StringType })
        ) {
            DetailScreen(
                viewModel,
                navController, quote = it.arguments?.getString("quote") ?: "",
                author = it.arguments?.getString("author") ?: ""
            )
        }
        composable(Screen.Favourites.route) { FavouritesScreen(viewModel, navController) }
    }
}