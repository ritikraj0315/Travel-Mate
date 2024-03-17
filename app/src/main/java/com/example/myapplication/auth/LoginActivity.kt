package com.example.myapplication.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.screens.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isLoading by remember { mutableStateOf(false) }

            fun loginUser(email: String, password: String) {
                val auth = FirebaseAuth.getInstance()
                isLoading = true

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                "Login successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

            val emailState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.dot_matrix)),
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                        ),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = passwordState.value,
                        onValueChange = { passwordState.value = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        textStyle = TextStyle(color = Color.White, fontWeight = FontWeight.Bold),
                        colors = OutlinedTextFieldDefaults.colors(
                            cursorColor = Color.White,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                            }
                        ),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Forgot Password?",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.dot_matrix)),
                        color = Color.White,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .clickable {
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        ForgotActivity::class.java
                                    )
                                )
                            }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            if (emailState.value.isEmpty() || passwordState.value.isEmpty()) {
                                Toast.makeText(
                                    applicationContext,
                                    "All fields are mandatory!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                loginUser(emailState.value, passwordState.value)
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color.White),
                    ) {
                        Text(
                            text = if (isLoading) "Loading..." else "Login".uppercase(), // Updated: Show "Loading..." when isLoading is true
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Row {
                        Text(
                            text = "Not registered yet? ",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                            color = Color.LightGray,
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                        Text(
                            text = "Register",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(Font(R.font.dot_matrix)),
                            color = Color.White,
                            modifier = Modifier
                                .padding(vertical = 20.dp)
                                .clickable {
                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            RegisterActivity::class.java
                                        )
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}