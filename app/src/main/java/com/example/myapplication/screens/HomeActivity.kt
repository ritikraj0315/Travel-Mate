package com.example.myapplication.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.auth.LoginActivity
import com.example.myapplication.components.AppBar
import com.example.myapplication.model.Note
import com.example.myapplication.model.NotesViewModel
import com.example.myapplication.model.Tag
import com.example.myapplication.model.TagViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : ComponentActivity() {
    private lateinit var tagViewModel: TagViewModel
    private lateinit var noteViewModel: NotesViewModel

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        tagViewModel = ViewModelProvider(this)[TagViewModel::class.java]
        noteViewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        if (uid != null) {
            tagViewModel.fetchTagsByUserUid(uid)
            noteViewModel.fetchNotesByUserUid(uid, "All")
        }

        setContent {
            val tagsState = tagViewModel.tagsState.value
            val notesState = noteViewModel.notesState.value

            val allState = remember { mutableStateOf("All") }

            fun nameChanger(name: String) {
                allState.value = name
            }

            Surface(
                modifier = Modifier.fillMaxSize(), color = Color.Black
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .height(50.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "Notes",
                                fontSize = 35.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Light,
                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                color = Color.White
                            )
                            Box(modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .clickable {
                                    val intent =
                                        Intent(applicationContext, ProfileActivity::class.java)
                                    startActivity(intent)
                                }) {
                                Image(
                                    painter = painterResource(id = R.drawable.user_rounded_icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(7.dp),

                                    )
                            }
                        }
                        //body
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Spacer(Modifier.height(20.dp))
                            LazyRow {
                                items(tagsState.size) { tag ->
                                    Row {
                                        Box(modifier = Modifier
                                            .border(
                                                width = 1.5.dp,
                                                color = Color.White,
                                                shape = RoundedCornerShape(25.dp)
                                            )
                                            .combinedClickable(onClick = {
                                                if (uid != null) {
                                                    noteViewModel.fetchNotesByUserUid(
                                                        uid, tagsState[tag].tagName
                                                    )
                                                    nameChanger(tagsState[tag].tagName)
                                                }
                                            }, onLongClick = {
                                                val intent = Intent(
                                                    this@HomeActivity,
                                                    DeleteTagActivity::class.java
                                                )
                                                intent.putExtra(
                                                    "tagId", tagsState[tag].id
                                                )
                                                intent.putExtra(
                                                    "tag", tagsState[tag].tagName
                                                )
                                                startActivity(intent)
                                            }, onLongClickLabel = ""
                                            )
                                            .padding(vertical = 6.dp, horizontal = 15.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = tagsState[tag].tagName,
                                                color = Color.White,
                                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(15.dp))
                                    }
                                }
                            }
                            Spacer(Modifier.height(15.dp))
                            Text(
                                "#" + allState.value,
                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White
                            )
                            Spacer(Modifier.height(10.dp))
                            if (notesState.isEmpty()) {
                                Spacer(modifier = Modifier.height(150.dp))
                                Text(
                                    "No data found!",
                                    fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(2),
                                    verticalItemSpacing = 8.dp,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    content = {
                                        items(notesState.size) { note ->
                                            Row {
                                                val colors = when (notesState[note].color) {
                                                    "yellow" -> CardDefaults.cardColors(
                                                        containerColor = Color(0xFFF7D44C),
                                                        contentColor = Color.Black
                                                    )

                                                    "orange" -> CardDefaults.cardColors(
                                                        containerColor = Color(0xFFEB7A53),
                                                        contentColor = Color.Black
                                                    )

                                                    "blue" -> CardDefaults.cardColors(
                                                        containerColor = Color(0xFF98B7DB),
                                                        contentColor = Color.Black
                                                    )

                                                    "green" -> CardDefaults.cardColors(
                                                        containerColor = Color(0xFFA8D672),
                                                        contentColor = Color.Black
                                                    )

                                                    "cream" -> CardDefaults.cardColors(
                                                        containerColor = Color(0xFFF6ECC9),
                                                        contentColor = Color.Black
                                                    )

                                                    else -> CardDefaults.cardColors()
                                                }
                                                Card(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .combinedClickable(
                                                            onClick = {
                                                                val intent = Intent(
                                                                    this@HomeActivity,
                                                                    NoteDetailsActivity::class.java
                                                                )
                                                                intent.putExtra(
                                                                    "id", notesState[note].id
                                                                )
                                                                intent.putExtra(
                                                                    "title", notesState[note].title
                                                                )
                                                                intent.putExtra(
                                                                    "description",
                                                                    notesState[note].description
                                                                )
                                                                intent.putExtra(
                                                                    "tag", notesState[note].tag
                                                                )
                                                                startActivity(intent)
                                                            },
//                                                            onLongClick = {
//                                                                Toast
//                                                                    .makeText(
//                                                                        applicationContext,
//                                                                        "Long click",
//                                                                        Toast.LENGTH_SHORT
//                                                                    )
//                                                                    .show()
//
//                                                            },
                                                            onLongClickLabel = ""
                                                        )
                                                        .padding(0.dp), colors = colors
                                                ) {
                                                    Column(
                                                        modifier = Modifier.padding(16.dp),
                                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        Column {
                                                            Text(
                                                                text = notesState[note].title,
                                                                textAlign = TextAlign.Start,
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 16.sp,
                                                                maxLines = 2,
                                                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                                                color = Color.Black,
                                                                modifier = Modifier.width(80.dp)
                                                            )
                                                            Spacer(modifier = Modifier.height(8.dp))
                                                            Text(
                                                                text = notesState[note].description,
                                                                textAlign = TextAlign.Start,
                                                                fontWeight = FontWeight.Normal,
                                                                fontSize = 14.sp,
                                                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                                                maxLines = 5,
                                                                color = Color.Black,
                                                                modifier = Modifier.width(80.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                        }
                    }
                    ExtendedFloatingActionButton(
                        modifier = Modifier
                            .padding(30.dp)
                            .align(Alignment.BottomEnd),
                        onClick = {
                            val intent = Intent(applicationContext, AddNoteActivity::class.java)
                            startActivity(intent)
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.add_square_icon),
                                contentDescription = "Extended floating action button."
                            )
                        },
                        text = {
                            Text(
                                text = "Add Note",
                                fontSize = 15.sp,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.Normal,
                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                color = Color.White
                            )
                        },
                    )
                }
            }
        }

    }
}
