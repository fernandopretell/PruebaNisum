package com.fernandopretell.model.model


data class ResponseItunesSong(val trackId: Int,
                    val artistId: Int,
                    val collectionId: Int,
                    val artistName: String,
                    val collectionName: String,
                    val trackName: String,
                    val artworkUrl60: String)