package com.example.myapplication.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.auth.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class IntroActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp),

                        ) {
                        Spacer(modifier = Modifier.height(50.dp))
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_icon),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "NoteMaster".uppercase(),
                            fontSize = 28.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Capturing Thoughts".uppercase(),
                            fontSize = 28.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "NoteMaster is a comprehensive notes app designed to help you capture, organize, and manage your thoughts and ideas efficiently.".uppercase(),
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
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                            },
                            modifier = Modifier
                                .padding(15.dp)
                                .height(50.dp)
                                .fillMaxWidth(),
                            colors = buttonColors(Color.White),
                        ) {
                            Text(
                                text = "LOGIN",
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Font(R.font.dot_matrix)),
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))
                    }

                }
            }
        }
    }
}
