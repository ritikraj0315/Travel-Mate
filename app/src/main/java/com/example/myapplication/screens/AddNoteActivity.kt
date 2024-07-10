package com.example.myapplication.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.components.AppBar
import com.example.myapplication.model.Tag
import com.example.myapplication.model.TagViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class AddNoteActivity : ComponentActivity() {
    private lateinit var tagViewModel: TagViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        tagViewModel = ViewModelProvider(this)[TagViewModel::class.java]
        if (uid != null) {
            tagViewModel.fetchTagsByUserUid(uid)
        }

        fun currentDateTime(): String {
            val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDateTime.now()
            } else {
                TODO("VERSION.SDK_INT < O")
            }
            val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
            return currentDateTime.format(formatter)
        }

        setContent {
            val titleState = remember { mutableStateOf("") }
            val descState = remember { mutableStateOf("") }
            val tagState = remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }

            val colorsArray = arrayOf(
                "yellow",
                "orange",
                "blue",
                "green",
                "cream",
            )

            fun addNote(title: String, desc: String, tag: String) {
                val db = FirebaseFirestore.getInstance()

                if (uid != null) {
                    val defaultTag = "All"
                    val tagData = hashMapOf(
                        "title" to title,
                        "description" to desc,
                        if (tagState.value.trim().isEmpty()) {
                            "tag" to defaultTag
                        } else {
                            "tag" to tag
                        },
                        "color" to colorsArray.random(),
                        "user_uid" to uid,
                        "created_at" to currentDateTime(),
                        "updated_at" to currentDateTime(),
                    )
                    db.collection("notes")
                        .add(tagData)
                        .addOnSuccessListener {
                            isLoading = false
                            Toast.makeText(
                                applicationContext,
                                "Note added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackPressed()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                applicationContext,
                                "Error adding note: $e",
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

            val tagsState = tagViewModel.tagsState.value
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
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
                            modifier = Modifier.height(30.dp).width(30.dp).padding(4.dp).clickable {
                                onBackPressed()
                            }
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "Add Note",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(),
                            color = Color.White
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 15.dp),

                            ) {
                            OutlinedTextField(
                                value = titleState.value,
                                onValueChange = { titleState.value = it },
                                label = {
                                    Text(
                                        "Title",
                                        fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 5,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                            Spacer(modifier = Modifier.height(5.dp))
                            OutlinedTextField(
                                value = descState.value,
                                onValueChange = { descState.value = it },
                                label = {
                                    Text(
                                        "Description",
                                        fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                            if (tagState.value.trim().isEmpty()) {
                                Spacer(modifier = Modifier.height(0.dp))
                            } else {
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                width = 1.5.dp,
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(25.dp)
                                            )
                                            .padding(vertical = 6.dp, horizontal = 15.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = tagState.value.trim(),
                                            color = MaterialTheme.colorScheme.primary,
                                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Box(
                                        modifier = Modifier
                                            .border(
                                                width = 1.5.dp,
                                                color = Color.Transparent,
                                                shape = RoundedCornerShape(25.dp)
                                            )
                                            .padding(vertical = 6.dp, horizontal = 15.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "Remove Tag",
                                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.White,
                                            modifier = Modifier.clickable {
                                                tagState.value = ""
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                "Select Tag",
                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            LazyRow {
                                items(tagsState.size) { tag ->
                                    Row {
                                        Box(
                                            modifier = Modifier
                                                .border(
                                                    width = 1.5.dp,
                                                    color = Color.White,
                                                    shape = RoundedCornerShape(25.dp)
                                                )
                                                .clickable {
                                                    tagState.value = tagsState[tag].tagName
                                                }
                                                .padding(vertical = 6.dp, horizontal = 15.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = tagsState[tag].tagName,
                                                color = Color.White,
                                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row {
                                Box(
                                    modifier = Modifier
                                        .border(
                                            width = 1.5.dp,
                                            color = Color.White,
                                            shape = RoundedCornerShape(25.dp)
                                        )
                                        .clickable {
                                            val intent = Intent(
                                                applicationContext,
                                                AddTagActivity::class.java
                                            )
                                            startActivity(intent)
                                        }
                                        .padding(vertical = 6.dp, horizontal = 15.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = R.drawable.add_square_icon),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(20.dp)
                                                .width(20.dp)
                                                .padding(1.5.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = "Add Tag",
                                            color = Color.White,
                                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(
                                text = "With tags, you can quickly retrieve related notes by filtering or searching based on specific tags.".uppercase(),
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
                                    if (titleState.value.trim().isEmpty()) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Please enter title!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (descState.value.trim().isEmpty()) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Please enter description!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        isLoading = true
                                        addNote(
                                            titleState.value.trim(),
                                            descState.value.trim(),
                                            tagState.value.trim()
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .padding(15.dp)
                                    .height(50.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                            ) {
                                Text(
                                    text = if (isLoading) "Loading..." else "Add Note",
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

    @Composable
    fun TagList(tags: List<Tag>) {
        LazyRow {
            items(tags.size) { tag ->
                Row {
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.5.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .clickable {

                            }
                            .padding(vertical = 6.dp, horizontal = 15.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tags[tag].tagName,
                            color = Color.White,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }
    }

}