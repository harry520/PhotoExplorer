package com.example.photoexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photoexplorer.ui.theme.PhotoExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoExplorerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
@Preview
fun Navigation() {
    val navController = rememberNavController()
    var transitionState by remember { mutableStateOf(TransitionState.Initial) }
    NavHost(navController, startDestination = "image_grid") {
        composable("image_grid") { backStackEntry ->
            transitionState = TransitionState.Entering
            ImageGridFragment(navController)
        }
        composable("details/{photoUrl}") { backStackEntry ->
            transitionState = TransitionState.Exiting
            val photoUrl = backStackEntry.arguments?.getString("photoUrl")
            PhotoDetailsFragment(photoUrl)
        }
    }
    AnimatedVisibility(
        visible = transitionState == TransitionState.Entering,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(300)),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(tween(300))
    ) {
    }
    AnimatedVisibility(
        visible = transitionState == TransitionState.Exiting,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(tween(300)),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(tween(300))
    ) {
    }
}