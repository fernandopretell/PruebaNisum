package com.fernandopretell.pruebanisum.source.remote

import com.fernandopretell.pruebanisum.util.Constants
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by fernandopretell.
 */
interface WebServiceImages {

    @GET("{image}")
    fun obtenerImagen(@Path("image") image:String): Call<ResponseBody>
}