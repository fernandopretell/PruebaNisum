package com.fernandopretell.pruebanisum.source.remote

import com.fernandopretell.model.model.ResponseItunesSong
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by fernandopretell.
 */
interface WebServiceData {

    @GET("search?mediaType=music&limit=20")
    fun getSongs(@Query("term") text_search: String?): Deferred<Response<ResponseItunesSong>>
}