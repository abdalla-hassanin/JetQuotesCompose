package com.abdalla.jetquotescompose.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.abdalla.jetquotescompose.MainViewModel
import com.abdalla.jetquotescompose.data.Quote
import com.abdalla.jetquotescompose.utils.Screen

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    buttonTheme: () -> Unit
) {

    Scaffold(
        topBar = {
            HomeTopBar(navController, buttonTheme)
        },
        content = {
            HomeContent(viewModel, navController)
        }
    )
}

@Composable
private fun HomeContent(viewModel: MainViewModel, navController: NavHostController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.fetchQuotes()
    val quotesList = remember { mutableStateListOf<Quote>() }

    viewModel.quotesResponse.observe(lifecycleOwner, { it ->

        if (it.isSuccessful) {
            it.body()?.let {
                quotesList.addAll(it)
            }
        }

    })
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier

                .padding(36.dp, 12.dp, 0.dp, 12.dp)
        ) {
            items(quotesList) {
                val author=if (it.author.isNullOrBlank()) "\\\" - Unknown\\\"" else it.author
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(12.dp)
                        .clickable(onClick = {
                            navController.navigate("${Screen.Details.route}/${it.text}/${author}")
                        })

                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = """ " """,
                            style = MaterialTheme.typography.h4,
                        )
                        Text(
                            text = it.text,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                        Spacer(Modifier.height(12.dp))

                        Box(modifier = Modifier.fillMaxSize()) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(12.dp),
                                text =  author
                               , style = MaterialTheme.typography.caption,
                            )
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTopBar(navController: NavController, buttonTheme: () -> Unit) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp,
        title = { Text(text = "JetQuotes", style = MaterialTheme.typography.h5) },
        actions = {

            IconButton(onClick = {
                navController.navigate(Screen.Favourites.route)
            }) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            IconButton(onClick = {
                buttonTheme()
            }, Modifier.padding(10.dp, 0.dp)) {
                Icon(
                    imageVector = Icons.Outlined.LightMode,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    )
}