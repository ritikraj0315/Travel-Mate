package com.example.myapplication.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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

class AddTagActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val tagState = remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }

            fun addTag(tag: String) {
                val auth = FirebaseAuth.getInstance()
                val db = FirebaseFirestore.getInstance()
                val uid = auth.currentUser?.uid

                if (uid != null) {
                    val tagData = hashMapOf(
                        "tag_name" to tag,
                        "user_uid" to uid,
                    )
                    db.collection("tags")
                        .add(tagData)
                        .addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(
                                applicationContext,
                                "Tag added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                applicationContext,
                                "Error adding tag: $e",
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
                    AppBar().TitleBackBar("Add Tag")
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp),

                            ) {
                            OutlinedTextField(
                                value = tagState.value,
                                onValueChange = { tagState.value = it },
                                label = {
                                    Text(
                                        "Tag Name",
                                        fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                textStyle = TextStyle(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = Color.White,
                                    focusedBorderColor = Color.White,
                                    unfocusedBorderColor = Color.Gray,
                                ),
                            )

                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "Using tags can be a powerful organizational tool for you to categorize and find your notes more efficiently".uppercase(),
                                fontSize = 15.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Light,
                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                color = Color.Gray
                            )
                        }
                        Column {
                            Button(
                                onClick = {
                                    if (tagState.value.isEmpty()) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Please enter tag name!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        isLoading = true
                                        addTag(tagState.value.trim())
                                    }
                                },
                                modifier = Modifier
                                    .padding(15.dp)
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            ) {
                                Text(
                                    text = if (isLoading) "Loading..." else "Add Tag",
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