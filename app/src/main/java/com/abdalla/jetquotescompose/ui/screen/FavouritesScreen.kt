package com.abdalla.jetquotescompose.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.abdalla.jetquotescompose.MainViewModel
import com.abdalla.jetquotescompose.R
import com.abdalla.jetquotescompose.data.Quote
import com.abdalla.jetquotescompose.ui.theme.red
import com.abdalla.jetquotescompose.utils.Screen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieAnimationSpec
import com.airbnb.lottie.compose.LottieAnimationState
import com.airbnb.lottie.compose.rememberLottieAnimationState
import kotlinx.coroutines.*

@ExperimentalMaterialApi
@Composable
fun FavouritesScreen(mainViewModel: MainViewModel, navController: NavController) {
    val getAllFavourites by mainViewModel.getAllFavourites.observeAsState()
    Scaffold(
        topBar = {
            FavouriteTopBar(navController)
        },
        content = {
            if (getAllFavourites.isNullOrEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Loader()
                    Text(
                        text = "Your wish list is empty!",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explore more and add some quotes!",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.body2,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onPrimary),
                        onClick = { navController.navigateUp() }
                    ) {
                        Text(text = "Back to home", color = MaterialTheme.colors.primary)
                    }
                }
            } else {
                getAllFavourites?.let { FavouriteContent(it, mainViewModel, navController) }

            }


        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun FavouriteContent(
    list: List<Quote>,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(list) { quote ->
            var unread by remember { mutableStateOf(false) }
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToEnd) unread = !unread
                    it != DismissValue.DismissedToEnd
                }
            )
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = 4.dp),
                directions = setOf(
                    DismissDirection.StartToEnd,
                    DismissDirection.EndToStart
                ),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                },
                background = {
                    val direction =
                        dismissState.dismissDirection ?: return@SwipeToDismiss
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> MaterialTheme.colors.background
                            DismissValue.DismissedToEnd -> red
                            DismissValue.DismissedToStart -> red
                        }
                    )
                    val alignment = when (direction) {
                        DismissDirection.StartToEnd -> Alignment.CenterStart
                        DismissDirection.EndToStart -> Alignment.CenterEnd
                    }
                    val icon = when (direction) {
                        DismissDirection.StartToEnd -> Icons.Outlined.Delete
                        DismissDirection.EndToStart -> Icons.Outlined.Delete
                    }
                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1.5f
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Localized description",
                            modifier = Modifier
                                .scale(scale)
                                .clickable {
                                    mainViewModel.deleteFavourite(quote)
                                },
                            tint = Color.White
                        )
                    }
                },
                dismissContent = {
                    ListItem(
                        text = {
                            Card(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .padding(12.dp)
                                    .clickable(onClick = {
                                        navController.navigate("${Screen.Details.route}/${quote.text}/${quote.author}")
                                    })

                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = """ " """,
                                        style = MaterialTheme.typography.h4,
                                    )
                                    Text(
                                        text = quote.text,
                                        style = MaterialTheme.typography.body1,
                                        modifier = Modifier.padding(start = 12.dp)
                                    )
                                    Spacer(Modifier.height(12.dp))

                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Text(
                                            modifier = Modifier
                                                .align(Alignment.CenterEnd)
                                                .padding(12.dp),
                                            text = quote.author.toString()
                                                .ifBlank { "\\\" - Unknown\\\"" },
                                            style = MaterialTheme.typography.caption,
                                        )
                                        Spacer(Modifier.height(8.dp))
                                    }
                                }
                            }
                        },
                        secondaryText = { Text("Swipe to Remove from favourites!") }
                    )
                }
            )


        }
    }
}

@Composable
fun Loader() {
    val animationSpec: LottieAnimationSpec.RawRes =
        remember { LottieAnimationSpec.RawRes(R.raw.data) }
    val animationState: LottieAnimationState =
        rememberLottieAnimationState(autoPlay = true, repeatCount = Integer.MAX_VALUE)

    LottieAnimation(
        animationSpec,
        modifier = Modifier.size(300.dp),
        animationState = animationState
    )

}

@Composable
fun FavouriteTopBar(navController: NavController) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primary,
        elevation = 0.dp,
        title = { Text(text = "Favourites", style = MaterialTheme.typography.h6) },
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
