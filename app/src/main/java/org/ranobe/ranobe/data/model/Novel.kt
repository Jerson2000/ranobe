package org.ranobe.ranobe.data.model

data class Novel(
    val id:String,
    val name: String,
    val url: String,
    val cover: String,
    val status: String,
    val author: String,
    val rating: Double,
    val summary: String,
    val published: String,
    val genres: List<String>,
) {

    constructor(
        id:String,
        name: String,
        url: String,
        cover: String? = null,
        status: String? = null,
        author: String? = null,
        rating: Double? = null,
        summary: String? = null,
        published: String? = null,
        genres: List<String>?=null,
    ) : this(
        id,
        name,
        url,
        cover ?: "",
        status ?: "None",
        author ?: "Unknown",
        rating ?: 0.0,
        summary ?: "There's no summary for this novel.",
        published ?: "Undetermined",
        genres?:mutableListOf()
    )
}