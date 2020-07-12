package com.fernandopretell.pruebanisum.source.remote

import com.fernandopretell.model.model.ResponseItunesSong
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Created by fernandopretell.
 */
interface WebServiceData {

    @Headers("Content-Type: application/json")
    @GET("term={text_search}&mediaType=music&limit=20")
    fun getSongs(@Path("text_search") text_search: String?): Deferred<Response<List<ResponseItunesSong>>>
}