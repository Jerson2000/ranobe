package org.ranobe.ranobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.ranobe.ranobe.data.model.Chapter
import org.ranobe.ranobe.data.model.Novel
import org.ranobe.ranobe.data.repository.SourceRepository
import org.ranobe.ranobe.util.EventStates

class ViewModel(
    private val novel: SourceRepository
): ViewModel() {

    private val _list = MutableStateFlow<EventStates<List<Novel>>?>(null)
    val novels: StateFlow<EventStates<List<Novel>>?> = _list

    fun getNovels(){
        viewModelScope.launch{

        }
    }
}