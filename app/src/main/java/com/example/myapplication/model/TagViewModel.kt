package com.example.myapplication.model

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore

class TagViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val tagsState = mutableStateOf<List<Tag>>(emptyList())

    fun fetchTagsByUserUid(userUid: String) {
        db.collection("tags")
            .whereEqualTo("user_uid", userUid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                val tagsList = mutableListOf<Tag>()
                for (document in snapshot!!.documents) {
                    val id = document.id
                    val tagName = document.getString("tag_name") ?: ""
                    val userId = document.getString("user_uid") ?: ""

                    val tag = Tag(id, tagName, userId)
                    tagsList.add(tag)
                }

                tagsState.value = tagsList
            }
    }
}
