package com.example.myapplication.model

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore

class NotesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val notesState = mutableStateOf<List<Note>>(emptyList())

    fun fetchNotesByUserUid(userUid: String, tag: String) {

        if (tag == "All") {
            db.collection("notes")
                .whereEqualTo("user_uid", userUid)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    val notesList = mutableListOf<Note>()
                    for (document in snapshot!!.documents) {
                        val id = document.id
                        val noteTitle = document.getString("title") ?: ""
                        val noteDesc = document.getString("description") ?: ""
                        val tags = document.getString("tag") ?: ""
                        val color = document.getString("color") ?: ""
                        val created = document.getString("created_at") ?: ""
                        val updated = document.getString("updated_at") ?: ""

                        val note = Note(id, noteTitle, noteDesc, tags, color, created, updated)
                        notesList.add(note)
                    }

                    notesState.value = notesList
                }
        } else {
            db.collection("notes")
                .whereEqualTo("user_uid", userUid).whereEqualTo("tag", tag)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    val notesList = mutableListOf<Note>()
                    for (document in snapshot!!.documents) {
                        val id = document.id
                        val noteTitle = document.getString("title") ?: ""
                        val noteDesc = document.getString("description") ?: ""
                        val tags = document.getString("tag") ?: ""
                        val color = document.getString("color") ?: ""
                        val created = document.getString("created_at") ?: ""
                        val updated = document.getString("updated_at") ?: ""

                        val note = Note(id, noteTitle, noteDesc, tags, color, created, updated)
                        notesList.add(note)
                    }

                    notesState.value = notesList
                }
        }
    }
}
