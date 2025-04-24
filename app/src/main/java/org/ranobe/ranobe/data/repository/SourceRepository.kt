package org.ranobe.ranobe.data.repository

import kotlinx.coroutines.flow.Flow
import org.ranobe.ranobe.data.model.Chapter
import org.ranobe.ranobe.data.model.Novel
import org.ranobe.ranobe.util.EventStates

interface SourceRepository {
    suspend fun novels(page:Int?=1): Flow<EventStates<List<Novel>>>
    suspend fun chapters(novel: Novel): Flow<EventStates<List<Chapter>>>
    suspend fun detail(novel: Novel): Flow<EventStates<Novel>>
    suspend fun chapter(chapter: Chapter): Flow<EventStates<Chapter>>
    suspend fun search(query:String,filter: List<String>?=null): Flow<EventStates<List<Novel>>>
}