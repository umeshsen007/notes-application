package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.NoteDao
import com.example.myapplication.data.local.entity.NoteEntity
import com.example.myapplication.data.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val firestore: FirebaseFirestore
) {
    private val notesCollection = firestore.collection("notes")
    private val TAG = "NotesRepository"

    fun getAllNotes(): Flow<List<NoteEntity>> = noteDao.getAllNotes()

    fun getNotesByUserId(userId: String): Flow<List<NoteEntity>> = 
        noteDao.getNotesByUserId(userId)

    fun getNoteById(noteId: Int): Flow<NoteEntity?> = noteDao.getNoteById(noteId)

    suspend fun insertNote(note: NoteEntity): Long {
        // Insert into Room database
        val roomId = noteDao.insertNote(note)
        
        // Sync to Firestore
        try {
            val firestoreNote = hashMapOf(
                "id" to roomId.toString(),
                "title" to note.title,
                "content" to note.content,
                "userId" to note.userId,
                "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "color" to note.color
            )
            
            notesCollection
                .document(roomId.toString())
                .set(firestoreNote)
                .await()
            
            Log.d(TAG, "Note synced to Firestore: $roomId")
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing note to Firestore", e)
        }
        
        return roomId
    }

    suspend fun updateNote(note: NoteEntity) {
        // Update in Room database
        noteDao.updateNote(note)
        
        // Sync to Firestore
        try {
            val firestoreNote = hashMapOf(
                "id" to note.id.toString(),
                "title" to note.title,
                "content" to note.content,
                "userId" to note.userId,
                "updatedAt" to com.google.firebase.firestore.FieldValue.serverTimestamp(),
                "color" to note.color
            )
            
            notesCollection
                .document(note.id.toString())
                .update(firestoreNote as Map<String, Any>)
                .await()
            
            Log.d(TAG, "Note updated in Firestore: ${note.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating note in Firestore", e)
        }
    }

    suspend fun deleteNote(note: NoteEntity) {
        // Delete from Room database
        noteDao.deleteNote(note)
        
        // Delete from Firestore
        try {
            notesCollection
                .document(note.id.toString())
                .delete()
                .await()
            
            Log.d(TAG, "Note deleted from Firestore: ${note.id}")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting note from Firestore", e)
        }
    }

    suspend fun deleteNoteById(noteId: Int) {
        // Delete from Room database
        noteDao.deleteNoteById(noteId)
        
        // Delete from Firestore
        try {
            notesCollection
                .document(noteId.toString())
                .delete()
                .await()
            
            Log.d(TAG, "Note deleted from Firestore: $noteId")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting note from Firestore", e)
        }
    }

    fun searchNotes(query: String): Flow<List<NoteEntity>> = noteDao.searchNotes(query)
    
    // Optional: Sync notes from Firestore to local database
    suspend fun syncNotesFromFirestore(userId: String) {
        try {
            val snapshot = notesCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            val notes = snapshot.documents.mapNotNull { doc ->
                try {
                    NoteEntity(
                        id = doc.getString("id")?.toIntOrNull() ?: 0,
                        title = doc.getString("title") ?: "",
                        content = doc.getString("content") ?: "",
                        userId = doc.getString("userId") ?: "",
                        createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis(),
                        updatedAt = doc.getLong("updatedAt") ?: System.currentTimeMillis(),
                        color = doc.getLong("color")?.toInt() ?: 0
                    )
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing note from Firestore", e)
                    null
                }
            }
            
            // Insert all notes into Room database
            notes.forEach { note ->
                noteDao.insertNote(note)
            }
            
            Log.d(TAG, "Synced ${notes.size} notes from Firestore")
        } catch (e: Exception) {
            Log.e(TAG, "Error syncing notes from Firestore", e)
        }
    }
}
