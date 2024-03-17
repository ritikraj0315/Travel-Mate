package com.example.myapplication.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.components.AppBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        setContent {

            val nameState = remember { mutableStateOf("Loading...") }
            val emailState = remember { mutableStateOf("Loading...") }

            if (uid != null) {
                usersRef.document(uid).get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            nameState.value = document.getString("name") ?: "Loading..."
                            emailState.value = document.getString("email") ?: "Loading..."
                        } else {
                            nameState.value = "The User doesn't exist."
                        }
                    } else {
                        task.exception?.message?.let {
                            nameState.value = it;
                        }
                    }
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
                    AppBar().TitleBackBar("Profile")
                    Spacer(modifier = Modifier.height(30.dp))
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_rounded_icon),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp)
                        )
                    }
                    Text(
                        text = nameState.value,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.dot_matrix)),
                        color = Color.White
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.DarkGray, //Card background color
                            contentColor = Color.White  //Card content color,e.g.text
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Email:",
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    color = Color.White,
                                    modifier = Modifier.width(80.dp)
                                )
                                Text(
                                    text = emailState.value ?: "",
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                    color = Color.White,
                                )
                            }
                        }
                    }
                    Button(
                        onClick = {
                            auth.signOut()
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        },
                        modifier = Modifier.width(IntrinsicSize.Max)
                    ) {
                        Text(
                            text = "Logout",
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}