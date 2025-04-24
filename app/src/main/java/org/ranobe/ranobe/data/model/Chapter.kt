package org.ranobe.ranobe.data.model

data class Chapter(
    val id: String?=null,
    val name: String,
    val url: String,
    val published: String? = null,
    val content: String? = null
)
