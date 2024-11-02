package com.example.haberuygulamasijetpackcompose.view

import android.media.audiofx.AudioEffect.Descriptor
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.haberuygulamasijetpackcompose.viewmodel.NewsViewModel
import com.google.gson.Gson
import com.kwabenaberko.newsapilib.models.Article


@Composable
fun DetailPage(newsViewModel: NewsViewModel) {

    val selectedArticle by newsViewModel.selectedArticle.observeAsState()

    selectedArticle?.let { article ->
        val fullContent = ((article.description ?: "") + "\n\n" +
                (article.content?.replace("[+\\d+ chars]", "") ?: ""))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start // Bu parametre kullanılacak
        ) {
            // Başlık Bölümü
            item {
                Text(
                    text = article.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .background(Color(0xFF444444), shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                        .padding(16.dp)
                )
            }

            // Makale Görseli
            item {
                AsyncImage(
                    model = article.urlToImage ?: "",
                    contentDescription = "Article Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(bottom = 12.dp)
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(10.dp))
                        .background(Color.Gray)
                        .shadow(8.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Yayınlanma Tarihi
            article.publishedAt?.let {
                item {
                    Text(
                        text = "Yayınlanma Tarihi: $it",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )
                }
            }

            // Açıklama ve İçerik Kartı
            item {
                androidx.compose.material3.Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = Color(0xFF333333)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = fullContent,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

            // Orijinal Makaleye Git Butonu
            item {
                androidx.compose.material3.Button(
                    onClick = { /* Kullanıcıyı article.url ile yönlendir */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00796B)
                    )
                ) {
                    Text(text = "Tamamını Oku", color = Color.White)
                }
            }
        }
    } ?: run {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Makale bulunamadı", color = Color.Red, fontSize = 18.sp)
        }
    }
}
