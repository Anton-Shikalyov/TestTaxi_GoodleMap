package com.example.testtaxi.dto

import kotlinx.serialization.Serializable

@Serializable
class JsonList(val albumId: Int, val id: Int, val title: String, val url: String, val thumbnailUrl: String) {
}