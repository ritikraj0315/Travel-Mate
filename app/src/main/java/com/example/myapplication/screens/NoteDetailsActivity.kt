package com.example.myapplication.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.components.AppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NoteDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val tag = intent.getStringExtra("tag")

        setContent {
            val tagState = remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }

            fun deleteNote(id: String) {
                val db = FirebaseFirestore.getInstance()

                if (uid != null) {
                    db.collection("notes")
                        .document(id)
                        .delete()
                        .addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(
                                applicationContext,
                                "Note deleted successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                applicationContext,
                                "Error deleting note: $e",
                                Toast.LENGTH_SHORT
                            ).show()
                            isLoading = false
                        }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "User is not authenticated.",
                        Toast.LENGTH_SHORT
                    ).show()
                    isLoading = false
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .height(60.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_left_square),
                            contentDescription = null,
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .padding(4.dp)
                                .clickable {
                                    onBackPressed()
                                }
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "Note Details",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                            color = Color.White
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp),

                            ) {
                            Text(
                                text = "Title".uppercase(),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (title != null) {
                                Text(
                                    text = title,
                                    fontSize = 17.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "Description".uppercase(),
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            if (description != null) {
                                Text(
                                    text = description,
                                    fontSize = 17.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    color = Color.White
                                )
                            }
                        }
                        Column {
                            Button(
                                onClick = {
                                    val intent = Intent(
                                        this@NoteDetailsActivity,
                                        EditNoteActivity::class.java
                                    )
                                    intent.putExtra("id", id)
                                    intent.putExtra("title", title)
                                    intent.putExtra("desc", description)
                                    intent.putExtra("tag", tag)
                                    startActivity(intent)

                                },
                                modifier = Modifier
                                    .padding(15.dp)
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            ) {
                                Text(
                                    text = "Edit",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                )
                            }
                            Button(
                                onClick = {
                                        isLoading = true
                                        if (id != null) {
                                            deleteNote(id)
                                        }
                                },
                                modifier = Modifier
                                    .padding(top = 0.dp, start = 15.dp, end = 15.dp)
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            ) {
                                Text(
                                    text = if (isLoading) "Loading..." else "Delete",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                )
                            }

                            Spacer(modifier = Modifier.height(5.dp))
                        }

                    }
                }
            }
        }
    }
}