package com.example.haberuygulamasijetpackcompose.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.haberuygulamasijetpackcompose.Constants.Constants
import com.kwabenaberko.newsapilib.NewsApiClient
import com.kwabenaberko.newsapilib.models.Article
import com.kwabenaberko.newsapilib.models.request.EverythingRequest
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest
import com.kwabenaberko.newsapilib.models.response.ArticleResponse
import retrofit2.http.Query

class NewsViewModel : ViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> = _articles

    private val _selectedArticle = MutableLiveData<Article?>()
    val selectedArticle: LiveData<Article?> = _selectedArticle

    
    init {
        fetchNewsTopHeadlines()
    }


    fun fetchNewsTopHeadlines(category: String = "GENERAL") {
        val newsApiClient = NewsApiClient(Constants.apiKey)
        val request = TopHeadlinesRequest
            .Builder()
            .language("en")
            .category(category)
            .build()
        newsApiClient.getTopHeadlines(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let {
                    _articles.postValue(it)
                }
            }

            override fun onFailure(throwable: Throwable?) {
                if (throwable != null) {
                    Log.i("NEWS API FAILED", throwable.localizedMessage)
                }
            }
        })
    }

    fun fetchEverything(query: String) {
        val newsApiClient = NewsApiClient(Constants.apiKey)
        val request = EverythingRequest
            .Builder()
            .language("en")
            .q(query)
            .build()
        newsApiClient.getEverything(request, object : NewsApiClient.ArticlesResponseCallback {
            override fun onSuccess(response: ArticleResponse?) {
                response?.articles?.let {
                    _articles.postValue(it)
                }
            }

            override fun onFailure(throwable: Throwable?) {
                if (throwable != null) {
                    Log.i("NEWS API FAILED", throwable.localizedMessage)
                }
            }
        })
    }

    // Seçilen makaleyi ayarlamak için fonksiyon
    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }
}