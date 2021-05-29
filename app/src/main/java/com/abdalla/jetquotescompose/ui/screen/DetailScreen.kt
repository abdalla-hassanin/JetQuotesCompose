package com.abdalla.jetquotescompose.ui.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.abdalla.jetquotescompose.MainViewModel
import com.abdalla.jetquotescompose.data.Quote
import com.abdalla.jetquotescompose.utils.copyToClipboard
import com.abdalla.jetquotescompose.utils.shareToOthers

@Composable
fun DetailScreen(
    mainViewModel: MainViewModel,
    navController: NavController,
    quote: String,
    author: String
) {
    Scaffold(
        topBar = {
            DetailTopBar(navController)
        },
        content = {
            DetailContent(mainViewModel,quote, author)
        }
    )
}

@Composable
private fun DetailContent(mainViewModel: MainViewModel, quote: String, author: String) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .clickable(onClick = {
                    context.copyToClipboard("$quote - $author")
                    Toast
                        .makeText(context, "Quote copied!", Toast.LENGTH_SHORT)
                        .show()
                })
                .align(Alignment.Center)
        ) {
            Column(
                modifier = Modifier
                    .padding(52.dp),

                ) {

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .wrapContentSize(align = Alignment.Center),
                    text = """ " """,
                    style = MaterialTheme.typography.h4,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .wrapContentSize(align = Alignment.Center),
                    text = quote.ifBlank { "\\\" No Quotes found\\\"" },
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = author.ifBlank { "\\\" - Unknown\\\"" },
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd).fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(30.dp, 30.dp, 30.dp, 30.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier
                            .clickable {
                                mainViewModel.insertFavourite(Quote(quote, author))
                                Toast.makeText(context, "Added to Favourites!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null)
                        Text(text = "Favourite")
                    }



                    Column(
                        modifier = Modifier
                            .clickable {
                                context.copyToClipboard("$quote - $author")
                                Toast.makeText(context, "Quote Copied!", Toast.LENGTH_SHORT).show()
                            }
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.ContentCopy,
                            contentDescription = null
                        )
                        Text(text = "Copy")
                    }



                    Column(
                        modifier = Modifier
                            .clickable {
                                context.shareToOthers("$quote - $author")
                            }
                            .padding(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(imageVector = Icons.Outlined.Share, contentDescription = null)
                        Text(text = "Share")
                    }




                }
            }

        }
    }
}

@Composable
private fun DetailTopBar(navController: NavController) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp,
        title = { Text(text = "Details", style = MaterialTheme.typography.h6) },
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}