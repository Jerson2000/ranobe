package org.ranobe.ranobe.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.ranobe.ranobe.data.model.Chapter
import org.ranobe.ranobe.data.model.Novel
import org.ranobe.ranobe.data.repository.SourceRepository
import org.ranobe.ranobe.util.EventStates
import org.ranobe.ranobe.util.await
import org.ranobe.ranobe.util.getRequest
import java.util.UUID

class AllNovel(
    private val okhttp: OkHttpClient
) : SourceRepository {
    val baseUrl = "https://allnovel.org"

    override suspend fun novels(page: Int?): Flow<EventStates<List<Novel>>> = flow {
        try {
            emit(EventStates.Loading())
            val response = okhttp.newCall(getRequest("$baseUrl/latest-release-novel")).await()
            if (response.isSuccessful) {
                response.body.use {
                    val body = Jsoup.parse(it.string(), baseUrl)
                    val items = mutableListOf<Novel>()
                    val doc: Elements = body.select("div.col-truyen-main.archive")

                    for (element in doc.select("div.row")) {
                        val url = element.select("h3.truyen-title > a").attr("href").trim()
                        val item = Novel(
                            id = UUID.randomUUID().toString(),
                            name = element.select("h3.truyen-title > a").text().trim(),
                            url = url,
                            cover = element.select("img").text()
                        )
                        items.add(item)
                    }
                    emit(EventStates.Success(items))

                }
            }
        } catch (ex: Exception) {
            emit(EventStates.Error(ex.message))
        }
    }

    override suspend fun chapters(novel: Novel): Flow<EventStates<List<Chapter>>> = flow {

        try {
            emit(EventStates.Loading())
            val response =
                okhttp.newCall(getRequest("$baseUrl/ajax-chapter-option?novelId=${novel.id}"))
                    .await()
            if (response.isSuccessful) {
                response.body.use {
                    val body = Jsoup.parse(it.string(), baseUrl)
                    val items = mutableListOf<Chapter>()

                    for (element in body.select("select option")) {
                        val item = Chapter(
                            name = element.text().trim(),
                            url = element.attr("value").trim(),
                        )
                        items.add(item)
                    }
                    emit(EventStates.Success(items))
                }
            } else {
                emit(EventStates.Error("${response.message}\t ${response.code}"))
            }
        } catch (ex: Exception) {
            emit(EventStates.Error(ex.message))
        }

    }


    override suspend fun detail(novel: Novel): Flow<EventStates<Novel>> = flow {
        try {
            emit(EventStates.Loading())
            val response = okhttp.newCall(getRequest("$baseUrl${novel.url}")).await()
            if (response.isSuccessful) {
                response.body.use {
                    val body = Jsoup.parse(it.string(), baseUrl)
                    val item = Novel(
                        id = body.select("div#rating").attr("data-novel-id").toString().trim(),
                        name = body.select("div.books h3.title").text().trim(),
                        url = novel.url,
                        cover = "$baseUrl${body.select("div.books img").attr("src").trim()}",
                        status = body.select("div:eq(4) > a:eq(1)").text().trim(),
                        genres = body.select("div.info div:eq(2) > a").text().split(",").toList(),
                        author = body.select("div.info div:eq(0) > a:eq(1)").text(),
                        summary = body.select("div.desc-text > p:eq(0)").text(),
                    )
                    emit(EventStates.Success(item))
                }
            }
        } catch (ex: Exception) {
            emit(EventStates.Error(ex.message))
        }
    }

    override suspend fun chapter(chapter: Chapter): Flow<EventStates<Chapter>> = flow {
        try {
            emit(EventStates.Loading())
            val response = okhttp.newCall(getRequest("$baseUrl${chapter.url}")).await()
            if (response.isSuccessful) {
                response.body.use {
                    val body = Jsoup.parse(it.string(), baseUrl)
                    val item = Chapter(
                        chapter.id,
                        chapter.name,
                        chapter.url,
                        chapter.published,
                        contentExtract(body)
                    )
                    emit(EventStates.Success(item))
                }
            }
        } catch (ex: Exception) {
            emit(EventStates.Error(ex.message))
        }
    }

    override suspend fun search(
        query: String,
        filter: List<String>?
    ): Flow<EventStates<List<Novel>>> = flow {
        try {
            emit(EventStates.Loading())
            val response = okhttp.newCall(getRequest("https://nhentai.net/")).await()
            print("TRY $response")
            if (response.isSuccessful) {
                response.body.use {
                    println("SHITTT ${response.body.string()}")
                }
            }
        } catch (ex: Exception) {
            emit(EventStates.Error(ex.message))
        }
    }

    fun contentExtract(body: Document): String {
        val paragraphs = body.select("div.chapter-c p")
        val contentBuilder = StringBuilder()
        for (paragraph in paragraphs) {
            val paragraphText = paragraph.text().trim()
            if (paragraphText.isNotEmpty()) {
                contentBuilder.append(paragraphText).append("::")
            }
        }
        val content = contentBuilder.toString()
        return if (content.isNotEmpty()) {
            content.replace("::", "\n").trim()
        } else {
            ""
        }
    }

}

