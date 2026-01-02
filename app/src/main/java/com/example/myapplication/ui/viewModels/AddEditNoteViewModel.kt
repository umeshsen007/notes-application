package com.example.myapplication.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.NoteEntity
import com.example.myapplication.data.repository.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val repository: NotesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Int? = savedStateHandle.get<String>("noteId")?.toIntOrNull()

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()

    private val _color = MutableStateFlow(0)
    val color: StateFlow<Int> = _color.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        noteId?.let { id ->
            viewModelScope.launch {
                repository.getNoteById(id).collectLatest { note ->
                    note?.let {
                        _title.value = it.title
                        _content.value = it.content
                        _color.value = it.color
                    }
                }
            }
        }
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }

    fun updateColor(newColor: Int) {
        _color.value = newColor
    }

    fun saveNote(onSaved: () -> Unit) {
        viewModelScope.launch {
            if (_title.value.isBlank() && _content.value.isBlank()) {
                onSaved()
                return@launch
            }

            _isSaving.value = true

            val note = NoteEntity(
                id = noteId ?: 0,
                title = _title.value.trim(),
                content = _content.value.trim(),
                color = _color.value,
                updatedAt = System.currentTimeMillis()
            )

            repository.insertNote(note)
            _isSaving.value = false
            onSaved()
        }
    }
}
