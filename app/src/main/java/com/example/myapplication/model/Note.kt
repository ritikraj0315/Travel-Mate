package com.example.myapplication.model

data class Note(
    val id: String,
    val title: String,
    val description: String,
    val tag: String,
    val color: String,
    val createdAt: String,
    val updatedAt: String
)