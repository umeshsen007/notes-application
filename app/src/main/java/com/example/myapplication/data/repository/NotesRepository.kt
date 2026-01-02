package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.NoteDao
import com.example.myapplication.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val noteDao: NoteDao
) {
    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun getNotesByUserId(userId: String): Flow<List<NoteEntity>> = 
        noteDao.getNotesByUserId(userId)

    fun getNoteById(noteId: Int): Flow<NoteEntity?> = noteDao.getNoteById(noteId)

    suspend fun insertNote(note: NoteEntity): Long = noteDao.insertNote(note)

    suspend fun updateNote(note: NoteEntity) = noteDao.updateNote(note)

    suspend fun deleteNote(note: NoteEntity) = noteDao.deleteNote(note)

    suspend fun deleteNoteById(noteId: Int) = noteDao.deleteNoteById(noteId)

    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)
}
