package com.example.haberuygulamasijetpackcompose.view

import HomePage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.haberuygulamasijetpackcompose.viewmodel.NewsViewModel
import com.example.haberuygulamasijetpackcompose.ui.theme.HaberUygulamasiJetpackComposeTheme
import com.kwabenaberko.newsapilib.models.Article

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        setContent {

            val navController = rememberNavController()

            HaberUygulamasiJetpackComposeTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomePage(
                                newsViewModel = newsViewModel,
                                navController = navController
                            )
                        }
                        composable("detail") { backStackEntry ->
                            DetailPage(newsViewModel = newsViewModel)
                        }
                    }
                }
            }

        }
    }
}


