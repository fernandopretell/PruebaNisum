package com.fernandopretell.pruebanisum.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fernandopretell.model.model.ResponseItunesSong
import com.fernandopretell.pruebanisum.repository.SearchSongRepository
import com.fernandopretell.pruebanisum.source.remote.HelperWs
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Fernando Pretell on 12/07/2020.
 * fernandopretell93@gmail.com
 *
 * Lima, Peru.
 **/
class SearchSongsViewModel : ViewModel(){

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : SearchSongRepository = SearchSongRepository(HelperWs.getServiceData())

    val popularMoviesLiveData = MutableLiveData<MutableList<ResponseItunesSong>>()

    fun fetchSongs(text: String){
        scope.launch {
            val popularMovies = repository.getSongsItunes(text)
            popularMoviesLiveData.postValue(popularMovies)
        }
    }


    fun cancelAllRequests() = coroutineContext.cancel()
}