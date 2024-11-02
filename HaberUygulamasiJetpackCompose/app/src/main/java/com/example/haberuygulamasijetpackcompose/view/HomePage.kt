
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.haberuygulamasijetpackcompose.viewmodel.NewsViewModel
import com.kwabenaberko.newsapilib.models.Article
import com.google.gson.Gson


@Composable
fun HomePage(newsViewModel: NewsViewModel, navController: NavHostController) {

    val articles by newsViewModel.articles.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        CategoriesBar(newsViewModel)

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // İlk haberi büyük gösteriyoruz
            item {
                if (articles.isNotEmpty()) {
                    ArticleItemBig(article = articles[0],navController,newsViewModel)
                }
            }

            // Diğer haberler 2 sütun halinde alt alta gösteriliyor
            items(articles.drop(1).chunked(2)) { articlePair ->
                Row(
                    modifier = Modifier.
                    fillMaxWidth()
                    ,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    articlePair.forEach { article ->
                        ArticleItemSmall(article,navController,newsViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleItemBig(article: Article,navController: NavHostController,newsViewModel: NewsViewModel) {
    val gson = Gson()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                newsViewModel.selectArticle(article)
                navController.navigate("detail")
// JSON'u serileştirirken hata oluşabileceği için bir try-catch ekleyelim

            }
        // .padding(0.dp)
    ) {
        AsyncImage(
            model = article.urlToImage ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNNLEL-qmmLeFR1nxJuepFOgPYfnwHR56vcw&s",
            contentDescription = "Big News Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
                .height(200.dp), // Büyük resim için yükseklik ayarlandı
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp)) // Resim ile metin arası boşluk
        Text(
            text = article.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp, // Büyük başlık
            maxLines = 2,
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = article.description ?: "",
            fontSize = 14.sp,
            maxLines = 3,
            modifier = Modifier.padding(4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp)) // Alt yazı ile metin arası boşluk
        Text(
            text = article.source.name,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ArticleItemSmall(article: Article,navController: NavHostController,newsViewModel: NewsViewModel) {
    val gson = Gson()
    Column(
        modifier = Modifier
            .width(180.dp)
            .padding(4.dp)
            .clickable {
                newsViewModel.selectArticle(article)
                navController.navigate("detail")
            }
    ) {
        AsyncImage(
            model = article.urlToImage ?: "https://placekitten.com/400/200",
            contentDescription = "Small News Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp)) // Resim ile metin arası boşluk
        Text(
            text = article.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp, // Küçük başlık
            maxLines = 2,
            modifier = Modifier.padding(4.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = article.source.name,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun CategoriesBar(newsViewModel: NewsViewModel) {

    var searchBar by remember { mutableStateOf("") }
    var isSearchExpended by remember { mutableStateOf(false) }

    val categoriesList = listOf(
        "GENERAL", "BUSINESS", "ENTERTAINMENT", "SCIENCE", "HEALTH", "TECHNOLOGY"
    )

    // Seçilen kategoriyi takip etmek için bir durum ekleyelim
    var selectedCategory by remember { mutableStateOf(categoriesList[0]) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if (isSearchExpended) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(7.dp)
                    .height(48.dp)
                    .border(1.dp, Color.Gray, CircleShape)
                    .clip(CircleShape),
                value = searchBar,
                onValueChange = {
                    searchBar = it
                },
                trailingIcon = {
                    IconButton(onClick = {
                        isSearchExpended = false
                        if (searchBar.isNotEmpty()) {
                            newsViewModel.fetchEverything(searchBar)
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Bar")
                    }
                }
            )
        } else {
            IconButton(onClick = {
                isSearchExpended = true
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Bar")
            }
        }

        categoriesList.forEachIndexed { index, category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(3.dp)
                    .clickable {
                        selectedCategory = category
                        newsViewModel.fetchNewsTopHeadlines(category)
                    }
            ) {
                Text(
                    text = category,
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Monospace,
                    color = if (selectedCategory == category) Color.Yellow else Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Seçilen kategoriye bağlı olarak altına çizgi ekliyoruz
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(30.dp)
                        .background(if (selectedCategory == category) Color.Yellow else Color.Transparent)
                )
            }

            // Kategoriler arasına çizgi
            if (index < categoriesList.size - 1) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(Color.Gray)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}