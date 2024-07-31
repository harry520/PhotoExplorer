package com.example.photoexplorer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ImageGridFragment(navController: NavController) {
    var photos by remember { mutableStateOf<List<UnsplashPhoto>?>(null) }
    var loading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    RetrofitClient.instance.getPhotos("W66LiUVIxuJbPEU_UYkIkidmT8H7E0ygD9hvtrLzbh0")
                        .execute()
                if (response.isSuccessful) {
                    photos = response.body()
                    errorMessage = null
                } else errorMessage = "Error: ${response.code()} - ${response.message()}"
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
            } finally {
                loading = false
            }
        }
    }
    if (loading) CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
    else if (errorMessage != null) Text(
        text = errorMessage ?: "Unknown error",
        color = Color.Red,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
    else LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        photos?.let { list ->
            items(list.size) { index ->
                val photo = list[index]
                val encodedUrl = URLEncoder.encode(photo.urls.regular, StandardCharsets.UTF_8.toString())
                Image(
                    painter = rememberAsyncImagePainter(photo.urls.regular),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { navController.navigate("details/${encodedUrl}") },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}