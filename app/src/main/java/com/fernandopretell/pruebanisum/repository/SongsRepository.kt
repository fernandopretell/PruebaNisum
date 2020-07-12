package com.fernandopretell.pruebanisum.repository

import com.fernandopretell.model.model.ResponseItunesSong
import com.fernandopretell.pruebanisum.base.BaseRepository
import com.fernandopretell.pruebanisum.source.remote.WebServiceData

/**
 * Created by Fernando Pretell on 11/07/2020.
 * fernandopretell93@gmail.com
 *
 * Lima, Peru.
 **/
class SearchSongRepository(private val api : WebServiceData) : BaseRepository() {

    suspend fun getSongsItunes(text: String) : MutableList<ResponseItunesSong>?{

        val itunesResponse = safeApiCall(
            call = {api.getSongs(text).await()},
            errorMessage = "Error Fetching songs"
        )

        return itunesResponse?.toMutableList()

    }

}